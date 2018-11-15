package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.SignupBody;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private TextView backLogin;
    private EditText txtNome, txtEmail, txtSenha, txtSenhaTwo;
    private Button btnCadastro;
    private CheckBox checkLembreCadastro;
    private ProgressBar progressBarCadastro;
    private ImageView profileCadastro;
    private static final int SELECTION_CODE = 200;
    private String imageProfile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        backLogin = findViewById(R.id.backLogin);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtSenhaTwo = findViewById(R.id.txtSenhaTwo);
        btnCadastro = findViewById(R.id.btnCadastro);
        progressBarCadastro = findViewById(R.id.progressBarCadastro);
        profileCadastro = findViewById(R.id.imageFeed);

        final Intent login = new Intent(this, MainActivity.class);

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login);
                finish();
            }
        });

        profileCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (profile.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(profile, SELECTION_CODE);
                }
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNome = txtNome.getText().toString();
                String strEmail = txtEmail.getText().toString();
                String strSenha = txtSenha.getText().toString();
                String strSenhaTwo = txtSenhaTwo.getText().toString();
                String imageProfileF = !imageProfile.equals("") ? imageProfile : "";

                if (strNome.isEmpty()) {
                    Toast.makeText(CadastroActivity.this,
                            "Campos obrigatórios",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (strSenha.isEmpty()) {
                    Toast.makeText(CadastroActivity.this,
                            "Campos obrigatórios",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (strSenhaTwo.isEmpty() || !strSenha.equals(strSenhaTwo)) {
                    Toast.makeText(CadastroActivity.this,
                            "Campos obrigatórios",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (strEmail.isEmpty()) {
                    Toast.makeText(CadastroActivity.this,
                            "Campos obrigatórios",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                btnCadastro.setEnabled(false);
                txtNome.setEnabled(false);
                txtEmail.setEnabled(false);
                txtSenha.setEnabled(false);
                txtSenhaTwo.setEnabled(false);
                backLogin.setEnabled(false);
                progressBarCadastro.setVisibility(View.VISIBLE);

                Retrofit usersApi = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers api = usersApi.create(ApiUsers.class);
                SignupBody signupBody = new SignupBody();

                signupBody.setEmail(strEmail);
                signupBody.setNome(strNome);
                signupBody.setSenha(strSenha);

                signupBody.setFoto(imageProfileF);

                Call<Long> callSignup = api.postSignup(signupBody);

                Callback<Long> callback = new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if (response.code() == 200) {
                            Long res = response.body();
                            Intent feed = new Intent(CadastroActivity.this, FeedActivity.class);

                            if (response.isSuccessful() && res != null) {
                                startActivity(feed);
                                finish();
                            } else {
                                Toast.makeText(CadastroActivity.this, "Houve um erro,  tente novamente!", Toast.LENGTH_LONG).show();

                                progressBarCadastro.setVisibility(View.GONE);
                                btnCadastro.setEnabled(true);
                                txtNome.setEnabled(true);
                                txtEmail.setEnabled(true);
                                txtSenha.setEnabled(true);
                                txtSenhaTwo.setEnabled(true);
                                backLogin.setEnabled(true);
                            }
                        } if (response.code() == 406) {
                            Toast.makeText(CadastroActivity.this,
                                    "Usuário já existe!",
                                    Toast.LENGTH_LONG).show();

                            progressBarCadastro.setVisibility(View.GONE);
                            btnCadastro.setEnabled(true);
                            txtNome.setEnabled(true);
                            txtEmail.setEnabled(true);
                            txtSenha.setEnabled(true);
                            txtSenhaTwo.setEnabled(true);
                            backLogin.setEnabled(true);
                        }
                        else {
                            Toast.makeText(CadastroActivity.this,
                                    "Houve um erro, tente novamente",
                                    Toast.LENGTH_LONG).show();

                            progressBarCadastro.setVisibility(View.GONE);
                            btnCadastro.setEnabled(true);
                            txtNome.setEnabled(true);
                            txtEmail.setEnabled(true);
                            txtSenha.setEnabled(true);
                            txtSenhaTwo.setEnabled(true);
                            backLogin.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        t.printStackTrace();

                        Toast.makeText(CadastroActivity.this,
                                "Houve um erro,  tente novamente!",
                                Toast.LENGTH_LONG).show();

                        progressBarCadastro.setVisibility(View.GONE);
                        btnCadastro.setEnabled(true);
                        txtNome.setEnabled(true);
                        txtEmail.setEnabled(true);
                        txtSenha.setEnabled(true);
                        txtSenhaTwo.setEnabled(true);
                        backLogin.setEnabled(true);
                    }
                };

                callSignup.enqueue(callback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECTION_CODE:
                        Uri localImagem = Objects.requireNonNull(data).getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                        break;
                }

                if (imagem != null) {
                    profileCadastro.setImageBitmap(imagem);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArr = stream.toByteArray();
                    imageProfile = Base64.encodeToString(byteArr, Base64.DEFAULT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
