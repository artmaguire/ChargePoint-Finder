package com.example.chargepoint.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChargePoint implements Serializable, Parcelable {

    private String map_id;
    private String operator;
    private String usageType;
    private boolean isPayAtLocation;
    private boolean isMembershipRequired;
    private boolean isOperational;
    private boolean isFastC;
    private int numberOfPoints;
    private double latitude;
    private double longitude;
    private Map<String, String> address = new HashMap<>();
    private List<ChargeConnection> connections;
    private float kw;
    private int voltage;
    private int amps;

    public ChargePoint() {
    }

    public ChargePoint(String map_id, boolean isOperational, boolean isFastC, Map<String, String> address, String operator, float kw, int voltage, int amps) {
        this.map_id = map_id;
        this.isOperational = isOperational;
        this.isFastC = isFastC;
        this.address = address;
        this.operator = operator;
        this.kw = kw;
        this.voltage = voltage;
        this.amps = amps;
    }

    protected ChargePoint(Parcel in) {
        map_id = in.readString();
        operator = in.readString();
        usageType = in.readString();
        isPayAtLocation = in.readByte() != 0;
        isMembershipRequired = in.readByte() != 0;
        isOperational = in.readByte() != 0;
        isFastC = in.readByte() != 0;
        numberOfPoints = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        kw = in.readFloat();
        voltage = in.readInt();
        amps = in.readInt();
    }

    public static final Creator<ChargePoint> CREATOR = new Creator<ChargePoint>() {
        @Override
        public ChargePoint createFromParcel(Parcel in) {
            return new ChargePoint(in);
        }

        @Override
        public ChargePoint[] newArray(int size) {
            return new ChargePoint[size];
        }
    };

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
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

    @PropertyName("isPayAtLocation")
    public boolean isPayAtLocation() {
        return isPayAtLocation;
    }

    @PropertyName("isPayAtLocation")
    public void setPayAtLocation(boolean payAtLocation) {
        isPayAtLocation = payAtLocation;
    }

    @PropertyName("isMembershipRequired")
    public boolean isMembershipRequired() {
        return isMembershipRequired;
    }

    @PropertyName("isMembershipRequired")
    public void setMembershipRequired(boolean membershipRequired) {
        isMembershipRequired = membershipRequired;
    }

    @PropertyName("isOperational")
    public boolean isOperational() {
        return isOperational;
    }

    @PropertyName("isOperational")
    public void setOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }

    @PropertyName("isFastC")
    public boolean isFastC() {
        return isFastC;
    }

    @PropertyName("isFastC")
    public void setFastC(boolean isFastC) {
        this.isFastC = isFastC;
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

    public float getKw() {
        return kw;
    }

    public int getVoltage() {
        return voltage;
    }

    public int getAmps() {
        return amps;
    }

    public void setKw(float kw) {
        this.kw = kw;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public void setAmps(int amps) {
        this.amps = amps;
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

    @NonNull
    @Override
    public String toString() {
        return "ChargePoint{" + "map_id='" + map_id + '\'' + "operator='" + operator + '\'' + ", usageType='" + usageType + '\'' + ", isPayAtLocation=" + isPayAtLocation + ", isMembershipRequired=" + isMembershipRequired + ", isOperational=" + isOperational + ", numberOfPoints=" + numberOfPoints + ", location={lat: " + latitude + ", long: " + longitude + "}" + ", address=" + address + ", connections=" + connections + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(map_id);
        dest.writeString(operator);
        dest.writeString(usageType);
        dest.writeBoolean(isPayAtLocation);
        dest.writeBoolean(isMembershipRequired);
        dest.writeBoolean(isOperational);
        dest.writeBoolean(isFastC);
        dest.writeInt(numberOfPoints);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeMap(address);
        dest.writeList(connections);
        dest.writeList(Collections.singletonList(kw));
        dest.writeList(Collections.singletonList(voltage));
        dest.writeList(Collections.singletonList(amps));
    }
}
