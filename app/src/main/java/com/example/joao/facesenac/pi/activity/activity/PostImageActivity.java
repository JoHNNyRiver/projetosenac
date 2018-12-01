package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.PostFeed;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostImageActivity extends AppCompatActivity {
    private static final int SELECTION_CODE = 200;
    private ImageView imageCamera;
    private EditText editTextImage;
    private Button buttonPostarDaImagem;
    private Long idGeneral;
    private Bitmap scaledImage = null;
    private ProgressBar progressBarPostTwo;
    private ImageView imageViewCamera, imageViewDispositivo;
    private Bitmap scaleImage300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        SQLiteDatabase sqlite = openOrCreateDatabase("app", MODE_PRIVATE, null);
        PostUserLogin user = new PostUserLogin();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM user", null);

        int id = cursor.getColumnIndex("id");
        int nomeidx = cursor.getColumnIndex("nome");
        int senhas = cursor.getColumnIndex("senha");
        int email = cursor.getColumnIndex("email");
        int fotoTwo = cursor.getColumnIndex("foto");

        while (cursor.moveToFirst()) {
            user.setId(cursor.getLong(id));
            user.setNome(cursor.getString(nomeidx));
            user.setSenha(cursor.getString(senhas));
            user.setEmail(cursor.getString(email));
            user.setTemfoto(cursor.getInt(fotoTwo));

            break;
        }

        if (cursor.moveToFirst()) {
            idGeneral = user.getId();

            sqlite.close();
            cursor.close();
        }

        editTextImage = findViewById(R.id.editTextImage);
        buttonPostarDaImagem = findViewById(R.id.buttonPostarDaImagem);
        imageCamera = findViewById(R.id.imageCamera);
        progressBarPostTwo = findViewById(R.id.progressBarPostTwo);

        imageViewCamera = findViewById(R.id.imageViewCamera);
        imageViewDispositivo = findViewById(R.id.imageViewDispositivo);

        buttonPostarDaImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarPostTwo.setVisibility(View.VISIBLE);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                scaleImage300.compress(Bitmap.CompressFormat.JPEG, 65, byteArrayOutputStream);

                disabledOrEnabled(false);

                Retrofit postFeed = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiFeed = postFeed.create(ApiUsers.class);
                String textoValue = editTextImage.getText().toString();
                String fotoValue = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                PostFeed postFeeder = new PostFeed();
                postFeeder.setTexto(textoValue);
                postFeeder.setId(idGeneral);
                postFeeder.setFoto(fotoValue);

                Call<Long> callFeed = apiFeed.postFeed(postFeeder);

                Callback<Long> callback = new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        Long resposta = response.body();
                        progressBarPostTwo.setVisibility(View.GONE);
                        disabledOrEnabled(true);

                        if (response.code() == 200) {
                            if (response.isSuccessful() && resposta != null) {
                                Toast.makeText(PostImageActivity.this,
                                        "successfull",
                                        Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(PostImageActivity.this,
                                        "Houve um erro!",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(PostImageActivity.this,
                                    "Houve um erro!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        t.printStackTrace();

                        progressBarPostTwo.setVisibility(View.GONE);
                        disabledOrEnabled(true);

                        Toast.makeText(PostImageActivity.this,
                                "Houve um erro!",
                                Toast.LENGTH_LONG).show();
                    }
                };

                callFeed.enqueue(callback);
            }
        });

        imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_IMAGE_CAPTURE = 51;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        imageViewDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (profile.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(profile, SELECTION_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_IMAGE_CAPTURE = 51;

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageCamera.setVisibility(View.VISIBLE);
            editTextImage.setVisibility(View.VISIBLE);
            buttonPostarDaImagem.setVisibility(View.VISIBLE);

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap imageBitmapTwo = (Bitmap) extras.get("data");

            int largura = imageBitmap.getWidth();
            int altura = imageBitmap.getHeight();
            int x = (150 * largura) / altura;

            int larguraT = imageBitmapTwo.getWidth();
            int alturaT = imageBitmapTwo.getHeight();
            int n = (300 * larguraT) / alturaT;

            scaledImage = Bitmap.createScaledBitmap(imageBitmap, x, 150, true);
            scaleImage300 = Bitmap.createScaledBitmap(imageBitmapTwo, n, 300, true);
            imageCamera.setImageBitmap(scaledImage);
        }

        if (resultCode == RESULT_OK && requestCode == SELECTION_CODE) {
            imageCamera.setVisibility(View.VISIBLE);
            editTextImage.setVisibility(View.VISIBLE);
            buttonPostarDaImagem.setVisibility(View.VISIBLE);

            Uri localImagem = Objects.requireNonNull(data).getData();
            Bitmap imagem = null;
            Bitmap imagemTwo = null;

            try {
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                imagemTwo = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);

                int largura = imagem.getWidth();
                int altura = imagem.getHeight();
                int x = (150 * largura) / altura;

                int larguraT = imagemTwo.getWidth();
                int alturaT = imagemTwo.getHeight();
                int n = (300 * larguraT) / alturaT;

                scaledImage = Bitmap.createScaledBitmap(imagem, x, 150, true);
                scaleImage300 = Bitmap.createScaledBitmap(imagemTwo, n, 300, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageCamera.setImageBitmap(scaledImage);
        }
    }

    private void disabledOrEnabled(Boolean enabled) {
        buttonPostarDaImagem.setEnabled(enabled);
        imageViewDispositivo.setEnabled(enabled);
        imageViewCamera.setEnabled(enabled);
    }
}
