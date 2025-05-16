package com.example.m3_app.backend;

import java.util.List;

public class RouteConfig {
    public List<Route> routes;

    public static class Route {
        public String imageResource;
        public String fromDestination;
        public String toDestination;

        public Filter filter;
    }

    public static class Filter {
        public boolean familyFun;
        public boolean historyHeritage;
        public boolean artCulture;
    }
}

