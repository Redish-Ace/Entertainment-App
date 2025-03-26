package com.example.individualstudy;

public class Movie implements MediaItem {
    private String title;
    private String imageUrl;

    public Movie(String title, String imageUrl) {
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

