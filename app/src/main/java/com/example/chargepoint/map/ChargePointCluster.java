package com.example.chargepoint.map;

import com.example.chargepoint.pojo.ChargePoint;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ChargePointCluster implements ClusterItem {

    private final LatLng latLng;
    private final String title;
    private final ChargePoint chargePoint;

    public ChargePointCluster(LatLng latLng, String title, ChargePoint chargePoint) {
        this.latLng = latLng;
        this.title = title;
        this.chargePoint = chargePoint;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return "";
    }

    public ChargePoint getChargePoint() {
        return chargePoint;
    }
}
