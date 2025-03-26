package com.example.individualstudy;

public class Anime implements MediaItem {
    private String title;
    private String imageUrl;

    public Anime(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}

