package com.example.joao.facesenac.pi.activity.frames;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.GetFriends;
import com.example.joao.facesenac.pi.activity.model.PutAmigos;
import com.example.joao.facesenac.pi.activity.model.getDeleteAmigo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
    private Long idHidden;
    private FeedActivity activitie;
    private ViewGroup mensagem;
    private Activity ctx;

    public AmigosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();
        ctx = getActivity();
        mensagem = view.findViewById(R.id.containerAmigos);

        Retrofit usersApi = new Retrofit.Builder()
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = usersApi.create(ApiUsers.class);
        Call<ArrayList<GetFriends>> callPosts = apiUsers.getFriendsAPI(idPosts);

        Callback<ArrayList<GetFriends>> callback = new Callback<ArrayList<GetFriends>>() {
            @Override
            public void onResponse(Call<ArrayList<GetFriends>> call, Response<ArrayList<GetFriends>> response) {
                ArrayList<GetFriends> body = response.body();

                if (response.isSuccessful() && body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        addCard(body.get(i).getStatusAmizade(),
                                body.get(i).getNome(),
                                body.get(i).getId(),
                                body.get(i).getAprovado());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetFriends>> call, Throwable t) {

            }
        };

        callPosts.enqueue(callback);

        return view;

    }

    public void addCard (String statusAmizade, String nome, Long id, Boolean aprovado) {
        final CardView cardView = (CardView) LayoutInflater.from(ctx)
                .inflate(R.layout.card_friends, mensagem, false);

        ImageView imageAmigosLayout = cardView.findViewById(R.id.imageAmigosLayout);
        TextView nomeAmigo = cardView.findViewById(R.id.nomeAmigo);
        TextView statusAmigo = cardView.findViewById(R.id.statusAmigo);
        final ImageButton buttonAmigosAdicionaLayout = cardView.findViewById(R.id.buttonAmigosAdicionaLayout);
        ImageButton buttonAmigosDeletaLayout = cardView.findViewById(R.id.buttonAmigosDeletaLayout);
        EditText amizadeHiddenId = cardView.findViewById(R.id.amizadeHiddenId);
        final ProgressBar progressBarAmigos = cardView.findViewById(R.id.progressBarAmigos);

        nomeAmigo.setText(nome);
        statusAmigo.setText(statusAmizade);
        idHidden = id;

        buttonAmigosAdicionaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiUsers = usersApi.create(ApiUsers.class);
//                PutAmigos putAmigos = new PutAmigos();
//                putAmigos.setUsuario1(idPosts);
//                putAmigos.setUsuario2(idHidden);
                Call<getDeleteAmigo> call = apiUsers.putFriends(idPosts, idHidden);

                progressBarAmigos.setVisibility(View.VISIBLE);

                Callback<getDeleteAmigo> callback = new Callback<getDeleteAmigo>() {
                    @Override
                    public void onResponse(Call<getDeleteAmigo> call, Response<getDeleteAmigo> response) {
                        getDeleteAmigo body = response.body();
                        progressBarAmigos.setVisibility(View.GONE);

                        if (response.isSuccessful() && body != null) {
                            buttonAmigosAdicionaLayout.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<getDeleteAmigo> call, Throwable t) {
                        t.printStackTrace();
                        progressBarAmigos.setVisibility(View.GONE);
                    }
                };

                call.enqueue(callback);
            }
        });

        buttonAmigosDeletaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiUsers = usersApi.create(ApiUsers.class);
                Call<getDeleteAmigo> call = apiUsers.deletAmizade(idPosts, idHidden);

                progressBarAmigos.setVisibility(View.VISIBLE);

                Callback<getDeleteAmigo> callback = new Callback<getDeleteAmigo>() {
                    @Override
                    public void onResponse(Call<getDeleteAmigo> call, Response<getDeleteAmigo> response) {
                        getDeleteAmigo body = response.body();
                        progressBarAmigos.setVisibility(View.GONE);

                        if (response.isSuccessful() && body != null) {
                            cardView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<getDeleteAmigo> call, Throwable t) {
                        t.printStackTrace();
                        progressBarAmigos.setVisibility(View.GONE);
                    }
                };

                call.enqueue(callback);
            }
        });

        String urlPost = "https://pi4facenac.azurewebsites.net/PI4/api/users/image/" + id;
        ImageLoader imageLoaderPost = ImageLoader.getInstance();
        imageLoaderPost.init(ImageLoaderConfiguration.createDefault(ctx));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_icon)
                .showImageForEmptyUri(R.drawable.nouser)
                .showImageOnFail(R.drawable.nouser)
                .cacheInMemory(true)
                .build();

        imageLoaderPost.displayImage(urlPost, imageAmigosLayout, options);

        if (statusAmizade.equals("solicitante") || statusAmizade.equals("amigos")) {
            buttonAmigosAdicionaLayout.setVisibility(View.INVISIBLE);
        }

        mensagem.addView(cardView);
    }

}
