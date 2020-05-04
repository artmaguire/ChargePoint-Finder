package com.example.chargepoint.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.ChargePointAdapter;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.dialogbox.DialogBoxCounty;
import com.example.chargepoint.dialogbox.DialogBoxDistance;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.viewmodel.ChargePointViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChargePointsFragment extends Fragment {

    private final static String TAG = "CHARGEPOINT_FRAGMENT";

    private FirebaseHelper fbHelper = FirebaseHelper.getInstance();
    private ChargePointAdapter adapter;
    private ArrayList<String> spinnerArray;
    private ArrayAdapter<String> spinnerAdapter;
    private ProgressBar progressBar;
    private TextView noChargePointsView;

    public ChargePointsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chargepoints, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noChargePointsView = view.findViewById(R.id.noChargePointsView);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChargePointAdapter(view);
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.chargePointsPBar);

        Spinner spinner = view.findViewById(R.id.sort_cities);
        spinnerArray = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, spinnerArray);
        spinner.setAdapter(spinnerAdapter);

        loadSort();
        // loadCounty();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("County")) {
                    new DialogBoxCounty().show(getActivity().getSupportFragmentManager(), "DialogBoxCounty");
                } else if(spinner.getSelectedItem().toString().equals("Distance")) {
                    System.out.println("Distance choose : " + spinner.getSelectedItem().toString());
                    new DialogBoxDistance().show(getParentFragmentManager(), "DialogBoxDistance");
                } else {
                    System.out.println("Nothing selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ChargePointViewModel chargePointViewModel = new ViewModelProvider(requireActivity()).get(ChargePointViewModel.class);
        chargePointViewModel.getObservableChargePoints().observe(getViewLifecycleOwner(), chargePoints -> {
            adapter.setChargePoints(chargePoints);
            adapter.notifyDataSetChanged();

            if (adapter.getItemCount() == 0) {
                noChargePointsView.setVisibility(View.VISIBLE);
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void loadSort() {

        List<String> sort = new ArrayList<>(Arrays.asList("", "County", "Distance"));
        spinnerArray.addAll(sort);
        spinnerAdapter.notifyDataSetChanged();

    }

    // TODO: filter by county
    private void filterChargePointsByCounty(String county) {
        fbHelper.getAllChargePoints(task -> {
            if (task.isSuccessful()) {
                List<ChargePoint> chargePoints = task.getResult().toObjects(ChargePoint.class);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.titlebar_search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchChargePoints);
        android.widget.SearchView searchView = (android.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


}
