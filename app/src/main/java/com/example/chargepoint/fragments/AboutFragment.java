package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.GridAdapter;

public class AboutFragment extends BackFragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    GridView gridView;

    String[] name = {
        "Art",
        "Corentin",
        "Arthur",
        "Michael"
    };

    int[] image = {
        R.drawable.art,
        R.drawable.coco,
        R.drawable.arthur,
        R.drawable.tobi
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);

        View root = inflater.inflate(R.layout.fragment_about, container, false);

        gridView = (GridView) root.findViewById(R.id.gridView);
        GridAdapter gridAdapter = new GridAdapter(getContext(), name, image);
        gridView.setAdapter(gridAdapter);

        return root;
    }
}
