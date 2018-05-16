package com.example.jxr.gameeandroid;

public class Post {
    private String condition;
    private String date;
    private String pic; // to store pic url
    private String price;
    private String region;
    private String system;
    private String title;
    private String user;

    public Post(String condition, String date, String pic, String price, String region, String system, String title, String user) {
        this.condition = condition;
        this.date = date;
        this.pic = pic;
        this.price = price;
        this.region = region;
        this.system = system;
        this.title = title;
        this.user = user;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
