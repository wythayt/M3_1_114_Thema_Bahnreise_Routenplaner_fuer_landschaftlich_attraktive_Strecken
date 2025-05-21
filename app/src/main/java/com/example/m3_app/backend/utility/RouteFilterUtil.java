package com.example.m3_app.backend.utility;

import com.example.m3_app.backend.RouteConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteFilterUtil {
    public static List<RouteConfig.Route> filterByChips(
            List<RouteConfig.Route> routes, Set<String> selectedChips, String inputFrom, String inputTo) {

        List<RouteConfig.Route> locationMatched = routes.stream()
                .filter(route ->
                        route.fromDestination.equalsIgnoreCase(inputFrom) &&
                        route.toDestination.equalsIgnoreCase(inputTo))
                .collect(Collectors.toList());

        if (selectedChips == null || selectedChips.isEmpty()) {
            return new ArrayList<>(locationMatched);
        }

        return locationMatched.stream()
                .filter(route -> {
                    RouteConfig.Filter f = route.filter;
                    return selectedChips.stream().anyMatch(tag -> {
                        try {
                            Field fld = f.getClass().getDeclaredField(tag);
                            fld.setAccessible(true);
                            return fld.getBoolean(f);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            return false;
                        }
                    });
                })
                .collect(Collectors.toList());
    }

    public static List<RouteConfig.Route> filterByChips(
            List<RouteConfig.Route> routes,
            Set<String> selectedChips,
            String inputFrom) {

        List<RouteConfig.Route> fromMatched = routes.stream()
                .filter(route -> route.fromDestination.equalsIgnoreCase(inputFrom))
                .collect(Collectors.toList());

        if (selectedChips == null || selectedChips.isEmpty()) {
            return new ArrayList<>(fromMatched);
        }

        return fromMatched.stream()
                .filter(route -> {
                    RouteConfig.Filter f = route.filter;
                    return selectedChips.stream().anyMatch(tag -> {
                        try {
                            Field fld = f.getClass().getDeclaredField(tag);
                            fld.setAccessible(true);
                            return fld.getBoolean(f);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            return false;
                        }
                    });
                })
                .collect(Collectors.toList());
    }

}
