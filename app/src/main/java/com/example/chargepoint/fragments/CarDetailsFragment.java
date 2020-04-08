package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarDetailsFragment extends Fragment {

    Spinner spinnerManufacturer, spinnerModel   ;
    public CarDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_details, container, false);
    }
}
