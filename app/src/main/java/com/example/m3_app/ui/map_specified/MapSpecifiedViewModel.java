package com.example.m3_app.ui.map_specified;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.m3_app.backend.RouteConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

public class MapSpecifiedViewModel extends AndroidViewModel {
    private final MutableLiveData<List<RouteConfig.Route>> allRoutes = new MutableLiveData<>();

    public LiveData<List<RouteConfig.Route>> getAllRoutes() {
        return allRoutes;
    }
    public MapSpecifiedViewModel(Application app) {
       super(app);

       try(Reader r = new InputStreamReader(app.getAssets().open("filter_config.json"))) {
           RouteConfig config = new Gson().fromJson(r, RouteConfig.class);
           allRoutes.setValue(config.routes);
       } catch (IOException e) {
          allRoutes.setValue(Collections.emptyList());
       }
    }
}

