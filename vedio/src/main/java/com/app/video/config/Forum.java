package com.app.video.config;

public class Forum {
    private String name;
    private String image;
    private String describe;
    private int sends;
    private int accept;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getSends() {
        return sends;
    }

    public void setSends(int sends) {
        this.sends = sends;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }
}
