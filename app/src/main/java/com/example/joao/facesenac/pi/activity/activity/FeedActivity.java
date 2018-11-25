package com.example.joao.facesenac.pi.activity.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.frames.AmigosFragment;
import com.example.joao.facesenac.pi.activity.frames.BlankFragment;
import com.example.joao.facesenac.pi.activity.frames.BuscarAmigosFragment;
import com.example.joao.facesenac.pi.activity.frames.PerfilFragment;
import com.example.joao.facesenac.pi.activity.frames.SobreFragment;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;

import java.util.Date;

public class FeedActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Long idGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        navigationView = findViewById(R.id.navigation_view);

        View header = navigationView.getHeaderView(0);
        TextView txtNome = header.findViewById(R.id.txtNome);
        ImageView profileMenu = header.findViewById(R.id.imageFeedDesc);

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
        idGeneral = bdl.getLong("id");

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

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        BlankFragment fragment = new BlankFragment();

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, fragment)
                                .commit();

                        return true;
                    case R.id.perfil:
                        SQLiteDatabase dbPerfil = openOrCreateDatabase(
                                "app", MODE_PRIVATE, null);
                        PostUserLogin user = new PostUserLogin();

                        try {
                            Cursor cursor = dbPerfil.rawQuery(
                                    "SELECT * FROM user", null);

                            int id = cursor.getColumnIndex("idGeneral");
                            int nomeidx = cursor.getColumnIndex("nome");
                            int senha = cursor.getColumnIndex("senha");
                            int email = cursor.getColumnIndex("email");
                            int foto = cursor.getColumnIndex("foto");

                            while (cursor.moveToFirst()) {
                                user.setId(cursor.getLong(id));
                                user.setNome(cursor.getString(nomeidx));
                                user.setSenha(cursor.getString(senha));
                                user.setEmail(cursor.getString(email));
                                user.setFoto(cursor.getString(foto));

                                break;
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        PerfilFragment perfilFragment = new PerfilFragment();
                        Bundle bundles = new Bundle();
                        bundles.putString("nome", user.getNome());
                        bundles.putString("email", user.getEmail());
                        bundles.putLong("idGeneral", user.getId());

                        perfilFragment.setArguments(bundles);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, perfilFragment)
                                .commit();

                        return true;
                    case R.id.amigos:
                        AmigosFragment amigosFragment = new AmigosFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, amigosFragment)
                                .commit();

                        return true;
                    case R.id.busca_amigos:
                        BuscarAmigosFragment buscarAmigosFragment = new BuscarAmigosFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, buscarAmigosFragment)
                                .commit();

                        return true;
                    case R.id.sobre:
                        SobreFragment sobreFragment = new SobreFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, sobreFragment)
                                .commit();

                        return true;
                    case R.id.sair:
                        SQLiteDatabase db = openOrCreateDatabase(
                                "app", MODE_PRIVATE, null);
                        db.execSQL("DROP TABLE user");

                        finish();
                        break;
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

    public Long returnId() {
        return idGeneral;
    }

}
