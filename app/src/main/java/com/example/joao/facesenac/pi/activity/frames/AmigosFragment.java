package com.example.joao.facesenac.pi.activity.frames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.adapter.AmigosAdapter;
import com.example.joao.facesenac.pi.activity.adapter.FeedAdapter;
import com.example.joao.facesenac.pi.activity.model.FriendsList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmigosFragment extends Fragment {


    public AmigosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        AmigosAdapter amigosAdapter = new AmigosAdapter();

        RecyclerView recycleAmigos = view.findViewById(R.id.recycleAmigos);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());

        recycleAmigos.setLayoutManager(layout);
        recycleAmigos.setAdapter(amigosAdapter);

        return view;

    }

}
