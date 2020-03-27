package com.example.chargepoint.ui.rates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.FirebaseHelper;
import com.example.chargepoint.R;
import com.example.chargepoint.adapter.RateAdapter;
import com.example.chargepoint.pojo.Rate;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RatesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RateAdapter adapter;
    private ArrayList<Rate> rateArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rates, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rateArrayList = new ArrayList<>();
        adapter = new RateAdapter(getContext(), rateArrayList);
        recyclerView.setAdapter(adapter);

        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getAllRates(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    System.out.println(document.getString("name") + " " + document.getString("a") + " " + document.getString("b") + " " + document.getString("c"));
                    Rate rate = new Rate(document.getString("name"), document.getString("a"), document.getString("b"), document.getString("c"));
                    rateArrayList.add(rate);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        System.out.println(rateArrayList.toString());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_rates);
    }
}
