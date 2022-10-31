package com.example.chatme.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("img")
    @Expose
    private String img;

    @SerializedName("like")
    @Expose
    private int like;

    @SerializedName("id_acc")
    @Expose
    private Account id_acc;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Post(String _id, String content, String img, int like, Account id_acc, String createdAt) {
        this._id = _id;
        this.content = content;
        this.img = img;
        this.like = like;
        this.id_acc = id_acc;
        this.createdAt = createdAt;
    }

    public Post() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Account getId_acc() {
        return id_acc;
    }

    public void setId_acc(Account id_acc) {
        this.id_acc = id_acc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
