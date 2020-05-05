package com.example.chargepoint.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Card implements Parcelable {
    private String cardName;
    private String cardNumber;
    private String cardDate;
    private String cardSecurityNumber;
    private String user_id;

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public Card(String cardName, String cardNumber, String cardDate, String cardSecurityNumber, String user_id) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardDate = cardDate;
        this.cardSecurityNumber = cardSecurityNumber;
        this.user_id = user_id;
    }

    public Card() {
    }

    protected Card(Parcel in) {
        cardName = in.readString();
        cardNumber = in.readString();
        cardDate = in.readString();
        cardSecurityNumber = in.readString();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public String getCardSecurityNumber() {
        return cardSecurityNumber;
    }

    public void setCardSecurityNumber(String cardSecurityNumber) {
        this.cardSecurityNumber = cardSecurityNumber;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + cardName + "\nNumber: " + cardNumber + "\nDate: " + cardDate + "\nSecurity Number: " + cardSecurityNumber;
    }

    public boolean equals(Card card) {
        return cardName.equals(card.getCardName()) && cardDate.equals(card.getCardDate()) && cardNumber.equals(card.getCardNumber()) && cardSecurityNumber
                .equals(card.getCardSecurityNumber());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardName);
        dest.writeString(cardNumber);
        dest.writeString(cardDate);
        dest.writeString(cardSecurityNumber);
    }
}
