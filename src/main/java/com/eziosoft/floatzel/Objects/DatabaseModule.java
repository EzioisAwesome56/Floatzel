package com.eziosoft.floatzel.Objects;

import com.google.gson.Gson;

public class DatabaseModule {
    private GenaricDatabase driver;
    private Gson gson = new Gson();

    public DatabaseModule(GenaricDatabase db){
        this.driver = db;
    }

    // private functions for ease of use
    private User convertUserFromJson(String h){
        return gson.fromJson(h, User.class);
    }

    private String convertUserToJson(User u){
        return gson.toJson(u);
    }

    // exposed database drivers
    public User getProfile(String uid){
        return convertUserFromJson(driver.getProfile(uid));
    }

    public void saveProfile(User dank){
        driver.saveProfile(convertUserToJson(dank));
    }

    public void init(){
        driver.initDatabase();
    }

    public boolean checkforuser(String id){
        return driver.checkForUser(id);
    }

    public void sendConninfo(String json){
        driver.Conninfo(json);
    }

    public int totalStocks(){
        return driver.totalStocks();
    }

    public void createNewStock(Stock s){
        driver.makeNewStock(gson.toJson(s));
    }

    public Stock loadStock(int id){
        return gson.fromJson(driver.getStock(id), Stock.class);
    }

    public void updateStock(Stock s){
        driver.updateStock(gson.toJson(s));
    }
}
