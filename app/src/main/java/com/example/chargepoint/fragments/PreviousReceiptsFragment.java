package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.ReceiptsAdapter;
import com.example.chargepoint.viewmodel.ReceiptViewModel;

/**
 * Created by Art
 * User can see a list of all their receipts
 */
public class PreviousReceiptsFragment extends BackFragment {

    private static final String TAG = "PAYMENT_RECEIPT";
    private ReceiptsAdapter adapter;
    private ProgressBar pgsBar;
    private TextView noReceiptsView;

    public PreviousReceiptsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        noReceiptsView = view.findViewById(R.id.noReceiptsView);

        ReceiptViewModel receiptViewModel = new ViewModelProvider(requireActivity()).get(ReceiptViewModel.class);
        receiptViewModel.getObservableReceipts().observe(getViewLifecycleOwner(), receipts -> {
            adapter.setReceipts(receipts);
            adapter.notifyDataSetChanged();

            if (adapter.getItemCount() == 0) {
                noReceiptsView.setVisibility(View.VISIBLE);
            }

            pgsBar.setVisibility(View.GONE);
        });
    }
}
