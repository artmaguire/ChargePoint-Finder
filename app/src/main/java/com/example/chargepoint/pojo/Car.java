package com.example.chargepoint.pojo;

public class Car {
    private String make;
    private String model;
    private String user_id;

    public Car(String make, String model, String user_id) {
        this.make = make;
        this.model = model;
        this.user_id = user_id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
