package com.eziosoft.floatzel;

import java.util.Arrays;
import java.util.List;

public class ConfigStock {
    // discord tokens
    public static String token = "";
    public static String devToken = "";
    // twitter tokens
    public static String key = "";
    public static String secretkey = "";
    public static String access = "";
    public static String secretaccess = "";
    // bot configuration
    public static String ownerid = "";
    public static String prefix = "&";
    public static String devprefix = "&&";
    // bot admin
    public static List<String> admins = Arrays.asList("");
    // twitter user ids
    public static long[] groupa = new long[]{};
    public static long[] groupb = new long[]{};
    // twitter channels
    public static String achan = "";
    public static String bchan = "";
    public static String devchan = "";
    // twitter enable switch
    public static boolean twittertog = false;
    // use old database toggle
    public static boolean olddb = false;
    // SLACK: bot token
    public static String slackbot = "";
}