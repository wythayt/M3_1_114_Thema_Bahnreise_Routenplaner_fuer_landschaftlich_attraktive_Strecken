package com.example.m3_app.ui.route_config;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.example.m3_app.backend.RouteConfig;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteConfigViewModel extends AndroidViewModel {
    private final MutableLiveData<List<RouteConfig.Route>> allRoutes = new MutableLiveData<>();
    private final Map<String, RouteConfig.Route> byId        = new HashMap<>();

    public RouteConfigViewModel(@NonNull Application app) {
        super(app);
        try (Reader r = new InputStreamReader(
                app.getAssets().open("filter_config.json"))) {
            RouteConfig cfg = new Gson().fromJson(r, RouteConfig.class);
            allRoutes.setValue(cfg.routes);
            for (RouteConfig.Route route : cfg.routes) {
                byId.put(route.id, route);
            }
        } catch (IOException e) {
            allRoutes.setValue(Collections.emptyList());
        }
    }

    public LiveData<List<RouteConfig.Route>> getAllRoutes() {
        return allRoutes;
    }

    public RouteConfig.Route getRouteById(String id) {
        return byId.get(id);
    }
}

