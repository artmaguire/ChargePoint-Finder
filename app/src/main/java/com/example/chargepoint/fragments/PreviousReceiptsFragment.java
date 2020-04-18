package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.ReceiptsAdapter;
import com.example.chargepoint.viewmodel.ReceiptViewModel;

public class PreviousReceiptsFragment extends Fragment {

    private static final String TAG = "PAYMENT_RECEIPT";
    private ReceiptsAdapter adapter;
    private ProgressBar pgsBar;

    public PreviousReceiptsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        ReceiptViewModel receiptViewModel = new ViewModelProvider(requireActivity()).get(ReceiptViewModel.class);
        receiptViewModel.getObservableReceipts().observe(getViewLifecycleOwner(), receipts -> {
            adapter.setReceipts(receipts);
            adapter.notifyDataSetChanged();

            pgsBar.setVisibility(View.GONE);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
