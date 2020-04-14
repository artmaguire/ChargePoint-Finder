package com.example.chargepoint.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Receipt implements Parcelable, Comparable<Receipt> {
    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };
    private Timestamp datetime;
    private double cost;
    private double electricity;
    private String card;
    private String map_id;
    private String invoice_id;

    public Receipt(String invoice_id, double cost, Timestamp datetime, String card, double electricity, String map_id) {
        this.invoice_id = invoice_id;
        this.cost = cost;
        this.datetime = datetime;
        this.card = card;
        this.electricity = electricity;
        this.map_id = map_id;
    }

    public Receipt() {
    }

    private Receipt(Parcel in) {
        invoice_id = in.readString();
        cost = in.readDouble();
        datetime = in.readParcelable(Timestamp.class.getClassLoader());
        card = in.readString();
        electricity = in.readDouble();
        map_id = in.readString();
    }

    public String getInvoice_Id() {
        return invoice_id;
    }

    public void setInvoice_Id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoice_id);
        dest.writeDouble(cost);
        dest.writeParcelable(datetime, flags);
        dest.writeString(card);
        dest.writeDouble(electricity);
        dest.writeString(map_id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Invoice ID: " + invoice_id + "\n"
                + "Cost: " + cost + "\n"
                + "Datetime: " + datetime + "\n"
                + "Card: " + card + "\n"
                + "Electricity: " + electricity + "\n"
                + "Map ID: " + map_id;
    }

    @Override
    public int compareTo(Receipt r) {
        return r.getDatetime().compareTo(datetime);
    }
}
