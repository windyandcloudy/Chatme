package com.example.chatme.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("id_post")
    @Expose
    private String id_post;

    @SerializedName("id_acc")
    @Expose
    private Account id_acc;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Comment(String _id, String content, String id_post, Account id_acc, String createdAt) {
        this._id = _id;
        this.content = content;
        this.id_post = id_post;
        this.id_acc = id_acc;
        this.createdAt = createdAt;
    }

    public Comment() {
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

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
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
