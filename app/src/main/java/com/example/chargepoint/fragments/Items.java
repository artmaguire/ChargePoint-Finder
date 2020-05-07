package com.example.chargepoint.fragments;

public class Items {

    private int imageResources;
    private String title;
    private String desc;
    private boolean isShrink = true;

    public Items() {

    }

    public Items(int imageResources, String title, String desc) {
        this.imageResources = imageResources;
        this.title = title;
        this.desc = desc;

    }

    public int getImageResources() {
        return imageResources;
    }

    public void setImageResources(int imageResources) {
        this.imageResources = imageResources;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }
}
