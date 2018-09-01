package com.eziosoft.floatzel.Timers;

import java.util.Random;
import java.util.TimerTask;

public class TwitterPoster extends TimerTask {

    public TwitterPoster() {}

    private Random random = new Random();

    // strings for image-less tweets
    String[] texttweet = {"Did you know, I can speak?!"};

    // strings for tweets with images are below
    // yukari strings
    String[] yukari = {"Man, Yukari's design is hecking awesome"};
    //reverse strings
    String[] uno = {"Ya'll just got REVERSED!"};

    // override timer task's main mathood here
    @Override
    public void run(){
        // do stuff here later
    }
}
