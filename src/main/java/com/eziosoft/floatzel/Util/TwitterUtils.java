package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Floatzel;
import com.rethinkdb.net.Cursor;

import java.util.List;
import java.util.Random;

public class TwitterUtils {

    private static Random random = new Random();

    public static void tweetbot(){
        // is the tweetbot on?
        if (!Floatzel.tweeton){
            return;
        }
        // get all ttweets
        Cursor cur = Database.dbgetalltweets();
        // did it fail to load?
        if (Floatzel.fail){
            Floatzel.fail = false;
            return;
        }
        // get a list from the cursor
        List tweets = cur.toList();
        int lenght = tweets.size();
        // then get a tweet from the list
        int oof = random.nextInt(lenght);
        String tweet = tweets.get(oof).toString();
        // then send the tweet out to the world
        TwitterManager.tweet(tweet);
    }
}
