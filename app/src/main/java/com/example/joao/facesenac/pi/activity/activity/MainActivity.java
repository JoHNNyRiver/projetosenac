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
import com.example.joao.facesenac.pi.activity.activity.CadastroActivity;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.Users;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                progressBar.setVisibility(View.VISIBLE);

                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/pi4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers api = usersApi.create(ApiUsers.class);
                Call<Users> callUsers = api.postLogin(new Users(strLogin, strSenha));

                Callback<Users> callback = new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        Users user = response.body();
                        Intent feed = new Intent(MainActivity.this, FeedActivity.class);

                        if (response.isSuccessful() && user != null) {
                            startActivity(feed);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this,
                                "Houve um erro, tente novamente!",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                };

                callUsers.enqueue(callback);
            }
        });
    }
}
