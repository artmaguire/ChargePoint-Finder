package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;

public class NewsFragment extends BackFragment {

    RecyclerView recyclerView;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);


        recyclerView = v.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLiLayoutManager);



        return v;
    }
}
