package com.eziosoft.floatzel.Commands.PayForCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BetterLoan extends FCommand {
    public BetterLoan(){
        name = "betterloan";
        description = "Go to a better fucking bank and take out a loan";
        category = money;
    }

    @Override
    protected void execute(CommandEvent event){
        String uid = event.getAuthor().getId();
        //check if they havent bought the command yet
        if (!Database.dbcheckbloan(uid)){
            event.getChannel().sendMessage("You didnt fucking buy this command yet jackass!").queue();
            return;
        }
        event.getChannel().sendMessage("Rest of command goes here").queue();
    }
}