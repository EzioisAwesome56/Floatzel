package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class ErrorCatcher {

    private static Random random = new Random();

    public static void CatchError(String stacktrace, String msg, CommandEvent event){
        // init string builder
        StringBuilder builder = new StringBuilder();
        //setup a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String output = null;
        // pick a phrase to slap at the top of the file
        String[] phrases = { "Aw shit!","Damn it ezio, you suck at coding","Crash party, anyone?","Salty is dumb lol",
        "how did you find this?","Stop breaking me, damn it!", "fuck off"};
        int index = random.nextInt(phrases.length);
        builder.append(phrases[index] + "\n\n");
        // boring text
        builder.append("Floatzel has ecountered an error. Detailed error informtion will be provided below\n\n");
        // add the error details
        builder.append("Error message:\n"+msg+"\n\nStack Trace:\n");
        builder.append(stacktrace);


    }
}
