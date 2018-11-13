package com.example.joao.facesenac.pi.activity.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.frames.BlankFragment;

import java.math.BigInteger;

public class FeedActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        navigationView = findViewById(R.id.navigation_view);

        View header = navigationView.getHeaderView(0);
        TextView txtNome = header.findViewById(R.id.txtNome);
        ImageView profileMenu = header.findViewById(R.id.profileMenu);

        BlankFragment fragment = new BlankFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, fragment)
                .commit();

        Bundle bdl = getIntent().getExtras();
        String nome = bdl.getString("nome");
        String foto = bdl.getString("foto");
        String email = bdl.getString("email");
        String senha = bdl.getString("senha");
        Long id = bdl.getLong("id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(nome);


        byte[] decodedString = Base64.decode(foto, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        txtNome.setText(nome);
//        profileMenu.setImageBitmap(decodedByte);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                drawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.home) {
                    BlankFragment fragment = new BlankFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_container, fragment)
                            .commit();

                    return true;
                }

                return false;
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.openDrawer, R.string.closeDrawer) {};
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
