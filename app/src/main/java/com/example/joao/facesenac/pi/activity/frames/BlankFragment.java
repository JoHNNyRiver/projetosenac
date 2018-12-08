package com.example.joao.facesenac.pi.activity.frames;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.activity.PostImageActivity;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.Curtidas;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
import com.example.joao.facesenac.pi.activity.model.PostFeed;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private TextView idUser;
    private TextView idHistorico;
    private TextView nomeFeed;
    private TextView dataFeed;
    private TextView descFeed;
    private ImageView imageFeedDesc;
    private ImageView imagemFeed;
    private Button comentar;
    private Activity ctx;

    public BlankFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        activitie = (FeedActivity) getActivity();
        idPosts = activitie.returnId();
        mensagens = view.findViewById(R.id.container);
        ctx = getActivity();

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
                String nomeUser = "Sem nome";
                String dataUser = "Sem data";
                String dataTexto = "Sem texto";
                int dataNumCurtidas = 0;
                Long dataUsuario = Long.valueOf(0);
                String fotoUser = "";
                Long id = Long.valueOf(0);
                Boolean liked = false;


                if (response.code() == 200) {
                    myposts.size();

                    if (response.isSuccessful()) {
                        for (int i = 0; i < myposts.size(); i++) {
                            if (myposts.get(i).getNomeUser() != null) {
                                nomeUser = myposts.get(i).getNomeUser();
                            }
                            if (myposts.get(i).getData() != null){
                                dataUser = myposts.get(i).getData();
                            }
                            if (myposts.get(i).getTexto() != null){
                                dataTexto = myposts.get(i).getTexto();
                            }
                            if (myposts.get(i).getNumCurtidas() != null){
                                dataNumCurtidas = myposts.get(i).getNumCurtidas();
                            }
                            if (myposts.get(i).getUsuario() != null){
                                dataUsuario = myposts.get(i).getUsuario();
                            }
                            if (myposts.get(i).getFotoUser() != null){
                                fotoUser = myposts.get(i).getFotoUser();
                            }

                            if (myposts.get(i).getId() != null){
                                id = myposts.get(i).getId();
                            }

                            if (myposts.get(i).getLiked() != null){
                                liked = myposts.get(i).getLiked();
                            }
                            addCard(nomeUser, dataUser, dataTexto, dataNumCurtidas, dataUsuario,
                                    fotoUser, id, liked, myposts.get(i).getTemFoto());
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

    public void addCard(String nome, String data, String desc, Integer curtidas, Long usuario, String fotoUser, Long id, Boolean liked, Integer temFoto) {
        if (!isAdded()) {
            return;
        }
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.card_feed, mensagens, false);

        nomeFeed = cardView.findViewById(R.id.nomeFeed);
        dataFeed = cardView.findViewById(R.id.dataFeed);
        descFeed = cardView.findViewById(R.id.descFeed);
        imageFeedDesc = cardView.findViewById(R.id.imageFeedDesc);
        imagemFeed = cardView.findViewById(R.id.imagemFeed);
        comentar = cardView.findViewById(R.id.comentar);
        idUser = cardView.findViewById(R.id.idUser);
        idHistorico = cardView.findViewById(R.id.idHistorico);

        final TextView curtidaFeed = cardView.findViewById(R.id.curtidaFeed);
        final Button curtir = cardView.findViewById(R.id.curtir);

        idUser.setText(String.valueOf(usuario));
        idHistorico.setText(String.valueOf(id));

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment = new Intent(getActivity(), FeedActivity.CommentActivity.class);
                startActivity(comment);
            }
        });

        if (liked) {
            curtir.setTextColor(getResources().getColor(R.color.bgbutton));
        }


        curtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/curtida/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                curtir.setText("Curtindo ...");
                curtir.setEnabled(false);

                Long idusr = Long.valueOf(idUser.getText().toString());
                Long idHist = Long.valueOf(idHistorico.getText().toString());

                ApiUsers apiUsers = retrofit2.create(ApiUsers.class);
                Call<Curtidas> comments = apiUsers.postComment(idusr, idHist);

                Callback<Curtidas> callback = new Callback<Curtidas>() {
                    @Override
                    public void onResponse(Call<Curtidas> call, Response<Curtidas> response) {
                        Curtidas body = response.body();
                        curtir.setEnabled(true);
                        curtir.setText("Curtir");

                        if (response.isSuccessful() && body != null) {
                            Long totalCurtida = body.getTotalCurtidas();
                            String numCurtidas = totalCurtida < 2 ? totalCurtida.toString()  + " like" : totalCurtida + "likes";
                            numCurtidas = totalCurtida == 0 ? "seja o primeiro a curtir" : numCurtidas;

                            curtidaFeed.setText(numCurtidas);

                            if (body.getStatus()) {
                                curtir.setTextColor(getResources().getColor(R.color.bgbutton));
                                Toast.makeText(getActivity(), "Curtido", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Descurtido", Toast.LENGTH_LONG).show();
                                curtir.setTextColor(getResources().getColor(R.color.facesenac));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Curtidas> call, Throwable t) {
                        t.printStackTrace();
                        curtir.setEnabled(true);
                    }
                };

                comments.enqueue(callback);
            }
        });

        String curtidaTexto = Integer.toString(curtidas);

        String url = "https://pi4facenac.azurewebsites.net/PI4/api/users/image/" + usuario;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        DisplayImageOptions optionsOne = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_icon)
                .showImageForEmptyUri(R.drawable.nouser)
                .showImageOnFail(R.drawable.nouser)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();

        imageLoader.displayImage(url, imagemFeed, optionsOne);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_icon)
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .cacheInMemory(true)
                .build();

        String urlPost = "https://pi4facenac.azurewebsites.net/PI4/api/posts/image/" + id;
        ImageLoader imageLoaderPost = ImageLoader.getInstance();
        imageLoaderPost.init(ImageLoaderConfiguration.createDefault(getActivity()));

        if (temFoto == 1) {
            imageLoaderPost.displayImage(urlPost, imageFeedDesc, options);
            imageFeedDesc.setVisibility(View.VISIBLE);
        } else {
            imageFeedDesc.setVisibility(View.GONE);
        }

        if (curtidaTexto.equals("0")) {
            curtidaTexto = "seja o primeiro a curtir";
        } else {
            curtidaTexto = curtidaTexto.equals("1") ? curtidaTexto + " like" : curtidaTexto + " likes";
        }

        String[] partialData = data.split("-");
        String modifiedData = partialData[2] + "/" + partialData[1] + "/" + partialData[0];

        nomeFeed.setText(nome);
        descFeed.setText(desc);
        curtidaFeed.setText(curtidaTexto);
        dataFeed.setText(modifiedData);


        mensagens.addView(cardView);
    }

    public  void addCardPost() {
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.card_post, mensagens, false);

        ImageButton btnEnviar = cardView.findViewById(R.id.btnEnviar);
        ImageButton imageButton = cardView.findViewById(R.id.imageButton);
        pprogressBarPost = cardView.findViewById(R.id.progressBarPost);
        texto = cardView.findViewById(R.id.textoPostagem);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedActivity activity = (FeedActivity) getActivity();
                Long id = activity.returnId();

                closeKeyboard();

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
                postFeeder.setUsuario(id);
                postFeeder.setFoto(fotoValue);


                Call<Long> callFeed = apiFeed.postFeed(postFeeder);

                Callback<Long> callback = new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        Long resposta = response.body();

                        if (response.code() == 200) {
                            if (response.isSuccessful() && resposta != null) {
                                Toast.makeText(getActivity(),
                                        "Postado com sucesso",
                                        Toast.LENGTH_LONG).show();

                                texto.setText("");
                                texto.setVisibility(View.VISIBLE);
                                pprogressBarPost.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getActivity(),
                                        "Houve um erro, 1!",
                                        Toast.LENGTH_LONG).show();
                                pprogressBarPost.setVisibility(View.GONE);
                                texto.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    "Houve um erro, 2!",
                                    Toast.LENGTH_LONG).show();
                            pprogressBarPost.setVisibility(View.GONE);
                            texto.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        t.printStackTrace();

                        Toast.makeText(getActivity(),
                                "Houve um erro,  tente novamente!",
                                Toast.LENGTH_LONG).show();
                        texto.setVisibility(View.VISIBLE);
                    }
                };

                callFeed.enqueue(callback);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageChoice = new Intent(getActivity(), PostImageActivity.class);
                startActivity(imageChoice);
            }
        });

        mensagens.addView(cardView);
    }

    public void addLoading() {
        CardView cardView = (CardView) LayoutInflater.from(getActivity())
                .inflate(R.layout.loading, mensagens, false);

        progressBarLoading = cardView.findViewById(R.id.progressBarLoading);
        mensagens.addView(cardView);
    }

    private void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}
