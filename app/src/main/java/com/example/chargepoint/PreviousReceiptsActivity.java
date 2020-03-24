package com.example.chargepoint;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class PreviousReceiptsActivity extends AppCompatActivity {

    private static final String TAG = "Payment Receipt";
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReceiptsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_receipts);

        recyclerView = findViewById(R.id.receiptsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReceiptsRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        //TODO: Get user receipts currently in database
        db.collection("receipts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Receipt> receipts = task.getResult().toObjects(Receipt.class);
                            adapter.setReceipts(receipts);
                            adapter.notifyDataSetChanged();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        String invoiceID = UUID.randomUUID().toString();
        Log.d(TAG, "onCreate: " + invoiceID);
    }
}
