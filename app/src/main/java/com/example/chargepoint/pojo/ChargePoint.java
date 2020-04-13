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
    private double latitude;
    private double longitude;
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

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    public void setLocation(GeoPoint location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
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

    public String getSimpleAddress() {
        StringBuilder sb = new StringBuilder();
        if (address.containsKey("title"))
            sb.append(address.get("title"));
        else if (address.containsKey("line1"))
            sb.append(address.get("line1"));

        if (sb.length() > 0)
            sb.append(", ");

        if (address.containsKey("county"))
            sb.append(address.get("county"));
        else if (address.containsKey("town"))
            sb.append(address.get("town"));

        return sb.toString();
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
                ", location={lat: " + latitude + ", long: " + longitude + "}" +
                ", address=" + address +
                ", connections=" + connections +
                '}';
    }
}
