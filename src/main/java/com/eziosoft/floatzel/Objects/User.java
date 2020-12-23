package com.eziosoft.floatzel.Objects;

public class User {

    private String uid;

    private int bal;

    private long lastloan;

    private Boolean[] perms;

    private int stockid;

    private boolean isAdmin;

    public User(String id, int bal, long lastloan, Boolean[] perms, int stockid, boolean isAdmin){
        this.uid = id;
        this.bal = bal;
        this.lastloan = lastloan;
        this.perms = perms;
        this.stockid = stockid;
        this.isAdmin = isAdmin;
    }
    public User(String id, int bal){
        this.uid = id;
        this.bal = bal;
        this.lastloan = 0L;
        this.perms = new Boolean[]{false, false};
        this.stockid = -1;
        this.isAdmin = false;
    }
    public User(String id, int bal, long lastloan){
        this.uid = id;
        this.bal = bal;
        this.lastloan = lastloan;
        this.perms = new Boolean[]{false, false};
        this.stockid = -1;
        this.isAdmin = false;
    }
    public User(String id, int bal, Boolean[] perms){
        this.uid = id;
        this.bal = bal;
        this.lastloan = 0L;
        this.perms = perms;
        this.stockid = -1;
        this.isAdmin = false;
    }
    public User(String id, int bal, int stockid){
        this.uid = id;
        this.bal = bal;
        this.lastloan = 0L;
        this.perms = new Boolean[]{false, false};
        this.stockid = stockid;
        this.isAdmin = false;
    }
    public User(String id, boolean isAdmin){
        this.uid = id;
        this.bal = bal;
        this.lastloan = 0L;
        this.perms = new Boolean[]{false, false};
        this.stockid = -1;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Boolean[] getPerms() {
        return perms;
    }

    public int getBal() {
        return bal;
    }

    public int getStockid() {
        return stockid;
    }

    public long getLastloan() {
        return lastloan;
    }

    public String getUid() {
        return uid;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setBal(int bal) {
        this.bal = bal;
    }

    public void setLastloan(long lastloan) {
        this.lastloan = lastloan;
    }

    public void setPerms(Boolean[] perms) {
        this.perms = perms;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }
}
