package com.example.m3_app.ui.route_img;

public class RouteImgCard {
    public String id;
    public String title;
    public int imageId;
    public String category;

    public RouteImgCard(String id, String title, int imageId, String category) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.category = category;
    }

    public String getId() {
        return id;
    }
}

