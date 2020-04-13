package com.example.chargepoint.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapViewModel extends ViewModel {
    private MutableLiveData<List<ChargePoint>> chargePoints;
    private CameraPosition mapCameraPosition = new CameraPosition(new LatLng(53.4, -8), 7, 0, 0);

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
                List<ChargePoint> cps = task.getResult().toObjects(ChargePoint.class);
                Log.d("VIEW_MODEL", "" + cps.size());
                chargePoints.postValue(cps);
            });
        }
    }

    public CameraPosition getMapCameraPosition() {
        return mapCameraPosition;
    }

    public void setMapCameraPosition(CameraPosition mapCameraPosition) {
        this.mapCameraPosition = mapCameraPosition;
    }
}
