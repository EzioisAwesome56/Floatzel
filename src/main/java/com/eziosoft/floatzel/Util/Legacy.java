package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

public class Legacy {

    // legacy code that was for a plugin previously exists here now
    // used to convert strings produced by js back to byte[]
    public static void JSFileSend(String file, CommandEvent e, String filename){
        // first, get a byte array from the string
        e.getChannel().sendFile(Base64.getDecoder().decode(file), filename).queue();
    }

    // used to convert byte[] to strings
    public static String attachTostring(Message m){
        try {
            return new String(Base64.getEncoder().encode(IOUtils.toByteArray(m.getAttachments().get(0).retrieveInputStream().get())));
        } catch (IOException | ExecutionException | InterruptedException e){
            e.printStackTrace();
            return "fuck";
        }
    }
}
