package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.codehaus.plexus.util.ExceptionUtils;
import twitter4j.TwitterException;

import javax.script.ScriptException;
import java.io.*;
import java.util.Random;

import static com.eziosoft.floatzel.Commands.FCommand.erevent;

public class Error {

    private static Random random = new Random();

    public static void SpecialError(String a, String b, CommandEvent event){
        Error.Handle(a, b, event);
    }
    public static void Catch(Throwable e, CommandEvent event){
        Error.Handle(ExceptionUtils.getStackTrace(e), e.getMessage(), event);
    }

    // until i find out how rethrowing from inside a lambada works, we need this
    public static void CatchOld(Throwable e){
        Error.handAnything(ExceptionUtils.getStackTrace(e), e.getMessage());
    }
    public static void CatchTweet(Exception e){
        Error.handtweet(ExceptionUtils.getStackTrace(e), e.getMessage());
    }
    // thing to log repeat tweets
    public static void ReportDupe(String msg){
        Error.handtweet("Twitter returned error code. See above", msg);
    }
    // actaully form the message and send it
    private static void Handle(String stacktrace, String msg, CommandEvent event){
        // New line character, dependant on OS.
        String endl = System.getProperty("line.separator");

        StringBuilder builder = new StringBuilder();

        // pick a phrase to slap at the top of the file
        String[] phrases = {"is this a bug?", "nah nah nah", "COSMIC RADIATION?", "my bread died :C", "brov", "huh, thats weird"};
        int index = random.nextInt(phrases.length);
        builder.append(phrases[index]).append(endl+endl);
        // boring text
        builder.append("Floatzel has ecountered an error. Detailed error information will be provided below").append(endl+endl);
        // add the error details
        builder.append("Error message:"+endl+msg+endl+endl+"Stack Trace:"+endl);
        builder.append(stacktrace);
        // the error report is done being made.

        // send the message
        try {
            event.getChannel().sendMessage("DAH HECK! Floatzel has encountered an error! Please look at the ereport.txt file for more " +
                    "information!").addFile(builder.toString().getBytes("UTF-8"), "ereport.txt").queue();
        } catch (UnsupportedEncodingException e) {
            //This will never be thrown, but java won't stop yelling at us if we don't try to catch it.
            e.printStackTrace();
        }
        return;

    }

    private static void handtweet(String stack, String msg){
        // New line character, dependant on OS.
        String endl = System.getProperty("line.separator");

        // basically its just handle but for sending to the tweet errors
        // init string builder
        StringBuilder builder = new StringBuilder();

        // pick a phrase to slap at the top of the file
        String[] phrases = {"twitter machine broke", "hecking twitter", "twitter sucks", "why", "i hate u twitter"};
        int index = random.nextInt(phrases.length);
        builder.append(phrases[index] + "\n\n");
        // boring text
        builder.append("Floatzel has encountered a twitter error. Detailed error information will be provided below\n\n");
        // add the error details
        builder.append("Error message:\n"+msg+"\n\nStack Trace:\n");
        builder.append(stack);
        // the error report is done being made.

        // send the message
        try {
            Floatzel.jda.getTextChannelById("512787662344814622").sendMessage("Floatzel ran into a error while tweeting and stopped trying. Check Error report below").addFile(builder.toString().getBytes("UTF-8"), "twittererror.txt").queue();
        } catch (UnsupportedEncodingException e) {
            //This will never be thrown, but java won't stop yelling at us if we don't try to catch it.
            e.printStackTrace();
        }
        return;

    }

    private static void handAnything(String stack, String msg){
        // New line character, dependant on OS.
        String endl = System.getProperty("line.separator");

        // basically its just handle but for sending to the tweet errors
        // init string builder
        StringBuilder builder = new StringBuilder();

        // pick a phrase to slap at the top of the file
        String[] phrases = {"oof", "oof", "oof", "oof", "oof", "oof"};
        int index = random.nextInt(phrases.length);
        builder.append(phrases[index] + "\n\n");
        // boring text
        builder.append("Floatzel has encountered a error somewhere!\n\n");
        // add the error details
        builder.append("Error message:\n"+msg+"\n\nStack Trace:\n");
        builder.append(stack);
        // the error report is done being made.

        // send the message
        try {
            Floatzel.jda.getTextChannelById("512787662344814622").sendMessage("Floatzel ran into an Error somewhere that couldnt be thrown out of. Report below").addFile(builder.toString().getBytes("UTF-8"), "error.txt").queue();
        } catch (UnsupportedEncodingException e) {
            //This will never be thrown, but java won't stop yelling at us if we don't try to catch it.
            e.printStackTrace();
        }
        return;

    }
}
