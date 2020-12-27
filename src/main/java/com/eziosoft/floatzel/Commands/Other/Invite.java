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
    protected void cmdrun(CommandEvent event){
        String msg = "**Support Server:**\nhttps://discord.gg/VGeACAw\n**Bot invite link:**\nhttps://discordapp.com/oauth2/authorize?&client_id=339614400526942218&scope=bot&permissions=0";
        event.getChannel().sendMessage(msg).queue();
    }
}
