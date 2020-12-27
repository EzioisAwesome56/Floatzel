package com.eziosoft.floatzel.Objects;

public class Tweet {

    private int id;
    private String text;

    public Tweet(String text, int id){
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
