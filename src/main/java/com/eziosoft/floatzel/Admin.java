package com.eziosoft.floatzel;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Listeners.MiscListener;
import com.eziosoft.floatzel.Util.TwitterUtils;

public class Admin {

    public static void tweet(){
        // just run the tweetbot
        try {
            TwitterUtils.tweetbot();
        } catch (DatabaseException e){

        }
        return;
    }

    public static boolean tweettog(){
        if(!Floatzel.tweeton){
            Floatzel.tweeton = true;
            return Floatzel.tweeton;
        } else {
            Floatzel.tweeton = false;
            return Floatzel.tweeton;
        }
    }

    public static void setKek(String a){
        MiscListener.kekoff = a;
    }
}
