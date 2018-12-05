package com.example.joao.facesenac.pi.activity.frames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
public class BuscarAmigosFragment extends Fragment {
    private TextView txtBusca;
    private Button sendBusca;
    private FeedActivity activitie;
    private Long idUser;

    public BuscarAmigosFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_amigos, container, false);

        txtBusca = view.findViewById(R.id.txtBusca);
        sendBusca = view.findViewById(R.id.sendBusca);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                activitie = (FeedActivity) getActivity();
                idUser = activitie.returnId();
                String query = txtBusca.getText().toString();


                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiUsers = usersApi.create(ApiUsers.class);

                Call<ArrayList<GetFriends>> callFriends = apiUsers.getUsersAPI(idUser, query);
                Callback<ArrayList<GetFriends>> callback = new Callback<ArrayList<GetFriends>>() {
                    @Override
                    public void onResponse(Call<ArrayList<GetFriends>> call, Response<ArrayList<GetFriends>> response) {

                    }

                    @Override
                    public void onFailure(Call<ArrayList<GetFriends>> call, Throwable t) {

                    }
                };

                callFriends.enqueue(callback);
            }
        };

        sendBusca.setOnClickListener(listener);



        return view;
    }

}
