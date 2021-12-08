package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.SlashCommands.Objects.GuildSlashSettings;

import javax.annotation.Nullable;

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

    int totalTweets();

    void saveTweet(String s);

    String loadTweet(int id);

    boolean checkForStock(int id);

    void deleteStock(int id);

    void setPerm (String uid, int id);

    void saveSlashGuildSettings(GuildSlashSettings gss);

    GuildSlashSettings[] loadAllSlashSettings();

    void LowLevelDB_Save(Object in, Class<?> type, String table);

    @Nullable
    Object LowLevelDB_Load(String table, String pkey, Class<?> type);

    int LowLevelDB_Count(String table);

    void LowLevelDB_Delete(String table, String pkey);
}