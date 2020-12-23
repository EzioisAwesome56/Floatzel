package com.eziosoft.floatzel.Objects;

public interface GenaricDatabase {

    void Conninfo(String info);

    String getProfile(String id);

    void saveProfile(String json);

    void initDatabase();

    boolean checkForUser(String id);

    int totalStocks();

    void makeNewStock(String s);


}