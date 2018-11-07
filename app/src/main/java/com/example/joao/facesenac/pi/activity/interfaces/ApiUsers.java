package com.example.joao.facesenac.pi.activity.interfaces;

import com.example.joao.facesenac.pi.activity.model.PostUserLogin;
import com.example.joao.facesenac.pi.activity.model.PostUserSignup;
import com.example.joao.facesenac.pi.activity.model.SignupBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiUsers {

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("/login")
    Call<PostUserLogin> postLogin(@Body String user);

    @Headers({
        "Accept: application/json;charset=utf-8",
        "Content-Type: application/json;charset=utf-8"
    })
    @POST("users")
    Call<Long> postSignup(@Body SignupBody info);
}
