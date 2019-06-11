package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Random extends FCommand {
    public Random(){
        name = "random";
        aliases = Utils.makeAlias("rng");
        description = "Generates a random number";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        java.util.Random rng = new java.util.Random();
        event.reply(Integer.toString(rng.nextInt()));
    }

}
