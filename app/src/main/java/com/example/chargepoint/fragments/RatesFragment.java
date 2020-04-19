package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.RateAdapter;
import com.example.chargepoint.adapter.ReceiptsAdapter;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Rate;
import com.example.chargepoint.pojo.Receipt;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RatesFragment extends Fragment {

    private FirebaseHelper fbHelper = FirebaseHelper.getInstance();
    private RateAdapter adapter;
    private ArrayList<Rate> rateArrayList;
    private ArrayList<String> spinnerArray;
    private ArrayAdapter<String> spinnerAdapter;
    private ProgressBar progressBar;

    public RatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rates, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.receiptsPBar);

        loadCounty();
        loadRates();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rateArrayList = new ArrayList<>();
        adapter = new RateAdapter(getContext(), rateArrayList);
        recyclerView.setAdapter(adapter);

        Spinner spinner = view.findViewById(R.id.sort_cities);
        spinnerArray = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, spinnerArray);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(spinner.getSelectedItem().toString());
                rateArrayList.clear();
                adapter.notifyDataSetChanged();
                loadRatesWithCounty(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void loadCounty() {

        fbHelper.getAllChargePoints(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String county = document.getString("address.county");
                    county = county.trim();
                    if (county.contains("Co ")) {
                        county = county.replace("Co ", "");
                    }
                    if (county.contains("Co. ")) {
                        county = county.replace("Co. ", "");
                    }
                    if (county.contains("County ")) {
                        county = county.replace("County ", "");
                    }
                    if (county.contains("Limerick City")) {
                        county = "Limerick";
                    }
                    if (county.contains("KILDARE")) {
                        county = "Kildare";
                    }
                    if (county.contains("Dublin")) {
                        county = "Dublin";
                    }
                    if (!spinnerArray.contains(county)) {
                        spinnerArray.add(county);
                    }
                    Collections.sort(spinnerArray);
                    spinnerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadRates() {

        fbHelper.getAllChargePoints(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    boolean isOp = false;
                    boolean isFastC = false;
                    ArrayList<String> list = (ArrayList<String>) document.get("connections");
                    if (list.toString().contains("isFastChargeCapable=true")) {
                        isFastC = true;
                    }
                    if (list.toString().contains("isOperational=true")) {
                        isOp = true;
                    }

                    Rate rate = new Rate(document.getString("address.town"), document.getString("address.title"), document.getString("address.line1"), isOp, isFastC);
                    rateArrayList.add(rate);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadRatesWithCounty(String county) {

        fbHelper.getAllChargePoints(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (Objects.requireNonNull(document.getString("address.county")).contains(county)) {
                        boolean isOp = false;
                        boolean isFastC = false;
                        ArrayList<String> list = (ArrayList<String>) document.get("connections");
                        if (list.toString().contains("isFastChargeCapable=true")) {
                            isFastC = true;
                        }
                        if (list.toString().contains("isOperational=true")) {
                            isOp = true;
                        }
                        Rate rate = new Rate(document.getString("address.town"), document.getString("address.title"), document.getString("address.line1"), isOp, isFastC);
                        rateArrayList.add(rate);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }
}
