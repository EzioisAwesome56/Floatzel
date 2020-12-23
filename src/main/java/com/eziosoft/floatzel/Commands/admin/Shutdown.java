package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.concurrent.TimeUnit;

public class Shutdown extends FCommand {
    
    public Shutdown() {
        name = "shutdown";
        ownerCommand = true;
        description = "Shuts down the bot";
        category = owner;
    }
    
    @Override
    protected void cmdrun(CommandEvent commandEvent) {
        if (argsplit[0].equals("-lolxd")) {
            commandEvent.getChannel().sendMessage("\uD83D\uDD2B").queue();
            commandEvent.getChannel().sendMessage("\uD83C\uDDE7 \uD83C\uDDE6 \uD83C\uDDF3 \uD83C\uDDEC").queue(m -> {
                Floatzel.jda.shutdown();
                Floatzel.twitterManager.shutdown();
                System.exit(1);
            });
        } else {
            commandEvent.getChannel().sendMessage("It is time to take a nap").queue();
            commandEvent.getChannel().sendMessage("Goodbye!").queue(m -> {
                Floatzel.jda.shutdown();
                Floatzel.twitterManager.shutdown();
                System.exit(1);
            });
        }
    }
}
