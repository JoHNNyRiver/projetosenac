package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;
import com.example.joao.facesenac.pi.activity.model.SigninBody;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView linkCad;
    private EditText txtLogin, txtSenha;
    private Button btnEntrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkCad = findViewById(R.id.linkCad);
        txtLogin = findViewById(R.id.txtLogin);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);

        final Intent cadastro = new Intent(this, CadastroActivity.class);
        linkCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(cadastro);
                finish();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLogin = txtLogin.getText().toString();
                String strSenha = txtSenha.getText().toString();

                if (strLogin.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Login inválido", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (strSenha.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Login inválido", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                btnEntrar.setEnabled(false);
                txtLogin.setEnabled(false);
                txtSenha.setEnabled(false);
                linkCad.setEnabled(false);

                progressBar.setVisibility(View.VISIBLE);

                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers api = usersApi.create(ApiUsers.class);
                SigninBody signinBody = new SigninBody();

                signinBody.setEmail(strLogin);
                signinBody.setSenha(strSenha);

                Call<PostUserLogin> callUsers = api.postLogin(signinBody);

                Callback<PostUserLogin> callback = new Callback<PostUserLogin>() {
                    @Override
                    public void onResponse(Call<PostUserLogin> call, Response<PostUserLogin> response) {
                        if (response.code() == 200) {
                            PostUserLogin user = response.body();
                            Intent feed = new Intent(MainActivity.this, FeedActivity.class);

                            if (response.isSuccessful() && user != null) {
                                feed.putExtra("nome",  user.getNome());
                                feed.putExtra("email",  user.getEmail());
                                feed.putExtra("foto",  user.getFoto());
                                feed.putExtra("id",  user.getId());
                                feed.putExtra("senha", user.getSenha());

                                startActivity(feed);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Houve um erro, tente novamente!",
                                        Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);
                                btnEntrar.setEnabled(true);
                                txtLogin.setEnabled(true);
                                txtSenha.setEnabled(true);
                                linkCad.setEnabled(true);
                            }
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Response: " + response.code(),
                                    Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);
                            btnEntrar.setEnabled(true);
                            txtLogin.setEnabled(true);
                            txtSenha.setEnabled(true);
                            linkCad.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<PostUserLogin> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this,
                                "Houve um erro, tente novamente!",
                                Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);
                        btnEntrar.setEnabled(true);
                        txtLogin.setEnabled(true);
                        txtSenha.setEnabled(true);
                        linkCad.setEnabled(true);
                    }
                };

                callUsers.enqueue(callback);
            }
        });
    }
}
