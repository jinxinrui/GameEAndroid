package com.example.jxr.gameeandroid;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String condition;
    private String date;
    private String description;
    private String pic; // to store pic url
    private String price;
    private String region;
    private String system;
    private String title;
    private String user;
    private String username;

    public Post(String condition, String date, String description, String pic, String price, String region, String system, String title, String user, String username) {
        this.condition = condition;
        this.date = date;
        this.description = description;
        this.pic = pic;
        this.price = price;
        this.region = region;
        this.system = system;
        this.title = title;
        this.user = user;
        this.username = username;
    }

    protected Post(Parcel in) {
        condition = in.readString();
        date = in.readString();
        description = in.readString();
        pic = in.readString();
        price = in.readString();
        region = in.readString();
        system = in.readString();
        title = in.readString();
        user = in.readString();
        username = in.readString();
    }

    public Post() {
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(condition);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(pic);
        dest.writeString(price);
        dest.writeString(region);
        dest.writeString(system);
        dest.writeString(title);
        dest.writeString(user);
        dest.writeString(username);
    }
}