package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Objects.Tweet;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Res.Phrase;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.List;
import java.util.Random;

public class TwitterUtils {

    private static Random random = new Random();

    public static void tweetbot() throws DatabaseException {
        // is the tweetbot on?
        if (!Floatzel.tweeton) {
            return;
        }
        // first get the total amount of tweets
        int total = Database.dbdriver.totalTweets();
        for (int i = 0; i < 5; i++){
            // get a random tweet
            Tweet t = Database.dbdriver.loadTweet(random.nextInt(total));
            // get error code
            int errorcode = TwitterManager.tweet(t.getText());
            if (errorcode != 187){
                break;
            }
        }
    }
}
