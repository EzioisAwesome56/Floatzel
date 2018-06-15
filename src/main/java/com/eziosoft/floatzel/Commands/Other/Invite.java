package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Invite extends FCommand {
    public Invite(){
        name = "invite";
        description = "Self-explanitory";
        category = other;
    }

    @Override
    protected void execute(CommandEvent event){
        String msg = "**Wanna join my fucking house?!**\nhttps://discord.gg/VGeACAw\n**Wanna Invite my ass to your server?!!!!!!**\nhttps://discordapp.com/oauth2/authorize?&client_id=339614400526942218&scope=bot&permissions=0";
        event.getChannel().sendMessage(msg).queue();
    }
}
