package com.eziosoft.floatzel.Objects;

public class Stock {

    private int id;
    private String name;
    private int units;
    private int price;
    private int diff;

    public Stock(int id, String name, int units, int price, int diff){
        this.id = id;;
        this.name = name;
        this.units = units;
        this.price  = price;
        this.diff = diff;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getUnits() {
        return units;
    }

    public int getDiff() {
        return diff;
    }
}
