package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.concurrent.TimeUnit;

public class KYS extends FCommand {
    
    public KYS() {
        name = "kys";
        ownerCommand = true;
        description = "Shuts down the bot";
        category = owner;
    }
    
    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage("\uD83D\uDD2B").queue();
        commandEvent.getChannel().sendMessage("\uD83C\uDDE7 \uD83C\uDDE6 \uD83C\uDDF3 \uD83C\uDDEC").queue(m -> {
            Floatzel.jda.shutdown();
            Floatzel.twitterManager.shutdown();
        });
    }
}
