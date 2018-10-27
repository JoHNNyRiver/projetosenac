package com.example.joao.facesenac.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.joao.facesenac.R;

public class CadastroActivity extends AppCompatActivity {
    private TextView backLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        backLogin = findViewById(R.id.backLogin);
        final Intent login = new Intent(this, MainActivity.class);

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login);
            }
        });
    }
}
