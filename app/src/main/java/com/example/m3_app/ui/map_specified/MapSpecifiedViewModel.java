package com.example.m3_app.ui.map_specified;
import android.app.Application;
import androidx.annotation.DrawableRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private static final @DrawableRes int PLACEHOLDER = R.drawable.large_empty_map_google;

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
        if (remaining == null || remaining.isEmpty()) {
            return PLACEHOLDER;
        }

        List<String> ids = new ArrayList<>(remaining);
        Collections.sort(ids);

        for (Group g : groups) {
            if (g.ids.equals(ids)) {
                return resId(g.image);
            }
        }

        if (ids.size() == 1 && allRoutes.getValue() != null) {
            return allRoutes.getValue().stream()
                    .filter(r -> r.id.equals(ids.get(0))).findFirst()
                    .map(r -> resId(r.imageResource)).orElse(PLACEHOLDER);
        }
        return PLACEHOLDER;
    }

    private int resId(String name) {
        return getApplication().getResources()
                .getIdentifier(name, "drawable",
                        getApplication().getPackageName());
    }
}

