package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Dashboard Navigation by navGraph
        root.findViewById(R.id.aboutCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_about);
        });

        root.findViewById(R.id.newsCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_news);
        });

        root.findViewById(R.id.termsCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_terms);
        });

        root.findViewById(R.id.carDetailsCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_car_details);
        });

        return root;
    }

    @Override
    public void onStart() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
