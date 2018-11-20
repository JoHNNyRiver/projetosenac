package com.example.joao.facesenac.pi.activity.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.model.FriendsList;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigosHolder> {
    @NonNull
    @Override
    public AmigosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View adapterAmigos = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.amigoslayout, viewGroup, false);
        return new AmigosHolder(adapterAmigos);
    }
    @Override
    public void onBindViewHolder(@NonNull AmigosHolder amigosHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class AmigosHolder extends RecyclerView.ViewHolder {

        public AmigosHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

