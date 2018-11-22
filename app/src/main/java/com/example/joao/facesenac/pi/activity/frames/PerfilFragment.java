package com.example.joao.facesenac.pi.activity.frames;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.adapter.FeedAdapter;

public class PerfilFragment extends Fragment {
    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String nome = getArguments().getString("nome");
        String email = getArguments().getString("email");
        Long id = getArguments().getLong("id");

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        FeedAdapter feedAdapter = new FeedAdapter();

        RecyclerView recycleFeed = view.findViewById(R.id.recyclePerfil);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());

        TextView nomePerfil = view.findViewById(R.id.nomePerfil);
        TextView emailPerfil = view.findViewById(R.id.emailPerfil);

        nomePerfil.setText(nome);
        emailPerfil.setText(email);

        recycleFeed.setLayoutManager(layout);
        recycleFeed.setAdapter(feedAdapter);

        return view;
    }
}
