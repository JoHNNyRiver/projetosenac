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
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.GetFriends;
import com.example.joao.facesenac.pi.activity.model.RetornoAmigos;
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
    private ProgressBar progressBarLoading;

    public AmigosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();
        ctx = getActivity();
        mensagem = view.findViewById(R.id.containerAmigos);

        addLoading();


        Retrofit usersApi = new Retrofit.Builder()
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = usersApi.create(ApiUsers.class);
        Call<ArrayList<GetFriends>> callPosts = apiUsers.getFriendsAPI(idPosts);

        Callback<ArrayList<GetFriends>> callback = new Callback<ArrayList<GetFriends>>() {
            @Override
            public void onResponse(Call<ArrayList<GetFriends>> call, Response<ArrayList<GetFriends>> response) {
                ArrayList<GetFriends> body = response.body();
                progressBarLoading.setVisibility(View.GONE);

                String status = "";
                if (response.isSuccessful() && body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        if (body.get(i).getAmizade() != null) {
                            status = body.get(i).getAmizade();
                        }

                        addCard(status,
                                body.get(i).getNome(),
                                body.get(i).getId());
                    }

                    if (body.size() < 1) {
                        view.findViewById(R.id.semAmigos).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetFriends>> call, Throwable t) {
                t.printStackTrace();
                progressBarLoading.setVisibility(View.GONE);
            }
        };

        callPosts.enqueue(callback);

        return view;

    }

    public void addCard (String statusAmizade, String nome, Long id) {
        final CardView cardView = (CardView) LayoutInflater.from(ctx)
                .inflate(R.layout.card_friends, mensagem, false);

        ImageView imageAmigosLayout = cardView.findViewById(R.id.imageAmigosLayout);
        TextView nomeAmigo = cardView.findViewById(R.id.nomeAmigo);
        TextView statusAmigo = cardView.findViewById(R.id.statusAmigo);
        final ImageView btnSolicita = cardView.findViewById(R.id.btnSolicita);
        final ImageButton btnDeletaAmigo = cardView.findViewById(R.id.btnDeletaAmigo);
        final ImageButton btnAceitar = cardView.findViewById(R.id.btnAceitar);
        EditText amizadeHiddenId = cardView.findViewById(R.id.amizadeHiddenId);
        final ProgressBar progressBarAmigos = cardView.findViewById(R.id.progressBarAmigos);

        nomeAmigo.setText(nome);
        statusAmigo.setText(statusAmizade);

        if (statusAmizade.equals("solicitante")) {
            statusAmigo.setText("Aceitar amizade?");
        }

        idHidden = id;

        btnSolicita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiUsers = usersApi.create(ApiUsers.class);
                Call<RetornoAmigos> call = apiUsers.putFriends(idPosts, idHidden);

                progressBarAmigos.setVisibility(View.VISIBLE);

                Callback<RetornoAmigos> callback = new Callback<RetornoAmigos>() {
                    @Override
                    public void onResponse(Call<RetornoAmigos> call, Response<RetornoAmigos> response) {
                        RetornoAmigos body = response.body();
                        progressBarAmigos.setVisibility(View.GONE);

                        if (response.isSuccessful() && body != null) {
                            btnSolicita.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetornoAmigos> call, Throwable t) {
                        t.printStackTrace();
                        progressBarAmigos.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                    }
                };

                call.enqueue(callback);
            }
        });

        btnDeletaAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiUsers = usersApi.create(ApiUsers.class);
                Call<RetornoAmigos> call = apiUsers.deletAmizade(idPosts, idHidden);

                progressBarAmigos.setVisibility(View.VISIBLE);

                Callback<RetornoAmigos> callback = new Callback<RetornoAmigos>() {
                    @Override
                    public void onResponse(Call<RetornoAmigos> call, Response<RetornoAmigos> response) {
                        RetornoAmigos body = response.body();
                        progressBarAmigos.setVisibility(View.GONE);

                        if (response.isSuccessful() && body != null) {
                            cardView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetornoAmigos> call, Throwable t) {
                        t.printStackTrace();
                        progressBarAmigos.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                    }
                };

                call.enqueue(callback);
            }
        });

        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiUsers = usersApi.create(ApiUsers.class);
                Call<RetornoAmigos> call = apiUsers.postFriends(idPosts, idHidden);

                progressBarAmigos.setVisibility(View.VISIBLE);

                Callback<RetornoAmigos> callback = new Callback<RetornoAmigos>() {
                    @Override
                    public void onResponse(Call<RetornoAmigos> call, Response<RetornoAmigos> response) {
                        RetornoAmigos body = response.body();
                        progressBarAmigos.setVisibility(View.GONE);

                        if (response.isSuccessful() && body != null) {
                            btnAceitar.setVisibility(View.GONE);
                            btnDeletaAmigo.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetornoAmigos> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
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
            btnAceitar.setVisibility(View.INVISIBLE);
        }

        if (statusAmizade.equals("amigos")) {
            btnSolicita.setVisibility(View.GONE);
        }

        if (statusAmizade.equals("solicitado")) {
            btnAceitar.setVisibility(View.GONE);
        }

        if (statusAmizade.equals("")) {
            btnAceitar.setVisibility(View.GONE);
            btnDeletaAmigo.setVisibility(View.GONE);
            statusAmigo.setText("Adicione " + nome + " como amigo");
        }

        mensagem.addView(cardView);
    }

    public void addLoading() {
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.loading, mensagem, false);

        progressBarLoading = cardView.findViewById(R.id.progressBarLoading);
        mensagem.addView(cardView);
    }

}
