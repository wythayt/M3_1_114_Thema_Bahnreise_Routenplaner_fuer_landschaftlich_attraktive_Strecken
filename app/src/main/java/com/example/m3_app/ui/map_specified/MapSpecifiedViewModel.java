package com.example.m3_app.ui.map_specified;
import android.app.Application;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class MapSpecifiedViewModel extends AndroidViewModel {
    private static final class Group {
        List<String> ids;
        String image;
    }
    private static final class MultiGroup {
        List<Group> groups;
    }
    private final MutableLiveData<List<RouteConfig.Route>> allRoutes = new MutableLiveData<>();
    private final List<Group> groups;

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

        List<Group> tmp;
        try (InputStream is = app.getResources()
                .openRawResource(R.raw.multi_route_maps)) {

            byte[] buf = new byte[is.available()];
            is.read(buf);
            String json = new String(buf, StandardCharsets.UTF_8);

            MultiGroup mg = new Gson().fromJson(json, MultiGroup.class);
            tmp = mg != null ? mg.groups : Collections.emptyList();

        } catch (IOException e) {
            tmp = Collections.emptyList();
        }
        groups = tmp;
    }

    public @DrawableRes int mapFor(List<String> remaining) {
        List<String> ids = remaining.stream().sorted().toList();
        for (Group g : groups) {
            if (g.ids.equals(ids)) {
                return getApplication().getResources()
                        .getIdentifier(g.image, "drawable",
                                getApplication().getPackageName());
            }
        }

        return getApplication().getResources()
                .getIdentifier(remaining.get(0), "drawable",
                        getApplication().getPackageName());
    }
}

