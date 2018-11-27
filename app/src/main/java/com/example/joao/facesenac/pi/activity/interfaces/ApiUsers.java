package com.example.joao.facesenac.pi.activity.interfaces;

import com.example.joao.facesenac.pi.activity.model.FeedPerfil;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
import com.example.joao.facesenac.pi.activity.model.PostFeed;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;
import com.example.joao.facesenac.pi.activity.model.PostUserSignup;
import com.example.joao.facesenac.pi.activity.model.SigninBody;
import com.example.joao.facesenac.pi.activity.model.SignupBody;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiUsers {

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("login")
    Call<PostUserLogin> postLogin(@Body SigninBody user);

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("users")
    Call<Long> postSignup(@Body SignupBody info);

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("posts")
    Call<Long> postFeed(@Body PostFeed postFeeder);

    @GET("friends/{id}")
    Call<ArrayList<GetFeed>> getPosts(@Path("id") Long id);

    @GET("posts/{id}")
    Call<ArrayList<GetFeed>> getMyPosts(@Path("id") Long id);
}
