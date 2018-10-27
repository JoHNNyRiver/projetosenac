package com.example.joao.facesenac.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.activity.CadastroActivity;

public class MainActivity extends AppCompatActivity {
    private TextView linkCad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkCad = findViewById(R.id.linkCad);
        final Intent cadastro = new Intent(this, CadastroActivity.class);

        linkCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(cadastro);
            }
        });
    }
}
