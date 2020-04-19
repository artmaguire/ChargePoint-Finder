package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.GridAdapter;

public class AboutFragment extends Fragment {

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
        this.setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_about, container, false);

        gridView = (GridView) root.findViewById(R.id.gridView);
        GridAdapter gridAdapter = new GridAdapter(getContext(), name, image);
        gridView.setAdapter(gridAdapter);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
