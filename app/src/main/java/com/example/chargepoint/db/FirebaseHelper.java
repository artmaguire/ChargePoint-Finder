/*
 * Created by Art.
 * Helper file for connection to the Firebase db.
 * All firebase code should go here, to ensure only one connection to the db is opened.
 * Access to Firebase by: FirebaseHelper fbHelper = FirebaseHelp.getInstance()
 */

package com.example.chargepoint.db;

import com.example.chargepoint.pojo.Car;
import com.example.chargepoint.pojo.Card;
import com.example.chargepoint.pojo.Receipt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseUser currentFirebaseUser;
    private final FirebaseFirestore db;

    private FirebaseHelper() {
        this.db = FirebaseFirestore.getInstance();
        this.currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null)
            instance = new FirebaseHelper();
        else
            instance.setCurrentFirebaseUser();
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    private void setCurrentFirebaseUser() {
        if (this.currentFirebaseUser == null)
            this.currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getAllReceiptsFromUser(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("receipts")
                .whereEqualTo("user_id", currentFirebaseUser.getUid())
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

    public void addReceiptToDB(Receipt r, OnCompleteListener<DocumentReference> listener) {
        db.collection("receipts").add(r).addOnCompleteListener(listener);
    }

    public void getCards(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("cards").whereEqualTo("user_id", currentFirebaseUser.getUid()).get().addOnCompleteListener(listener);
    }

    public void addCardToDB(Card c, OnCompleteListener<DocumentReference> listener) {
        db.collection("cards").add(c).addOnCompleteListener(listener);
    }

    public void addCarToDB(Car c, OnCompleteListener<DocumentReference> listener) {
        db.collection("cars").add(c).addOnCompleteListener(listener);
    }
}
