package com.example.chargepoint.map;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSpiderifier implements ClusterManager.OnClusterItemClickListener<ChargePointCluster>, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "MAP_SPIDERIFY";

    private int lineColour = Color.parseColor("#504F4F");

    private GoogleMap map;
    private ClusterManager clusterManager;

    private Map<String, SpiderMarker> spiderMarkers;

    private float zoom = -1;

    public MapSpiderifier(GoogleMap map, ClusterManager clusterManager) {
        this.map = map;
        this.clusterManager = clusterManager;

        this.spiderMarkers = new HashMap<>();
    }

    public MapSpiderifier(GoogleMap map, ClusterManager clusterManager, int lineColor) {
        this(map, clusterManager);
        this.lineColour = lineColor;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        unspiderfy();
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        zoom = map.getCameraPosition().zoom;

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE)
            zoom = map.getCameraPosition().zoom;
    }

    public void updateCameraPosition(CameraPosition cameraPosition) {
        if (zoom == -1) {
            zoom = cameraPosition.zoom;
            return;
        }

        if (zoom != cameraPosition.zoom) {
            unspiderfy();
        }
    }

    @Override
    public boolean onClusterItemClick(ChargePointCluster chargePointCluster) {
        if (spiderMarkers.containsKey(chargePointCluster.getSnippet()))
            return false;
        else
            unspiderfy();

        List<Marker> closeMarkers = new ArrayList<>();
        LatLng cpPosition = chargePointCluster.getPosition();

        final double zoomSpecificSpan = 100 / Math.pow(2, map.getCameraPosition().zoom) / 1.75;

        Bounds b = createBoundsFromSpan(cpPosition, zoomSpecificSpan);

        for (Marker m : clusterManager.getMarkerCollection().getMarkers()) {
            Point p = new Point(m.getPosition().latitude, m.getPosition().longitude);
            if (b.contains(p)) {
                if (!spiderMarkers.containsKey(m.getSnippet()))
                    closeMarkers.add(m);
            }
        }
        // only clicked marker in bound, proceed with normal onclick
        if (closeMarkers.size() == 1)
            return false;

        spiderify(closeMarkers, b);

        return true;
    }

    private void unspiderfy() {
        for (Map.Entry<String, SpiderMarker> pair : spiderMarkers.entrySet()) {
            SpiderMarker spiderMarker = pair.getValue();
            spiderMarker.line.remove();
            animateMarker(spiderMarker, spiderMarker.position, 50, false, true);
        }
    }

    private void spiderify(List<Marker> markers, Bounds b) {
        Point center = calculateCenter(markers);
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(center.x, center.y)), 300, null);

        Collections.sort(markers, (m1, m2) -> {
            Point m1p = normaliseCoordinate(m1.getPosition(), center);
            Point m2p = normaliseCoordinate(m2.getPosition(), center);

            double radians1 = getAngleInRadians(m1p);
            double radians2 = getAngleInRadians(m2p);

            return (int) (radians1 - radians2);
        });

        double width = ((b.minX + b.maxX) / 2) - b.minX;
        width /= 1.2;

        double offset = calculateOffset(center, markers.get(0).getPosition());

        for (int i = 0; i < markers.size(); i++) {
            Marker m = markers.get(i);
            SpiderMarker spiderMarker = new SpiderMarker(m);
            spiderMarkers.put(m.getSnippet(), spiderMarker);

            LatLng newPosition = calculateNewPosition(markers.size(), i, width, offset, m.getPosition());

            animateMarker(spiderMarker, newPosition, 100, true, false);
        }
    }

    private double calculateOffset(Point center, LatLng position) {
        Point normal = new Point(position.latitude - center.x, position.longitude - center.y);

        return getAngleInRadians(normal);
    }

    private Point calculateCenter(List<Marker> markers) {
        double sumLat = 0, sumLong = 0; // Total lat,lng used to calculate average

        for (Marker m : markers) {
            sumLat += m.getPosition().latitude;
            sumLong += m.getPosition().longitude;
        }
        return new Point(sumLat / (double) markers.size(), sumLong / (double) markers.size());
    }

    private Point normaliseCoordinate(LatLng position, Point center) {
        double x = position.latitude - center.x;
        double y = position.longitude - center.y;

        return new Point(x, y);
    }

    private double getAngleInRadians(Point p) {
        double radians = Math.atan2(p.y, p.x);
        if (radians < 1)
            radians = 2 * Math.PI - radians;

        return radians;
    }

    private LatLng calculateNewPosition(int size, int orderPosition, double scale, double offset, LatLng currentPosition) {
        double x = Math.cos((2 * Math.PI / size * orderPosition) + offset) * scale;
        double y = Math.sin((2 * Math.PI / size * orderPosition) + offset) * scale;

        x += currentPosition.latitude;
        y += currentPosition.longitude;

        return new LatLng(x, y);
    }

    private void animateMarker(final SpiderMarker spiderMarker, final LatLng toPosition, final long duration, final boolean drawLine, final boolean remove) {
        final Marker marker = spiderMarker.marker;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;

                LatLng newLatLng = new LatLng(lat, lng);
                if (t >= 1)
                    newLatLng = toPosition;

                marker.setPosition(newLatLng);

                if (drawLine) {
                    if (spiderMarker.line != null)
                        spiderMarker.line.remove();

                    spiderMarker.line = map.addPolyline(new PolylineOptions()
                            .add(startLatLng,
                                    new LatLng(lat, lng))
                            .width(2)
                            .color(lineColour));
                }

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (remove)
                        spiderMarkers.remove(spiderMarker.marker.getSnippet());
                }
            }
        });
    }

    private Bounds createBoundsFromSpan(LatLng p, double span) {
        double halfSpan = span / 2;
        return new Bounds(
                p.latitude - halfSpan, p.latitude + halfSpan,
                p.longitude - halfSpan, p.longitude + halfSpan);
    }

    static class SpiderMarker {
        Marker marker;
        LatLng position;
        Polyline line = null;

        SpiderMarker(Marker marker) {
            this.marker = marker;
            this.position = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        }
    }
}
