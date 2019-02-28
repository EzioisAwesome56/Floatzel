package com.eziosoft.floatzel;

import java.util.Arrays;
import java.util.List;

public class JsonConfig {
    // tokens
    private String token;
    private String devtoken;
    // db config
    private String dbUser;
    private String dbPass;
    // twitter stuff
    private String key;
    private String secretkey;
    private String access;
    private String secretaccess;
    // bot config
    private String ownerid;
    private String prefix;
    private String devprefix;
    // bot admins
    private List<String> admins;
    // twitter user ids
    private long[] groupa;
    private long[] groupb;
    // twitter channels
    private String achan;
    private String bchan;
    private String devchan;
    // twitter toggle
    private boolean twittertog;
    // slack shit
    private String slackbot;
    private boolean enslack;

    public void loadDefaults(){
        System.out.println("Loading default configuration data...");
        // tokens
        this.token = "discord token";
        this.devtoken = "Dev. token";
        // db
        this.dbUser = "null";
        this.dbPass = "null";
        // twitter
        this.key = "Twitter api key";
        this.secretkey = "Twitter api secret key";
        this.access = "Twitter api access";
        this.secretaccess = "Twitter api secret access";
        // bot config
        this.ownerid = "Your discord id";
        this.prefix = "&";
        this.devprefix = "&&";
        this.admins = Arrays.asList("your friend's discord id", "2", "3", "4");
        // twitter user ids
        this.groupa = new long[]{1L, 2L, 3L, 4L};
        this.groupb = new long[]{1L, 2L, 3L, 4L};
        // twitter channels
        this.achan = "channel id of where you want groupa tweets to go";
        this.bchan = "channel where you want the rest of them to go";
        this.devchan = "unused, you can leave this blank i guess";
        // twitter toggle
        this.twittertog = false;
        // slack shit
        this.slackbot = "Slack api token (optional)";
        this.enslack = false;
        System.out.println("Default configuration loaded!");
        return;
    }

    // db shit
    public String dbPass(){
        return dbPass;
    }
    public String dbUser(){
        return dbUser;
    }
    public void setDbUser(String h){
        this.dbUser = h;
    }
    public void setDbPass(String h){
        this.dbPass = h;
    }
}
