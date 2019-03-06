package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Plugin {

    public static void runPlugin(CommandEvent event, String name){
        // init the scripting engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, com.eziosoft.floatzel, org.apache.commons.io.IOUtils);");
            // give plugins access to shit
            engine.put("event", event);
            engine.put("guild", event.getGuild());
            engine.put("channel", event.getTextChannel());
            engine.put("message", event.getMessage());
            // load jvm-npm
            engine.eval("load('"+ Plugin.class.getResource("/plugin/lib/jvm-npm.js").getFile() + "');");
            // load polyfill.js
            engine.eval(new InputStreamReader(Utils.getResourse("/plugin/lib/", "polyfill.js")));
            // load 2 things for text decoding
            engine.eval("load('https://unpkg.com/text-encoding@0.6.4/lib/encoding-indexes.js');");
            engine.eval("load('https://unpkg.com/text-encoding@0.6.4/lib/encoding.js');");
            // load plugin api utils
            engine.eval(new InputStreamReader(Utils.getResourse("/plugin/", "util.js")));
            // load the plugin file
            engine.eval(new FileReader("plugins/" + name + ".js"));
            // load in Plugin api support file
            engine.eval(new InputStreamReader(Utils.getResourse("/plugin/", "support.js")));
            Invocable runjs = (Invocable) engine;

            // permission checking
            if (!(boolean) runjs.invokeFunction("checkPermission", "")) {
                // check if required permission is bot admin
                if ((boolean) runjs.invokeFunction("isAdmin", "")) {
                    if (!Utils.isAdmin(event.getAuthor().getId())) {
                        event.getChannel().sendMessage("Error: you are not a fucking bot admin! You cannot run this plugin!").queue();
                        return;
                    }
                } else if ((boolean) runjs.invokeFunction("isOwner", "")) {
                    if (!event.isOwner()) {
                        event.getChannel().sendMessage("Error: you arent a fucking bot owner and cant run this plugin!").queue();
                        return;
                    }
                }
            }

            // try running the plugin
            runjs.invokeFunction("run", "");
        } catch (FileNotFoundException e){
            event.getChannel().sendMessage("The fucking plugin you tired to run doesnt exist asswipe!").queue();
            return;
        } catch (ScriptException| NoSuchMethodException e){
            // until i can handle errors better, just print stack trace
            e.printStackTrace();
            return;
        }
    }

    // used to convert strings produced by js back to byte[]
    public static byte[] stringToByte(String file){
        // first, get a byte array from the string
        byte[] a = file.getBytes(StandardCharsets.ISO_8859_1);
        return a;
    }

    // used to convert byte[] to strings
    public static String attachTostring(CommandEvent event){
        try {
            return new String(IOUtils.toByteArray(event.getMessage().getAttachments().get(0).getInputStream()), StandardCharsets.ISO_8859_1);
        } catch (IOException e){
            e.printStackTrace();
            return "fuck";
        }
    }
}
