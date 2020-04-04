package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private final String TAG = "ChargeMap";
    private GoogleMap map;
    private List<ChargePoint> chargePoints;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        getChargePoints();

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        return root;
    }

    private void getChargePoints() {
        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getAllChargePoints(task -> {
            if (task.isSuccessful()) {
                //TODO: ART -> fields starting with 'is' not setting properly
                chargePoints = task.getResult().toObjects(ChargePoint.class);
                checkIfMapAndDbReady();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        LatLng ireland = new LatLng(53.4, -8);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(ireland, 7));

        checkIfMapAndDbReady();
    }

    private void checkIfMapAndDbReady() {
        if (map != null & chargePoints != null)
            addChargePointsToMap();
    }

    private void addChargePointsToMap() {
        for (ChargePoint cp : chargePoints) {
            map.addMarker(new MarkerOptions()
                    .position(cp.getLocationAsLatLng())
                    .title(cp.getOperator()));
        }
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
