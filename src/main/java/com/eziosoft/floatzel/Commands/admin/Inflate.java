package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.xml.crypto.Data;

public class Inflate extends FCommand {
    public Inflate(){
        name = "inflate";
        description = "inflates your wallet";
        ownerCommand = true;
        category = owner;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage("inflating your wallet, please wait...").queue();
        // first: load the current amount of money
        int curbal = Database.dbloadint(event.getMessage().getAuthor().getId().toString());
        // then add 10 to that
        int bal = Integer.MAX_VALUE;
        // convert it to string
        String newbal = Integer.toString(bal);
        // save the file
        Database.dbsave(event.getMessage().getAuthor().getId().toString(), newbal);
        event.getChannel().sendMessage("Wallet inflated!").queue();
    }
}
