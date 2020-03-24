package com.example.chargepoint;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Receipt {
    private String invoiceID;
    private double euro;
    private Timestamp datetime;
    private GeoPoint location;
    private String payment;
    private double electricity;

    public Receipt(String invoiceID, double euro, Timestamp datetime, GeoPoint location, String payment, double electricity) {
        this.invoiceID = invoiceID;
        this.euro = euro;
        this.datetime = datetime;
        this.location = location;
        this.payment = payment;
        this.electricity = electricity;
    }

    public Receipt() {
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public double getEuro() {
        return euro;
    }

    public void setEuro(double euro) {
        this.euro = euro;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }
}
