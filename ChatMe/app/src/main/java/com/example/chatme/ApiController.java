package com.example.chatme;

import com.example.chatme.Models.Account;
import com.example.chatme.Models.Comment;
import com.example.chatme.Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiController {
    String DOMAIN = Constant.BASE_URL+"/api/";
    Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create();

    ApiController apiService = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiController.class);

    @FormUrlEncoded
    @POST("accounts")
    Call<Account> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("accounts/register")
    Call<Account> register(@Field("username") String username, @Field("password") String password, @Field("fullname") String fullname);

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("comments/{id}")
    Call<List<Comment>> getComments(@Path("id") String id);

    @FormUrlEncoded
    @POST("comments/{id}")
    Call<Comment> createCmt(@Path("id") String idPost, @Field("content") String content, @Field("id_acc") String id_acc);

    @Multipart
    @POST("posts")
    Call<Post> createPost(@Part("content") RequestBody content, @Part MultipartBody.Part img, @Part("id_acc") RequestBody id_acc);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPostNoImg(@Field("content") String content, @Field("id_acc") String id_acc);

    @Multipart
    @PATCH("accounts/{id}")
    Call<Account> updateAccountAvt(@Path("id") String id, @Part MultipartBody.Part avt);

    @FormUrlEncoded
    @PATCH("accounts/{id}")
    Call<Account> updateAccountInfor(@Path("id") String id, @Field("fullname") String fullname, @Field("phone") String phone);

    @FormUrlEncoded
    @PATCH("accounts/{id}")
    Call<Account> updateAccountPassword(@Path("id") String id, @Field("password") String password);


}
