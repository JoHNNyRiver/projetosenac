package com.example.joao.facesenac.pi.activity.frames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joao.facesenac.R;
import com.example.joao.facesenac.pi.activity.adapter.FeedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        FeedAdapter feedAdapter = new FeedAdapter();

        RecyclerView recycleFeed = view.findViewById(R.id.recycleFeed);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());

        recycleFeed.setLayoutManager(layout);
        recycleFeed.setAdapter(feedAdapter);

        return view;
    }

}
