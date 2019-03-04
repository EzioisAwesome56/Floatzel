package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Plugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RunPlugin extends FCommand {
    public RunPlugin(){
        name = "run";
        description = "Fucking runs a javascript plugin";
        ownerCommand = true;
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        Plugin.runPlugin(event);
        return;
    }
}
