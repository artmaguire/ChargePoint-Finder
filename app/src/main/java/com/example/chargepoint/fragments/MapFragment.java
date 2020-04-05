package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.MainViewModel;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    MainViewModel mainViewModel;
    private MapView mapView;
    private final String TAG = "ChargeMap";
    private GoogleMap map;
    private List<ChargePoint> chargePoints;
    private boolean saved = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mainViewModel.getObservableChargePoints().observe(getViewLifecycleOwner(), chargePoints -> {
            this.chargePoints = chargePoints;
            checkIfMapAndDbReady();
        });

        setRetainInstance(true);

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // First incarnation of this activity.
        saved = savedInstanceState != null;

        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        this.map.setOnCameraMoveListener(this);

        if (!saved) {
            this.map.moveCamera(CameraUpdateFactory.newCameraPosition(mainViewModel.getMapCameraPosition()));
        }

        checkIfMapAndDbReady();
    }

    private void checkIfMapAndDbReady() {
        if (map != null && chargePoints != null)
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

    @Override
    public void onCameraMove() {
        mainViewModel.setMapCameraPosition(map.getCameraPosition());
    }
}
