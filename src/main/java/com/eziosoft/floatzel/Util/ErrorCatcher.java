package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class ErrorCatcher {

    private static Random random = new Random();

    public static void CatchError(String e, CommandEvent event){
        //setup a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String output = null;
        // pick a phrase to slap at the top of the file
        String[] phrases = { "Aw shit!","Damn it ezio, you suck at coding","Crash party, anyone?","Salty is dumb lol",
        "how did you find this?","Stop breaking me, damn it!", "fuck off"};
        int index = random.nextInt(phrases.length);
        output = output + phrases[index] + "\n\n";

    }
}
