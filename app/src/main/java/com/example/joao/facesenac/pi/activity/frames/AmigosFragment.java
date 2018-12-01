package com.example.joao.facesenac.pi.activity.frames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.GetFriends;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmigosFragment extends Fragment {
    private Long idPosts;
    private FeedActivity activitie;

    public AmigosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();

        Retrofit usersApi = new Retrofit.Builder()
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = usersApi.create(ApiUsers.class);
        Call<ArrayList<GetFriends>> callPosts = apiUsers.getFriendsAPI(idPosts);

        Callback<ArrayList<GetFriends>> callback = new Callback<ArrayList<GetFriends>>() {
            @Override
            public void onResponse(Call<ArrayList<GetFriends>> call, Response<ArrayList<GetFriends>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<GetFriends>> call, Throwable t) {

            }
        };

        callPosts.enqueue(callback);

        return view;

    }

}
