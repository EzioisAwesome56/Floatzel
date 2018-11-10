package com.eziosoft.floatzel.Commands.Debug;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Debug extends FCommand {
    public Debug(){
        name = "debug";
        help = "Debugs whatever is coded in";
        category = owner;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // stuff
    }
}
