package com.example.m3_app.ui.route_img;

public class RouteImgCard {
    public String title;
    public int imageId;
    public String category;
    boolean favourite;

    public RouteImgCard(String title, int imageId, String category, boolean favourite) {
        this.title = title;
        this.imageId = imageId;
        this.category = category;
        this.favourite = favourite;
    }
}

