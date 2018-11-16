package com.eziosoft.floatzel.Timers;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.TwitterUtils;
import com.eziosoft.floatzel.Util.Utils;
import com.rethinkdb.net.Cursor;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class TwitterPoster extends TimerTask {

    public TwitterPoster() {}

    private Random random = new Random();

    @Override
    public void run(){
        // just call the tweetbot in utils
        TwitterUtils.tweetbot();
    }
}
