package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.xml.crypto.Data;

public class Bal extends FCommand {
    public Bal(){
        name = "bal";
        description = "View how much money you have";
        category = money;
    }

    @Override
    protected void execute(CommandEvent event){
        // check to see if the user alright has a db entry
        Boolean exists = Database.dbcheckifexist(event.getMessage().getAuthor().getId().toString());
        if (!exists){
            event.getChannel().sendMessage("You have 0 \uD83E\uDD56").queue();
            return;
        } else {
            int bal = Database.dbloadint(event.getMessage().getAuthor().getId().toString());
            String balmsg = Integer.toString(bal);
            event.getChannel().sendMessage("You have "+balmsg+"\uD83E\uDD56").queue();
            return;
        }
    }
}
