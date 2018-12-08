package com.example.joao.facesenac.pi.activity.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.frames.AmigosFragment;
import com.example.joao.facesenac.pi.activity.frames.BlankFragment;
import com.example.joao.facesenac.pi.activity.frames.BuscarAmigosFragment;
import com.example.joao.facesenac.pi.activity.frames.PerfilFragment;
import com.example.joao.facesenac.pi.activity.frames.SobreFragment;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.GetFriends;
import com.example.joao.facesenac.pi.activity.model.PostUserLogin;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Long idGeneral;
    private Boolean foto;
    private String nome;
    private String emailTwo;
    private String senha;
    private int idNotify;



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
            foto = user.getTemfoto() != null;
            nome = user.getNome();
            emailTwo = user.getEmail();
            senha = user.getSenha();

            sqlite.close();
            cursor.close();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(nome);

        txtNome.setText(nome);

        String url = "https://pi4facenac.azurewebsites.net/PI4/api/users/image/" + idGeneral;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        if (foto) {
            imageLoader.displayImage(url, profileMenu);
        }

        Bundle bdl = getIntent().getExtras();

        if (bdl != null && bdl.getBoolean("amigos")) {
            AmigosFragment amigosFragment = new AmigosFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frag_container, amigosFragment)
                    .commit();
        }

        Retrofit usersApi = new Retrofit.Builder()
                .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUsers apiUsers = usersApi.create(ApiUsers.class);
        Call<ArrayList<GetFriends>> callPosts = apiUsers.getFriendsAPI(user.getId());

        Callback<ArrayList<GetFriends>> callback = new Callback<ArrayList<GetFriends>>() {
            @Override
            public void onResponse(Call<ArrayList<GetFriends>> call, Response<ArrayList<GetFriends>> response) {
                ArrayList<GetFriends> body = response.body();

                if (response.isSuccessful() && body.size() > 0) {
                    for (int i = 0; i < body.size(); i++) {

                        if (body.get(i).getAmizade() != null) {
                            if (body.get(i).getAmizade().equals("solicitado")) {
                                createNotification(body.get(i).getNome());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetFriends>> call, Throwable t) {
                t.printStackTrace();
            }
        };

        callPosts.enqueue(callback);

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
                        SQLiteDatabase dbPerfil = openOrCreateDatabase("app", MODE_PRIVATE, null);
                        PostUserLogin user = new PostUserLogin();
                        PerfilFragment fragmentPerfil = new PerfilFragment();

                        try {
                            Cursor cursor = dbPerfil.rawQuery(
                                    "SELECT * FROM user", null);

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

                            Bundle bundle = new Bundle();
                            bundle.putString("nome", user.getNome());
                            bundle.putString("email", user.getEmail());

                            Boolean tempFoto = user.getTemfoto() != null;

                            bundle.putBoolean("foto", tempFoto);
                            fragmentPerfil.setArguments(bundle);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, fragmentPerfil)
                                .commit();

                        return true;
                    case R.id.amigos:
                        AmigosFragment amigosFragment = new AmigosFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_container, amigosFragment)
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

    public static class CommentActivity extends AppCompatActivity {
        private EditText editTextComment;
        private Button buttonComment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_comment);
        }
    }

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public void createNotification(String nome)
    {
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(FeedActivity.this , FeedActivity.class);
        resultIntent.putExtra("amigos", true);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(FeedActivity.this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(FeedActivity.this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Pedido de amizade")
                .setContentText("O usuário " + nome + " quer ser seu amigo")
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) FeedActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.cancel(idNotify);
    }
}
