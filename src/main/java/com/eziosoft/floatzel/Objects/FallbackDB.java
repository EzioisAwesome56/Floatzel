package com.eziosoft.floatzel.Objects;

import com.google.gson.Gson;
import org.omg.CORBA.PRIVATE_MEMBER;

public class FallbackDB implements GenaricDatabase{

    private Gson g = new Gson();

    @Override
    public void Conninfo(String info) {
        return;
    }

    @Override
    public String getProfile(String id) {
        return g.toJson(new User("2", 12));
    }

    @Override
    public void saveProfile(String json) {
        return;
    }

    @Override
    public void initDatabase() {
        return;
    }

    @Override
    public boolean checkForUser(String id) {
        return false;
    }

    @Override
    public int totalStocks() {
        return 0;
    }

    @Override
    public void makeNewStock(String s) {
        return;
    }

    @Override
    public void updateStock(String s) {
        return;
    }

    @Override
    public String getStock(int id) {
        return null;
    }

    @Override
    public void makeTable(String name, String key) {
        return;
    }

    @Override
    public int totalTweets() {
        return 0;
    }

    @Override
    public void saveTweet(String s) {
        return;
    }

    @Override
    public String loadTweet(int id) {
        return null;
    }

    @Override
    public boolean checkForStock(int id) {
        return false;
    }

    @Override
    public void deleteStock(int id) {
        return;
    }

    @Override
    public void setPerm(String uid, int id) {
        return;
    }
}