package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.ReceiptsAdapter;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Receipt;

import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousReceiptsFragment extends Fragment {

    private static final String TAG = "PAYMENT_RECEIPT";
    private ReceiptsAdapter adapter;
    private ProgressBar pgsBar;

    public PreviousReceiptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_previous_receipts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.receiptsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReceiptsAdapter(view);
        recyclerView.setAdapter(adapter);

        pgsBar = view.findViewById(R.id.receiptsPBar);

        //TODO: Get user receipts currently in database
        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getAllReceipts(task -> {
            if (task.isSuccessful()) {
                List<Receipt> receipts = task.getResult().toObjects(Receipt.class);
                adapter.setReceipts(receipts);
                adapter.notifyDataSetChanged();

                pgsBar.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        String invoiceID = UUID.randomUUID().toString();
        Log.d(TAG, "onCreate: " + invoiceID);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
