package com.example.chargepoint.pojo;

public class Rate {

    private String rateName;
    private String a;
    private String b;
    private String c;

    public Rate(String rateName, String a, String b, String c) {
        this.rateName = rateName;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public String getRateA() {
        return a;
    }

    public void setRateA(String a) {
        this.a = a;
    }

    public String getRateB() {
        return b;
    }

    public void setRateB(String b) {
        this.b = b;
    }

    public String getRateC() {
        return c;
    }

    public void setRateC(String c) {
        this.c = c;
    }


}
