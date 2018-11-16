package com.eziosoft.floatzel.Timers;

import com.eziosoft.floatzel.Floatzel;

import java.util.Random;
import java.util.TimerTask;

public class TwitterPoster extends TimerTask {

    public TwitterPoster() {}

    @Override
    public void run(){
        // is the tweetbot on?
        if (!Floatzel.tweeton){
            return;
        }
    }
}
