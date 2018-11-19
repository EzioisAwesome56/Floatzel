package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Floof extends FCommand {
    public Floof(){
        name = "floof";
        help = "Floof up your day!";
        category =  other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // do a thing
    }
}
