/*
 * Created by Art.
 * Helper file for connection to the Firebase db.
 * All firebase code should go here, to ensure only one connection to the db is opened.
 * Access to Firebase by: FirebaseHelper fbHelper = FirebaseHelp.getInstance()
 */

package com.example.chargepoint.db;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseFirestore db;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null)
            instance = new FirebaseHelper();
        return instance;
    }

    public void getAllReceiptsFromUser(OnCompleteListener<QuerySnapshot> listener) {
        // TODO: Order receipts by data, working with specific user
        db.collection("receipts")
                .whereEqualTo("user", currentFirebaseUser.getUid())
                .orderBy("datetime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getAllChargePoints(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("map")
                .get()
                .addOnCompleteListener(listener);
    }

    public void getChargePoint(String map_id, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("map")
                .document(map_id)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getAllRates(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("rates")
                .get()
                .addOnCompleteListener(listener);
    }
}
