package com.example.individualstudy;

public class Book implements MediaItem {
    private String title;
    private String imageUrl;

    public Book(String title, String imageUrl) {
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

