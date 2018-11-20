package com.eziosoft.floatzel.Util;

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

    public static void tweetbot(){
        // is the tweetbot on?
        if (!Floatzel.tweeton){
            return;
        }
        // generate an integer to pick what type of tweet to tweet
        int type = random.nextInt(3);
        if (type == 0) {
            // get all ttweets
            Cursor cur = Database.dbgetalltweets();
            // did it fail to load?
            if (Floatzel.fail) {
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
        } else if (type == 1){
            // grab a ralsei from the list of filenames
            index = random.nextInt(Files.floof.length);
            filename = "/floof/" + Files.floof[index];
            File image = new File(Floatzel.class.getResource(filename).getFile());
            // then get a message from a small list
            index = random.nextInt(Phrase.floofword.length);
            msg = Phrase.floofword[index];
            // then pass it to the tweet handler
            errorcode = TwitterManager.tweet(msg, image);
        } else if (type == 2){
            // copy pasta
            index = random.nextInt(Files.unocards.length);
            filename = "/uno/" + Files.unocards[index];
            File image = new File(Floatzel.class.getResource(filename).getFile());
            // then get a message from a small list
           msg = "OMG UNO!!!!!!";
            // then pass it to the tweet handler
            errorcode = TwitterManager.tweet(msg, image);
        }
        // check if its a duplicate
        if (errorcode == 187){
            Error.ReportDupe("ERROR CODE "+Integer.toString(errorcode));
            // reset errorcode to 0
            errorcode = 0;
            // then recall the function
            tweetbot();
            return;
        }
    }
}
