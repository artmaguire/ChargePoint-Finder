package com.example.chargepoint.pojo;

import androidx.annotation.NonNull;

/**
 * Created by Art
 */
public class Card {
    private String cardName;
    private String cardNumber;
    private String cardDate;
    private String cardSecurityNumber;
    private String user_id;

    public Card(String cardName, String cardNumber, String cardDate, String cardSecurityNumber, String user_id) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardDate = cardDate;
        this.cardSecurityNumber = cardSecurityNumber;
        this.user_id = user_id;
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
}
