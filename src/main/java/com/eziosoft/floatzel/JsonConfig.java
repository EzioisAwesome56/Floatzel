package com.eziosoft.floatzel;

import java.util.Arrays;
import java.util.List;

public class JsonConfig {
    // tokens
    String token;
    String devtoken;
    // db config
    String dbUser;
    String dbPass;
    // twitter stuff
    String key;
    String secretkey;
    String access;
    String secretaccess;
    // bot config
    String ownerid;
    String prefix;
    String devprefix;
    // bot admins
    List<String> admins;
    // twitter user ids
    long[] groupa;
    long[] groupb;
    // twitter channels
    String achan;
    String bchan;
    String devchan;
    // twitter toggle
    boolean twittertog;
    // slack shit
    String slackbot;
    boolean enslack;
}
