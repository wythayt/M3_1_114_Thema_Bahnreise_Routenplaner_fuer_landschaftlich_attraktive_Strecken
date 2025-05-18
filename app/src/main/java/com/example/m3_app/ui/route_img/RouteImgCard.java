package com.example.m3_app.ui.route_img;

public class RouteImgCard {
    public String id;
    public String title;
    public int imageId;
    public String category;
    boolean favourite;

    public RouteImgCard(String id, String title, int imageId, String category, boolean favourite) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.category = category;
        this.favourite = favourite;
    }

    public String getId() {
        return id;
    }
}

