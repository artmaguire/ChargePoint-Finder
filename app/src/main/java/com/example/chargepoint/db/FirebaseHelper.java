/*
 * Created by Art.
 * Helper file for connection to the Firebase db.
 * All firebase code should go here, to ensure only one connection to the db is opened.
 * Access to Firebase by: FirebaseHelper fbHelper = FirebaseHelp.getInstance()
 */

package com.example.chargepoint.db;

import android.util.Log;

import com.example.chargepoint.pojo.Car;
import com.example.chargepoint.pojo.Card;
import com.example.chargepoint.pojo.Receipt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Art
 */
public class FirebaseHelper {
    private static FirebaseHelper instance;
    private final FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;

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
        db.collection("receipts").whereEqualTo("user_id", currentFirebaseUser.getUid()).get().addOnCompleteListener(listener);
    }

    public void getAllChargePoints(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("chargepoints").get().addOnCompleteListener(listener);
    }

    public void getChargePoint(String map_id, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("chargepoints").document(map_id).get().addOnCompleteListener(listener);
    }

    public void addReceiptToDB(Receipt r, OnCompleteListener<DocumentReference> listener) {
        db.collection("receipts").add(r).addOnCompleteListener(listener);
    }

    public void getCards(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("cards").whereEqualTo("user_id", currentFirebaseUser.getUid()).get().addOnCompleteListener(listener);
    }

    public void updateCard(Card oldCard, Card newCard, OnCompleteListener<DocumentReference> listener) {
        // Delete First
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference itemsRef = rootRef.collection("cards");
        Query query = itemsRef.whereEqualTo("cardName", oldCard.getCardName())
                .whereEqualTo("cardNumber", oldCard.getCardNumber())
                .whereEqualTo("cardDate", oldCard.getCardDate())
                .whereEqualTo("cardSecurityNumber", oldCard.getCardSecurityNumber());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    itemsRef.document(document.getId()).delete();
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });

        //Add Card
        addCardToDB(newCard, listener);
    }

    public void addCardToDB(Card c, OnCompleteListener<DocumentReference> listener) {
        db.collection("cards").add(c).addOnCompleteListener(listener);
    }

    public void addCarToDB(Car c, OnCompleteListener<DocumentReference> listener) {
        db.collection("cars").add(c).addOnCompleteListener(listener);
    }
}
