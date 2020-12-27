package com.eziosoft.floatzel.Objects;

public interface GenaricDatabase {

    void Conninfo(String info);

    String getProfile(String id);

    void saveProfile(String json);

    void initDatabase();

    boolean checkForUser(String id);

    int totalStocks();

    void makeNewStock(String s);

    void updateStock(String s);

    String getStock(int id);

    void makeTable(String name, String key);


}