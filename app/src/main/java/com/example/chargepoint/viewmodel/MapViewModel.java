package com.example.chargepoint.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapViewModel extends ViewModel {
    private final static String TAG = "MAP_VM";

    private CameraPosition mapCameraPosition = new CameraPosition(new LatLng(53.4, -8), 7, 0, 0);

    public CameraPosition getMapCameraPosition() {
        return mapCameraPosition;
    }

    public void setMapCameraPosition(CameraPosition mapCameraPosition) {
        this.mapCameraPosition = mapCameraPosition;
    }
}
