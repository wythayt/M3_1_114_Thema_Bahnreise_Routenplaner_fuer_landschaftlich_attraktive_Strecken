package com.example.m3_app.backend.utility;

import com.example.m3_app.backend.RouteConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteFilterUtil {
    public static List<RouteConfig.Route> filterByChips(
            List<RouteConfig.Route> routes, Set<String> selectedChips, String inputFrom, String inputTo) {

        List<RouteConfig.Route> locationMatched = routes.stream()
                .filter(route -> route.fromDestination.equalsIgnoreCase(inputFrom)
                        && route.toDestination.equalsIgnoreCase(inputTo))
                .collect(Collectors.toList());

        if (selectedChips == null || selectedChips.isEmpty()) {
            return new ArrayList<>(routes);
        }

        return locationMatched.stream().filter(route -> {
                    RouteConfig.Filter f = route.filter;
                    return (selectedChips.contains("familyFun") && f.familyFun)
                            || (selectedChips.contains("historyHeritage") && f.historyHeritage)
                            || (selectedChips.contains("artCulture") && f.artCulture);
                }).collect(Collectors.toList());
    }
}
