package com.example.joao.facesenac.pi.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.model.FeedPerfil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {
    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View adapterFeed = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.feedlayout, viewGroup, false);
        return new FeedHolder(adapterFeed);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder feedHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class FeedHolder extends RecyclerView.ViewHolder {
        ImageView imagemFeed, imageFeedDesc;
        TextView nomeFeed, dataFeed, descFeed, curtidaFeed;
        Button curtir, comentar;

        public FeedHolder(@NonNull View itemView) {
            super(itemView);

            imagemFeed = itemView.findViewById(R.id.imagemFeed);
            imageFeedDesc = itemView.findViewById(R.id.imageFeedDesc);
            nomeFeed = itemView.findViewById(R.id.nomeFeed);
            dataFeed = itemView.findViewById(R.id.dataFeed);
            descFeed = itemView.findViewById(R.id.descFeed);
            curtidaFeed = itemView.findViewById(R.id.curtidaFeed);
            curtir = itemView.findViewById(R.id.curtir);
            comentar = itemView.findViewById(R.id.comentar);
        }
    }
}
