package com.eziosoft.floatzel;

import com.eziosoft.floatzel.Objects.SimpleEmoji;

import java.util.Arrays;
import java.util.List;

public class JsonConfig {
    // tokens
    private String token;
    private String devtoken;
    // db config
    private String dbUser;
    private String dbPass;
    private String hostname;
    private int port;
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
    // custom emoji shit
    private List<SimpleEmoji> loadEmotes;
    private String clientid;

    public void loadDefaults(){
        System.out.println("Loading default configuration data...");
        // tokens
        this.token = "discord token";
        this.devtoken = "Dev. token";
        // db
        this.dbUser = "null";
        this.dbPass = "null";
        this.hostname = "localhost";
        this.port = 2;
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
        this.slackbot = "Slack api token (Serves no purpose)";
        this.enslack = false;
        // emote shit
        this.loadEmotes = Arrays.asList(new SimpleEmoji("put some cool animated emotes here!"));
        this.clientid = "put your bot account client id here";
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
    public int getPort() {
        return port;
    }
    public String getHostname() {
        return hostname;
    }

    // get everything else
    public String getOwnerid() {
        return ownerid;
    }
    public String getPrefix() {
        return prefix;
    }
    public String getDevprefix(){
        return devprefix;
    }
    public String getToken(){
        return this.token;
    }
    public String getDevtoken(){
        return this.devtoken;
    }
    // getting shit for twitter
    public String getKey(){
        return this.key;
    }
    public String getSecretkey(){
        return this.secretkey;
    }
    public String getAccess(){
        return this.access;
    }
    public String getSecretaccess(){
        return this.secretaccess;
    }
    // bot admins
    public List<String> getAdmins(){
        return this.admins;
    }
    // twitter groups
    public long[] getGroupa(){
        return this.groupa;
    }
    public long[] getGroupb(){
        return this.groupb;
    }
    // twitter channels
    public String getAchan(){
        return this.achan;
    }
    public String getBchan(){
        return this.bchan;
    }
    public String getDevchan(){
        return this.devchan;
    }
    // twitter toggle
    public boolean getTwitterTog(){
        return this.twittertog;
    }
    // load emotes
    public List<SimpleEmoji> getLoadEmotes(){ return this.loadEmotes;}
    // get client id
    public String getClientid() { return clientid; }
}
