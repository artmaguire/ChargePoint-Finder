package com.example.chargepoint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class PreviousReceiptsActivity extends AppCompatActivity {

    private static final String TAG = "Payment_Receipt";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReceiptsRecyclerAdapter adapter;
    private ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_receipts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.receiptsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReceiptsRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        pgsBar = findViewById(R.id.receiptsPBar);

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
