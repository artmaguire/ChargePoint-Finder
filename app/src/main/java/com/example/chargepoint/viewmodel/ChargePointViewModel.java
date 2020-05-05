package com.example.chargepoint.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Art
 * Gets all ChargePoints in the database
 */
public class ChargePointViewModel extends ViewModel {
    private final static String TAG = "CHARGEPOINT_VM";

    private MutableLiveData<List<ChargePoint>> chargePoints;

    public LiveData<List<ChargePoint>> getObservableChargePoints() {
        if (chargePoints == null) {
            chargePoints = new MutableLiveData<>();
            loadChargePoints();
        }
        return chargePoints;
    }

    public void loadChargePoints() {
        if (chargePoints == null) {
            chargePoints = new MutableLiveData<>();
            FirebaseHelper fbHelper = FirebaseHelper.getInstance();
            fbHelper.getAllChargePoints(task -> {
                if (task.getResult() == null)
                    return;

                List<ChargePoint> cps = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    ChargePoint cp = doc.toObject(ChargePoint.class);
                    if (cp != null) {
                        cp.setMap_id(doc.getId());
                        cps.add(cp);
                    }
                }

                Log.i(TAG, "" + cps.size());
                chargePoints.postValue(cps);
            });
        }
    }
}
