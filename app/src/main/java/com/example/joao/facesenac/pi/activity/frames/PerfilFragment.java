package com.example.joao.facesenac.pi.activity.frames;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.adapter.FeedAdapter;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
import com.example.joao.facesenac.pi.activity.model.PostFeed;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilFragment extends Fragment {
    private EditText texto;
    private ProgressBar pprogressBarPost;
    private ProgressBar progressBarLoading;
    private Long idPosts;
    private FeedActivity activitie;
    private ViewGroup mensagens;
    private String textnome, textEmail;

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();
        mensagens = view.findViewById(R.id.containerperfil);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            textnome = bundle.getString("nome");
            textEmail = bundle.getString("email");
        }

        addInfoCard();
        addLoading();
        addDinamyccard();

        return view;
    }

    public void addDinamyccard() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = retrofit2.create(ApiUsers.class);
        Call<ArrayList<GetFeed>> callPosts = apiUsers.getMyPosts(idPosts);

        Callback<ArrayList<GetFeed>> callbackPosts = new Callback<ArrayList<GetFeed>>() {
            @Override
            public void onResponse(Call<ArrayList<GetFeed>> call, Response<ArrayList<GetFeed>> response) {
                ArrayList<GetFeed> myposts = response.body();
                if (response.code() == 200) {
                    myposts.size();

                    if (response.isSuccessful()) {
                        for (int i = 0; i < myposts.size(); i++) {
                            addCard(myposts.get(i).getNomeUser(),
                                    myposts.get(i).getData(),
                                    myposts.get(i).getTexto(),
                                    myposts.get(i).getNumCurtidas());
                        }

                        progressBarLoading.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetFeed>> call, Throwable t) {
                t.printStackTrace();
            }
        };

        callPosts.enqueue(callbackPosts);
    }

    public void addCard(String nome, String data, String desc, Integer curtidas) {
        CardView cardView = (CardView) LayoutInflater.from(getContext())
                .inflate(R.layout.card_feed, mensagens, false);

        TextView nomeFeed = cardView.findViewById(R.id.nomeFeed);
        TextView dataFeed = cardView.findViewById(R.id.dataFeed);
        TextView descFeed = cardView.findViewById(R.id.descFeed);
        TextView curtidaFeed = cardView.findViewById(R.id.curtidaFeed);
        ImageView imageFeedDesc = cardView.findViewById(R.id.imageFeedDesc);
        ImageView imagemFeed = cardView.findViewById(R.id.imagemFeed);

        String curtidaTexto = Integer.toString(curtidas);

        if (curtidaTexto.equals("0")) {
            curtidaTexto = "seja o primeiro a curtir";
        } else {
            curtidaTexto = curtidaTexto + "like(s)";
        }

        String[] partialData = data.split("-");
        String modifiedData = partialData[2] + "/" + partialData[1] + "/" + partialData[0];

        imageFeedDesc.setVisibility(View.GONE);
        nomeFeed.setText(nome);
        descFeed.setText(desc);
        curtidaFeed.setText(curtidaTexto);
        dataFeed.setText(modifiedData);


        mensagens.addView(cardView);
    }

    public void addInfoCard() {
        CardView cardView = (CardView) LayoutInflater.from(getContext())
                .inflate(R.layout.card_profile, mensagens, false);

        TextView nomePerfil = cardView.findViewById(R.id.nomePerfil);
        TextView emailPerfil = cardView.findViewById(R.id.emailPerfil);

        nomePerfil.setText(textnome);
        emailPerfil.setText(textEmail);

        mensagens.addView(cardView);
    }

    public void addLoading() {
        CardView cardView = (CardView) LayoutInflater.from(getContext())
                .inflate(R.layout.loading, mensagens, false);

        progressBarLoading = cardView.findViewById(R.id.progressBarLoading);
        mensagens.addView(cardView);
    }
}
