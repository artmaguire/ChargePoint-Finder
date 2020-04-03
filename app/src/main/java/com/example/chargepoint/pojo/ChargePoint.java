package com.example.chargepoint.pojo;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChargePoint implements Serializable {
    private String operator;
    private String usageType;
    private boolean isPayAtLocation;
    private boolean isMembershipRequired;
    private boolean isOperational;
    private int numberOfPoints;
    private GeoPoint location;
    private Map<String, String> address;
    private List<ChargeConnection> connections;

    /*
     * Address consists of {title, line1, town, and county} key,value pairs
     * Not all of these keys might be present for every ChargePoint
     */

    public ChargePoint() {
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public boolean isPayAtLocation() {
        return isPayAtLocation;
    }

    public void setIsPayAtLocation(boolean payAtLocation) {
        isPayAtLocation = payAtLocation;
    }

    public boolean isMembershipRequired() {
        return isMembershipRequired;
    }

    public void setIsMembershipRequired(boolean membershipRequired) {
        isMembershipRequired = membershipRequired;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setIsOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public LatLng getLocationAsLatLng() {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    public List<ChargeConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<ChargeConnection> connections) {
        this.connections = connections;
    }

    @Override
    public String toString() {
        return "ChargePoint{" +
                "operator='" + operator + '\'' +
                ", usageType='" + usageType + '\'' +
                ", isPayAtLocation=" + isPayAtLocation +
                ", isMembershipRequired=" + isMembershipRequired +
                ", isOperational=" + isOperational +
                ", numberOfPoints=" + numberOfPoints +
                ", location=" + location +
                ", address=" + address +
                ", connections=" + connections +
                '}';
    }
}
