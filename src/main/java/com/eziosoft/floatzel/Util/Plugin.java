package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.LoadPluginException;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
            // load stringview lib
            engine.eval("load('"+ Plugin.class.getResource("/plugin/lib/stringview.js").getFile() +"');");
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
    public static void JSFileSend(String file, CommandEvent e, String filename){
        // first, get a byte array from the string
        e.getChannel().sendFile(Base64.getDecoder().decode(file), filename, null).queue();
    }

    // used to convert byte[] to strings
    public static String attachTostring(Message m){
        try {
            return new String(Base64.getEncoder().encode(IOUtils.toByteArray(m.getAttachments().get(0).getInputStream())));
        } catch (IOException e){
            e.printStackTrace();
            return "fuck";
        }
    }

    // get plugin information for registering
    public static String[] getPluginInfo(String filename) throws FileNotFoundException, LoadPluginException {
        // we dont need to load the entire plugin API, just enough to get the required strings from them
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        // load the plugin file
        try {
            engine.eval(new FileReader("plugins/" + filename + ".js"));
            // load the support library
            engine.eval(new InputStreamReader(Utils.getResourse("/plugin/", "support.js")));
            Invocable runjs = (Invocable) engine;
            // then load the 2 strings needed
            return new String[]{(String) runjs.invokeFunction("getName", ""), (String) runjs.invokeFunction("getHelp", "")};
        } catch (FileNotFoundException e){
            throw new FileNotFoundException();
        } catch (ScriptException | NoSuchMethodException e){
            // throw that shit baby
            throw new LoadPluginException(e.getMessage());
        }
    }
}
