package com.example.chargepoint.pojo;

import java.io.Serializable;

public class ChargeConnection implements Serializable {
    private String title;
    private int amps;
    private int voltage;
    private float powerKW;
    private int quantity;
    private boolean isFastChargeCapable;
    private boolean isOperational;
    private String currentTitle;

    /*
     * amps, voltage, and powerKW are not known for every ChargeConnection,
     * in which case they have the value of '-1', check is needed for optional display
     */

    public ChargeConnection() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmps() {
        return amps;
    }

    public void setAmps(int amps) {
        this.amps = amps;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public float getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(float powerKW) {
        this.powerKW = powerKW;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFastChargeCapable() {
        return isFastChargeCapable;
    }

    public void setFastChargeCapable(boolean fastChargeCapable) {
        isFastChargeCapable = fastChargeCapable;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setOperational(boolean operational) {
        isOperational = operational;
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    @Override
    public String toString() {
        return "ChargeConnection{" +
                "title='" + title + '\'' +
                ", amps=" + amps +
                ", voltage=" + voltage +
                ", powerKW=" + powerKW +
                ", quantity=" + quantity +
                ", isFastChargeCapable=" + isFastChargeCapable +
                ", isOperational=" + isOperational +
                ", currentTitle='" + currentTitle + '\'' +
                '}';
    }
}
