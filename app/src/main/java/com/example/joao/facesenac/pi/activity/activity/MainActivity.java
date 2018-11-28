package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;
import com.example.joao.facesenac.pi.activity.model.SigninBody;

import java.util.ArrayList;
import java.util.List;

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
    private CheckBox cbLembrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkCad = findViewById(R.id.linkCad);
        txtLogin = findViewById(R.id.txtLogin);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);
        cbLembrar = findViewById(R.id.cbLembrar);

        try {
            SQLiteDatabase db = openOrCreateDatabase("app", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS user(id INT(30), nome VARCHAR(255), senha VARCHAR(255), email VARCHAR(255), foto INTEGER )");

            PostUserLogin user = new PostUserLogin();
            Intent feed = new Intent(MainActivity.this, FeedActivity.class);
            Cursor cursor = db.rawQuery("SELECT * FROM user", null);

            int id = cursor.getColumnIndex("id");
            int nomeidx = cursor.getColumnIndex("nome");
            int senha = cursor.getColumnIndex("senha");
            int email = cursor.getColumnIndex("email");
            int foto = cursor.getColumnIndex("foto");

            while (cursor.moveToFirst()) {
                user.setId(cursor.getLong(id));
                user.setNome(cursor.getString(nomeidx));
                user.setSenha(cursor.getString(senha));
                user.setEmail(cursor.getString(email));
                user.setTemfoto(cursor.getInt(foto));

                break;
            }

            if (cursor.moveToFirst()) {
                Boolean tempFoto = user.getTemfoto() != null;

                feed.putExtra("nome",  user.getNome());
                feed.putExtra("email",  user.getEmail());
                feed.putExtra("foto",  tempFoto);
                feed.putExtra("id",  user.getId());
                feed.putExtra("senha", user.getSenha());

                db.close();
                cursor.close();

                startActivity(feed);
                finish();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


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

                                Boolean tempFoto = user.getTemfoto() != null;

                                feed.putExtra("foto",  tempFoto);
                                feed.putExtra("id",  user.getId());
                                feed.putExtra("senha", user.getSenha());

                                SQLiteDatabase db = openOrCreateDatabase("app", MODE_PRIVATE, null);
                                db.execSQL("INSERT INTO user(id, nome, senha, email, foto) " +
                                        "VALUES('"+ user.getId() +"', " +
                                        "'"+ user.getNome() +"', " +
                                        "'"+ user.getSenha() +"', " +
                                        "'"+ user.getEmail() +"', " +
                                        "'"+ user.getTemfoto() +"')");


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
