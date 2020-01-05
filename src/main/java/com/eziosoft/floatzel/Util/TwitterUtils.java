package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Res.Phrase;
import com.rethinkdb.net.Cursor;

import java.io.File;
import java.util.List;
import java.util.Random;

public class TwitterUtils {

    private static Random random = new Random();

    private static int index = 0;
    private static String filename = "";
    private static String msg = "";
    private static int errorcode = 0;
    private static int retry = 0;

    public static void tweetbot() throws DatabaseException {
        // is the tweetbot on?
        if (!Floatzel.tweeton) {
            return;
        }
        // get the actual tweet message from database
        Cursor cur = Database.dbgetalltweets();
        // did it fail to load?
        if (Floatzel.fail) {
            Floatzel.fail = false;
            return;
        }
        // cursor -> list conversion
        List tweets = cur.toList();
        int lenght = tweets.size();
        // loop to ensure we actually tweet something
        boolean finished = false;
        while (!finished) {
            // then send the tweet out to the world
            errorcode = TwitterManager.tweet(tweets.get(random.nextInt(lenght)).toString());
            if (errorcode == 187) {
                // just do it again
                retry++;
                errorcode = 0;
                if (retry == 5) {
                    // fuck it
                    finished = true;
            }
        } else {
                finished = true;
            }
        }
    }
}
