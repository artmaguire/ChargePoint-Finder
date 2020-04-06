package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.map.ChargePointCluster;
import com.example.chargepoint.map.ChargePointClusterRenderer;
import com.example.chargepoint.map.ChargePointInfoWindowAdapter;
import com.example.chargepoint.map.MapViewModel;
import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Iterator;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, ClusterManager.OnClusterItemInfoWindowClickListener<ChargePointCluster> {

    private final String TAG = "ChargeMap";

    private View root;
    private MapViewModel mapViewModel;
    private MapView mapView;
    private GoogleMap map;
    private ClusterManager<ChargePointCluster> clusterManager;
    private List<ChargePoint> chargePoints;
    private boolean saved = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);

        mapViewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);
        mapViewModel.getObservableChargePoints().observe(getViewLifecycleOwner(), chargePoints -> {
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
    public void onMapReady(GoogleMap m) {
        this.map = m;

        map.setOnCameraMoveListener(this);

        if (!saved) {
            this.map.moveCamera(CameraUpdateFactory.newCameraPosition(mapViewModel.getMapCameraPosition()));
        }

        clusterManager = new ClusterManager<>(getContext(), map);
        ChargePointClusterRenderer renderer = new ChargePointClusterRenderer(getContext(), map, clusterManager);
        clusterManager.setRenderer(renderer);

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new ChargePointInfoWindowAdapter(getContext()));
        map.setInfoWindowAdapter(clusterManager.getMarkerManager());

        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        map.setOnInfoWindowClickListener(clusterManager);

        clusterManager.setOnClusterClickListener(cluster -> {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            Iterator<ChargePointCluster> iterator = cluster.getItems().iterator();
            while (iterator.hasNext()) {
                builder.include(iterator.next().getPosition());
            }
            LatLngBounds bounds = builder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
            return true;
        });

        checkIfMapAndDbReady();
    }

    private void checkIfMapAndDbReady() {
        if (map != null && chargePoints != null)
            addChargePointsToMap();
    }

    private void addChargePointsToMap() {
        for (ChargePoint cp : chargePoints) {
            clusterManager.addItem(new ChargePointCluster(cp.getLocationAsLatLng(), cp.getOperator(), cp));
        }
    }

    @Override
    public void onClusterItemInfoWindowClick(ChargePointCluster chargePointCluster) {
        Bundle b = new Bundle();
        b.putSerializable("ChargePoint", chargePointCluster.getChargePoint());
        Navigation.findNavController(root).navigate(R.id.action_navigation_map_to_fragment_buy_power, b);
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
        mapViewModel.setMapCameraPosition(map.getCameraPosition());
    }
}
