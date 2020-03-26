package com.example.chargepoint;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseFirestore db;

    private FirebaseHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null)
            instance = new FirebaseHelper();
        return instance;
    }

    public void getAllReceipts(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("receipts")
                .get()
                .addOnCompleteListener(listener);
    }

    public void getAllMapPoints(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("map")
                .get()
                .addOnCompleteListener(listener);
    }

    public void getMapPoint(String map_id, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("map")
                .document(map_id)
                .get()
                .addOnCompleteListener(listener);
    }
}
