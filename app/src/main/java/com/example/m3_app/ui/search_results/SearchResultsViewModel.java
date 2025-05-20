package com.example.m3_app.ui.search_results;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.m3_app.backend.RouteConfig;

import java.util.List;

public class SearchResultsViewModel extends ViewModel {
    private final MutableLiveData<List<RouteConfig.Route>> filteredRoutes = new MutableLiveData<>();
    public LiveData<List<RouteConfig.Route>> getFilteredRoutes() {
        return filteredRoutes;
    }
    public void setFilteredRoutes(List<RouteConfig.Route> routes) {
        filteredRoutes.setValue(routes);
    }
}
