package com.eziosoft.floatzel.Commands.Owner;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Eval extends FCommand {
    public Eval(){
        name = "eval";
        help = "runs provided statement";
        ownerCommand = true;
        category = owner;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String code = event.getArgs();
        if (code.length() < 0){
            event.getChannel().sendMessage("Error: invalid arguments!").queue();
            return;
        }
        // enable the scripting manager
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try{
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, com.eziosoft.floatzel);");
        } catch (ScriptException e){
            Error.Catch(e);
            return;
        }
    }
}
