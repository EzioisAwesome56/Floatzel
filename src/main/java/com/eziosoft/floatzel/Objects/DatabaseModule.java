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

    public void makeTable(String name, String key){ driver.makeTable(name, key); }

    public int totalTweets() {return driver.totalTweets(); }

    public void saveTweet(Tweet t){
        driver.saveTweet(gson.toJson(t, Tweet.class));
    }

    public Tweet loadTweet(int id){ return gson.fromJson(driver.loadTweet(id), Tweet.class); }

    public boolean checkForStock(int id){ return driver.checkForStock(id); }

    public void deleteStock(int id){ driver.deleteStock(id); }

    public void setPerm(String uid, int id){ driver.setPerm(uid, id); }
}
