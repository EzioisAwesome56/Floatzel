package com.eziosoft.floatzel.Objects;

public class Stock {

    private int id;
    private String name;
    private int units;
    private int price;

    public Stock(int id, String name, int units, int price){
        this.id = id;;
        this.name = name;
        this.units = units;
        this.price  = price;
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
}
