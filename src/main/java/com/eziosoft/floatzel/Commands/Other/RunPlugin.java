package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Plugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RunPlugin extends FCommand {
    public RunPlugin(){
        name = "run";
        description = "Fucking runs a javascript plugin";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // check to make sure they gave arguments
        if (event.getArgs().length() < 1){
            event.getChannel().sendMessage("Fuckhead! You didnt provide the name of the plugin you want to run!").queue();
            return;
        }
        Plugin.runPlugin(event, argsplit[0]);
        return;
    }
}
