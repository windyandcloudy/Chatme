package com.example.chatme.Home;

import com.example.chatme.Models.Post;

public interface IOnClickPost {
    void iOnClickCmt(Post post, int pos);
    void iOnClickLike(Post post, int pos);
}
