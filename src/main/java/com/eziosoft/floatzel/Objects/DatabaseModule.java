package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.SlashCommands.Objects.GuildSlashSettings;
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

    // exposed database driver functions

    /**
     * gets a user profile from the database
     * @param uid
     * @return User
     */
    public User getProfile(String uid){
        return convertUserFromJson(this.driver.getProfile(uid));
    }

    /**
     * saves a profile to the database
     * @param dank- user object to save
     */
    public void saveProfile(User dank){
        this.driver.saveProfile(convertUserToJson(dank));
    }

    /**
     * inits the database driver that was loaded (duh)
     */
    public void init(){
        this.driver.initDatabase();
    }

    /**
     * checks if a user exists in the database
     * @param id
     * @return
     */
    public boolean checkforuser(String id){
        return this.driver.checkForUser(id);
    }

    /**
     * sends connection info to the database in form of json
     * @param json
     */
    public void sendConninfo(String json){
        this.driver.Conninfo(json);
    }

    /**
     * gets the total amount of stocks currently stored in the database
     * @return int
     */
    public int totalStocks(){
        return this.driver.totalStocks();
    }

    /**
     * creates a new stock in the database
     * @param s- stock object that you would like to store
     */
    public void createNewStock(Stock s){
        this.driver.makeNewStock(gson.toJson(s));
    }

    /**
     * loads a stock object from the database
     * @param id- id of stock to load
     * @return stock object
     */
    public Stock loadStock(int id){
        return gson.fromJson(this.driver.getStock(id), Stock.class);
    }

    /**
     * updates a stock in the database- basically resaves it
     * @param s- stock object to update
     */
    public void updateStock(Stock s){
        this.driver.updateStock(gson.toJson(s));
    }

    /**
     * self explanatory- makes a new table in the database
     * @param name- table name
     * @param key- primary key-may be optional depending on the driver
     */
    public void makeTable(String name, String key){ this.driver.makeTable(name, key); }

    /**
     * gets the total number of tweets currently stored in the database
     * @return int
     */
    public int totalTweets() {return this.driver.totalTweets(); }

    /**
     * saves a tweet to the database
     * @param t- tweet to store in the database
     */
    public void saveTweet(Tweet t){
        this.driver.saveTweet(gson.toJson(t, Tweet.class));
    }

    /**
     * loads a tweet from the database by its id number
     * @param id- tweet id, must be int
     * @return tweet
     */
    public Tweet loadTweet(int id){ return gson.fromJson(this.driver.loadTweet(id), Tweet.class); }

    /**
     * checks to see if a stock exists in the database
     * @param id- stock id int
     * @return boolean
     */
    public boolean checkForStock(int id){ return this.driver.checkForStock(id); }

    /**
     * deletes a stock from the database
     * @param id- stock id int
     */
    public void deleteStock(int id){ this.driver.deleteStock(id); }

    /**
     * sets a permission in the database for a user
     * @param uid- user id for who you're setting permissions for
     * @param id- permission id
     */
    public void setPerm(String uid, int id){ this.driver.setPerm(uid, id); }

    /**
     * save a guild's registered slash commands
     * @param gss- guildslashsettings object
     */
    public void saveGuildSlashSettings(GuildSlashSettings gss){
        this.driver.saveSlashGuildSettings(gss);
    }

    /**
     * loads all slashsettings from the database
     * @return guildslashsettings array
     */
    public GuildSlashSettings[] loadAllSlashSettings(){
        return this.driver.loadAllSlashSettings();
    }
}
