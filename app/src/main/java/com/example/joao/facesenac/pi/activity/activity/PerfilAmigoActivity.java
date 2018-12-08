package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.Curtidas;
import com.example.joao.facesenac.pi.activity.model.GetFeed;
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

public class PerfilAmigoActivity extends AppCompatActivity {
    private ImageView imagemAmigoPerfil;
    private TextView txtNomeDoAmigo;
    private Long idInterno;
    private ViewGroup mensagensTop;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        imagemAmigoPerfil = findViewById(R.id.imagemAmigoPerfilFuck);
        txtNomeDoAmigo = findViewById(R.id.txtNomeDoAmigo);
        mensagensTop = findViewById(R.id.containerAmigoPerfil);
        progress = findViewById(R.id.progress);

        Bundle bdl = getIntent().getExtras();

        progress.setVisibility(View.VISIBLE);

        txtNomeDoAmigo.setText(bdl.getString("nomePerfil"));
        idInterno = bdl.getLong("idPerfil");

        String urlPost = "https://pi4facenac.azurewebsites.net/PI4/api/users/image/" + idInterno;
        ImageLoader imageLoaderPost = ImageLoader.getInstance();
        imageLoaderPost.init(ImageLoaderConfiguration.createDefault(PerfilAmigoActivity.this));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_icon)
                .showImageForEmptyUri(R.drawable.nouser)
                .showImageOnFail(R.drawable.nouser)
                .cacheInMemory(true)
                .build();

        imageLoaderPost.displayImage(urlPost, imagemAmigoPerfil, options);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .cache(null)
                .build();

        Retrofit retrofit3 = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = retrofit3.create(ApiUsers.class);
        Call<ArrayList<GetFeed>> call = apiUsers.getMyPosts(idInterno);

        Callback<ArrayList<GetFeed>> callback = new Callback<ArrayList<GetFeed>>() {
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

                        progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetFeed>> call, Throwable t) {
                t.printStackTrace();
                progress.setVisibility(View.GONE);
            }
        };

        call.enqueue(callback);

    }

    public void addCard(String nome, String data, String desc, Integer curtidas, Long usuario, String fotoUser, Long id, Boolean liked, Integer temFoto) {
        CardView cardView = (CardView) LayoutInflater.from(PerfilAmigoActivity.this)
                .inflate(R.layout.card_feed, mensagensTop, false);

        final TextView nomeFeed = cardView.findViewById(R.id.nomeFeed);
        final TextView dataFeed = cardView.findViewById(R.id.dataFeed);
        final TextView descFeed = cardView.findViewById(R.id.descFeed);
        final ImageView imageFeedDesc = cardView.findViewById(R.id.imageFeedDesc);
        final ImageView imagemFeed = cardView.findViewById(R.id.imagemFeed);
        final Button comentar = cardView.findViewById(R.id.comentar);
        final TextView idUser = cardView.findViewById(R.id.idUser);
        final TextView idHistorico = cardView.findViewById(R.id.idHistorico);

        final TextView curtidaFeed = cardView.findViewById(R.id.curtidaFeed);
        final Button curtir = cardView.findViewById(R.id.curtir);

        idUser.setText(String.valueOf(usuario));
        idHistorico.setText(String.valueOf(id));

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment = new Intent(PerfilAmigoActivity.this, FeedActivity.CommentActivity.class);
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
                                Toast.makeText(PerfilAmigoActivity.this, "Curtido", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(PerfilAmigoActivity.this, "Descurtido", Toast.LENGTH_LONG).show();
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
        imageLoader.init(ImageLoaderConfiguration.createDefault(PerfilAmigoActivity.this));

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
        imageLoaderPost.init(ImageLoaderConfiguration.createDefault(PerfilAmigoActivity.this));

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


        mensagensTop.addView(cardView);
    }
}
