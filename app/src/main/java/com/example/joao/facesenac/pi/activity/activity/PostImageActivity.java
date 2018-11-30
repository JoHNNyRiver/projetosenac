package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.joao.facesenac.R;
import java.io.IOException;
import java.util.Objects;

public class PostImageActivity extends AppCompatActivity {
    private ImageView imageCamera;
    private static final int SELECTION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ImageView imageViewCamera = findViewById(R.id.imageViewCamera);
        ImageView imageViewDispositivo = findViewById(R.id.imageViewDispositivo);
        imageCamera = findViewById(R.id.imageCamera);

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
        Bitmap scaledImage = null;
        int REQUEST_IMAGE_CAPTURE = 51;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageCamera.setVisibility(View.VISIBLE);

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            scaledImage = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            imageCamera.setImageBitmap(scaledImage);
        }

        if (resultCode == RESULT_OK && requestCode == SELECTION_CODE) {
            imageCamera.setVisibility(View.VISIBLE);
            Uri localImagem = Objects.requireNonNull(data).getData();
            Bitmap imagem = null;

            try {
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                scaledImage = Bitmap.createScaledBitmap(imagem, 500, 500, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageCamera.setImageBitmap(scaledImage);
        }
    }
}
