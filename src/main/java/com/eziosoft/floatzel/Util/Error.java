package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.rethinkdb.gen.exc.ReqlError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import static com.eziosoft.floatzel.Commands.FCommand.erevent;

public class Error {

    private static Random random = new Random();
    private static StringWriter sw = new StringWriter();

    // handle every execption that could happen
    public static void Catch(ReqlError e){
        e.printStackTrace(new PrintWriter(sw));
        Error.Handle(sw.toString(), e.getMessage());
    }
    public static void Catch(IOException e){
        e.printStackTrace(new PrintWriter(sw));
        Error.Handle(sw.toString(), e.getMessage());
    }
    public static void SpecialError(String a, String b){
        Error.Handle(a, b);
    }

    // actaully form the message and send it
    private static void Handle(String stacktrace, String msg){
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
        // the error report is done being made.
        // write it to a string
        output = builder.toString();
        // then, convert the string into a stream
        try {
            stream.flush();
            stream.write(output.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // okay, its in the stream now
        // send the message
        erevent.getChannel().sendMessage("DAH SHIT! Floatzel has encountered an error! Please look at the ereport.txt file for more " +
                "information!").addFile(stream.toByteArray(), "ereport.txt").queue();
        return;

    }
}
