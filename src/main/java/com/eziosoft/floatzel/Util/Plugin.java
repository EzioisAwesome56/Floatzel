package com.eziosoft.floatzel.Util;

import com.jagrosh.jdautilities.command.CommandEvent;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Plugin {

    public static void runPlugin(CommandEvent event){
        // init the scripting engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try{
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, com.eziosoft.floatzel);");
            // give plugins access to shit
            engine.put("event", event);
            engine.put("guild", event.getGuild());
            engine.put("channel", event.getTextChannel());
            // load the plugin file
            engine.eval(new FileReader("plugins/test.js"));
            Invocable runjs = (Invocable) engine;
            // try running the plugin
            runjs.invokeFunction("run", "");
        } catch (ScriptException | FileNotFoundException | NoSuchMethodException e){
            // until i can handle errors better, just print stack trace
            e.printStackTrace();
            return;
        }
    }
}
