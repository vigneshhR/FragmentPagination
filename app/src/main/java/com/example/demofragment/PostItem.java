package com.example.demofragment;

public class PostItem {

    private String description;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String imageURL;

    private String title;

    public String getImageURL() {
        return imageURL;
    }

    public String getType() {
        return type;
    }

    private String type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
