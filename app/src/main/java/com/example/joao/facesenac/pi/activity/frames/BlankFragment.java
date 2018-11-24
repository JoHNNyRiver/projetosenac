package com.example.joao.facesenac.pi.activity.frames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.activity.FeedActivity;
import com.example.joao.facesenac.pi.activity.activity.MainActivity;
import com.example.joao.facesenac.pi.activity.adapter.FeedAdapter;
import com.example.joao.facesenac.pi.activity.interfaces.ApiUsers;
import com.example.joao.facesenac.pi.activity.model.PostFeed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private EditText texto;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ImageButton btnEnviar = view.findViewById(R.id.btnEnviar);

        FeedAdapter feedAdapter = new FeedAdapter();

        RecyclerView recycleFeed = view.findViewById(R.id.recycleFeed);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());

        recycleFeed.setLayoutManager(layout);
        recycleFeed.setAdapter(feedAdapter);

        texto = view.findViewById(R.id.textoPostagem);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedActivity activity = (FeedActivity) getActivity();
                Long id = activity.returnId();

                Retrofit postFeed = new Retrofit.Builder()
                        .baseUrl("https://pi4facenac.azurewebsites.net/PI4/api/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiUsers apiFeed = postFeed.create(ApiUsers.class);
                String textoValue = texto.getText().toString();

                PostFeed postFeedI = new PostFeed(id, textoValue, "");
                Call<Long>  callFeed = apiFeed.postFeed(postFeedI);

                Callback<Long> callback = new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if (response.code() == 200) {
                            Long resposta = response.body();

                            if (response.isSuccessful() && resposta != null) {
                                Toast.makeText(getContext(),
                                        "successfull",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),
                                        "Houve um erro, 1!",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(),
                                    "Houve um erro, 2!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        t.printStackTrace();

                        Toast.makeText(getContext(),
                                "Houve um erro,  3!",
                                Toast.LENGTH_LONG).show();
                    }
                };

                callFeed.enqueue(callback);
            }
        });

        return view;
    }

}
