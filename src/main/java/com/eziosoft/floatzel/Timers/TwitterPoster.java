package com.eziosoft.floatzel.Timers;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import com.rethinkdb.net.Cursor;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class TwitterPoster extends TimerTask {

    public TwitterPoster() {}

    private Random random = new Random();

    @Override
    public void run(){
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

    }
}
