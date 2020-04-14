package com.example.chargepoint.pojo;

public class Rate {

    private String town;
    private String title;
    private String line;
    private boolean isOp;
    private boolean isFastC;

    public Rate(String town, String title, String line, boolean isOp, boolean isFastC) {
        this.town = town;
        this.title = title;
        this.line = line;
        this.isOp = isOp;
        this.isFastC = isFastC;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean getIsOp() {
        return isOp;
    }

    public void setIsOp(boolean isOp) {
        this.isOp = isOp;
    }

    public boolean getIsFastC() {
        return isFastC;
    }

    public void setIsFastC(boolean isFastC) {
        this.isFastC = isFastC;
    }
}
