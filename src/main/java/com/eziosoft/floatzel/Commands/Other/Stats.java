package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.CommdLogic.StatsLogic;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Stats extends FCommand {
    public Stats(){
        name = "stats";
        description = "Displays stats about the bot";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage(StatsLogic.makeMessage(false)).queue();
    }
}
