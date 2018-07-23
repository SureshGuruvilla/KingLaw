package com.example.admin.kinglaw;

/**
 * Created by ADMIN on 1/21/2018.
 */

public class FirebaseHelper {
    private String image;
    private String desc;
    private String name;
    private String dp;
    private String u_id;
    private String time;
    private String video;
    private String thumbnail;
    public FirebaseHelper(){

    }

    public FirebaseHelper(String image, String desc, String name, String dp, String u_id, String time, String video, String thumbnail) {
        this.image = image;
        this.desc = desc;
        this.name = name;
        this.dp = dp;
        this.u_id = u_id;
        this.time = time;
        this.video = video;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}