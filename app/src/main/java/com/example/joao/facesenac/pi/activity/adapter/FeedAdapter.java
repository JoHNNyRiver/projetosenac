package com.example.joao.facesenac.pi.activity.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joao.facesenac.R;

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
        return 20;
    }

    public class FeedHolder extends RecyclerView.ViewHolder {

        public FeedHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
