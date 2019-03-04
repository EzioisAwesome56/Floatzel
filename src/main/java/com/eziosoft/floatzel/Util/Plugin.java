package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Plugin {

    public static void runPlugin(CommandEvent event, String name){
        // init the scripting engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, com.eziosoft.floatzel);");
            // give plugins access to shit
            engine.put("event", event);
            engine.put("guild", event.getGuild());
            engine.put("channel", event.getTextChannel());
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
}
