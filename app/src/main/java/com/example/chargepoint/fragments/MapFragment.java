package com.example.chargepoint.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.map.ChargePointCluster;
import com.example.chargepoint.map.ChargePointClusterRenderer;
import com.example.chargepoint.map.ChargePointInfoWindowAdapter;
import com.example.chargepoint.map.MapSpiderifier;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.utils.PreferenceConfiguration;
import com.example.chargepoint.viewmodel.ChargePointViewModel;
import com.example.chargepoint.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, ClusterManager.OnClusterItemInfoWindowClickListener<ChargePointCluster> {

    private final static String TAG = "CHARGE_MAP";

    private View root;
    private MapViewModel mapViewModel;
    private View progressBar;
    private MapView mapView;
    private GroundOverlay blackOverlay;
    private GoogleMap map;
    private ClusterManager<ChargePointCluster> clusterManager;
    private MapSpiderifier mapSpiderifier;
    private List<ChargePoint> chargePoints;
    private boolean saved = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        ChargePointViewModel cp_vm = new ViewModelProvider(requireActivity()).get(ChargePointViewModel.class);
        cp_vm.getObservableChargePoints().observe(getViewLifecycleOwner(), chargePoints -> {
            this.chargePoints = chargePoints;
            checkIfMapAndDbReady();
        });

        setRetainInstance(true);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // First incarnation of this activity.
        saved = savedInstanceState != null;
        mapView.getMapAsync(this);

        progressBar = view.findViewById(R.id.mapLoading);

        requestLocation();
    }

    @Override
    public void onMapReady(GoogleMap m) {
        this.map = m;


        boolean enabled = PreferenceConfiguration.isNightMode(requireContext());
        if (enabled) {
            try {
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_dark_mode));
                GroundOverlayOptions goo = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.black))
                        .position(mapViewModel.getMapCameraPosition().target, 8600000f, 6500000f);
                blackOverlay = map.addGroundOverlay(goo);

                new Handler().postDelayed(() -> blackOverlay.remove(), 500);
            } catch (Exception ignored) {
            }
        }

        map.getUiSettings().setAllGesturesEnabled(false);

        map.setOnCameraMoveListener(this);

        if (!saved) {
            this.map.moveCamera(CameraUpdateFactory.newCameraPosition(mapViewModel.getMapCameraPosition()));
        }

        if (haveLocationPermission())
            if (map != null) map.setMyLocationEnabled(true);

        clusterManager = new ClusterManager<>(requireContext(), map);

        ChargePointClusterRenderer renderer = new ChargePointClusterRenderer(requireContext(), map, clusterManager);
        clusterManager.setRenderer(renderer);

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new ChargePointInfoWindowAdapter(getContext()));
        map.setInfoWindowAdapter(clusterManager.getMarkerManager());

        mapSpiderifier = new MapSpiderifier(map, clusterManager, getResources().getColor(R.color.textColourHint));
        clusterManager.setOnClusterItemClickListener(mapSpiderifier);
        map.setOnCameraMoveStartedListener(mapSpiderifier);

        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        map.setOnInfoWindowClickListener(clusterManager);

        map.setOnMapClickListener(mapSpiderifier);

        clusterManager.setOnClusterClickListener(cluster -> {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ChargePointCluster chargePointCluster : cluster.getItems()) {
                builder.include(chargePointCluster.getPosition());
            }
            LatLngBounds bounds = builder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
            return true;
        });

        checkIfMapAndDbReady();
    }

    private void checkIfMapAndDbReady() {
        if (clusterManager != null && chargePoints != null)
            addChargePointsToMap();
    }

    private void addChargePointsToMap() {
        for (ChargePoint cp : chargePoints) {
            clusterManager.addItem(new ChargePointCluster(cp.getLocation(), cp.getOperator(), cp));
        }

        clusterManager.cluster();

        progressBar.setVisibility(View.GONE);
        map.getUiSettings().setAllGesturesEnabled(true);
    }

    @Override
    public void onClusterItemInfoWindowClick(ChargePointCluster chargePointCluster) {
        Bundle b = new Bundle();
        b.putSerializable("ChargePoint", chargePointCluster.getChargePoint());
        Navigation.findNavController(root).navigate(R.id.action_navigation_map_to_fragment_buy_power, b);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCameraMove() {
        mapViewModel.setMapCameraPosition(map.getCameraPosition());

        mapSpiderifier.updateCameraPosition(map.getCameraPosition());
    }

    private void requestLocation() {
        if (haveLocationPermission()) {
            if (map != null) map.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, getResources().getInteger(R.integer.LOCATION_REQUEST_CODE));
        }
    }

    private boolean haveLocationPermission() {
        return checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == getResources().getInteger(R.integer.LOCATION_REQUEST_CODE)) {
            for (int gResult : grantResults) {
                if (gResult == PackageManager.PERMISSION_GRANTED)
                    if (map != null)
                        map.setMyLocationEnabled(true);
            }
        }
    }
}
