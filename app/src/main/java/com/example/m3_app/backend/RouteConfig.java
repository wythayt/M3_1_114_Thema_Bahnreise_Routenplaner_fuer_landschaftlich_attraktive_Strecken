package com.example.m3_app.backend;

import java.util.List;

public class RouteConfig {
    public List<Route> routes;

    public static class Route {
        public String id;
        public String imageResource;
        public String cardImageResource;
        public String title; //fragment_details/titleText(TextView) + routeName(TextView)
        // + in item_route_img in textTitle(TextView) + in item_route_card  routeTitle(TextView)
        public String fromDestination; //fragment_search_results/from
        public String toDestination; //fragment_search_results/to
        public List<List<String>> stations; //fragment_details/stations(TextView) + also in route_segment
        public String transferTime; //route_connection time???
        public String journeyTime; //fragment_details/journeyTime(TextView)
        public String cost;//fragment_details/cost(TextView)
        public String distance; //fragment_details/distance(TextView)

        public String rating; //fragment_details/rating
        public String mainCategory; //item_route_card/textViewType(TextView)
        //+fragment_details/mainCategory
        public List<String> transportOperator;//fragment_details/operators
        public String description; //fragment_details/description
        public List<String> imageGallery; //added images
        public Filter filter;
    }

    public static class Filter {
        public boolean familyFun;
        public boolean historyHeritage;
        public boolean artCulture;
        public boolean noTransfer;
        public boolean audioGuide;
        public boolean withSights;
        public boolean topRated;
        public boolean barrierFree;
        public boolean alongTheRiver;
        public boolean forest;
        public boolean mountains;
    }
}

