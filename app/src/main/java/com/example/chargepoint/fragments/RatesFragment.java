package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.RateAdapter;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Rate;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RatesFragment extends Fragment {

    private FirebaseHelper fbHelper = FirebaseHelper.getInstance();
    private RateAdapter adapter;
    private ArrayList<Rate> rateArrayList;
    private ArrayList<String> spinnerArray;
    private ArrayAdapter<String> spinnerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rates, container, false);

        Spinner spinner = root.findViewById(R.id.sort_cities);
        spinnerArray = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1, spinnerArray);

        spinner.setAdapter(spinnerAdapter);

        loadCounty();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rateArrayList = new ArrayList<>();
        adapter = new RateAdapter(getContext(), rateArrayList);
        recyclerView.setAdapter(adapter);

        loadRates();

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

        return root;
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
                    adapter.notifyDataSetChanged();
                }
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
