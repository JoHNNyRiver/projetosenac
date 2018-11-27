package com.example.joao.facesenac.pi.activity.frames;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import com.example.joao.facesenac.pi.activity.activity.MainActivity;
import com.example.joao.facesenac.pi.activity.adapter.FeedAdapter;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
import com.example.joao.facesenac.pi.activity.model.PostFeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private EditText texto;
    private ProgressBar pprogressBarPost;
    private ProgressBar progressBarLoading;
    private Long idPosts;
    private FeedActivity activitie;
    private ViewGroup mensagens;

    public BlankFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();
        mensagens = view.findViewById(R.id.container);

        addCardPost();
        addLoading();
        callAddPost();

        return view;
    }

    public void callAddPost() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/posts/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = retrofit2.create(ApiUsers.class);
        Call<ArrayList<GetFeed>> callPosts = apiUsers.getPosts(idPosts);

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
        Button curtir = cardView.findViewById(R.id.curtir);
        Button comentar = cardView.findViewById(R.id.comentar);

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

    public  void addCardPost() {
        CardView cardView = (CardView) LayoutInflater.from(getContext())
                .inflate(R.layout.card_post, mensagens, false);

        ImageButton btnEnviar = cardView.findViewById(R.id.btnEnviar);
        pprogressBarPost = cardView.findViewById(R.id.progressBarPost);
        texto = cardView.findViewById(R.id.textoPostagem);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedActivity activity = (FeedActivity) getActivity();
                Long id = activity.returnId();

                pprogressBarPost.setVisibility(View.VISIBLE);
                texto.setVisibility(View.GONE);

                Retrofit postFeed = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiFeed = postFeed.create(ApiUsers.class);
                String textoValue = texto.getText().toString();
                String fotoValue = "";

                PostFeed postFeeder = new PostFeed();
                postFeeder.setTexto(textoValue);
                postFeeder.setId(id);
                postFeeder.setFoto(fotoValue);


                Call<Long> callFeed = apiFeed.postFeed(postFeeder);

                Callback<Long> callback = new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        Long resposta = response.body();

                        if (response.code() == 200) {
                            if (response.isSuccessful() && resposta != null) {
                                Toast.makeText(getContext(),
                                        "successfull",
                                        Toast.LENGTH_LONG).show();

                                texto.setText("");
                                texto.setVisibility(View.VISIBLE);
                                pprogressBarPost.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(),
                                        "Houve um erro, 1!",
                                        Toast.LENGTH_LONG).show();
                                texto.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getContext(),
                                    "Houve um erro, 2!",
                                    Toast.LENGTH_LONG).show();
                            texto.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        t.printStackTrace();

                        Toast.makeText(getContext(),
                                "Houve um erro,  3!",
                                Toast.LENGTH_LONG).show();
                        texto.setVisibility(View.VISIBLE);
                    }
                };

                callFeed.enqueue(callback);
            }
        });

        mensagens.addView(cardView);
    }

    public void addLoading() {
        CardView cardView = (CardView) LayoutInflater.from(getContext())
                .inflate(R.layout.loading, mensagens, false);

        progressBarLoading = cardView.findViewById(R.id.progressBarLoading);
        mensagens.addView(cardView);
    }

}
