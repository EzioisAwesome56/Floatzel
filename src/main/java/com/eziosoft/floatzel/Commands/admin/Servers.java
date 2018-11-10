package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Servers  extends FCommand {
    public Servers(){
        name = "servers";
        description = "list all servers the bot is in";
        ownerCommand = true;
        category = new Category("Owner Commands");
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // start by getting the guilds
        String serv = Floatzel.jda.getGuilds().toString();
        event.getChannel().sendMessage("Floatzel is on the following servers:\n" + serv).queue();
    }
}
