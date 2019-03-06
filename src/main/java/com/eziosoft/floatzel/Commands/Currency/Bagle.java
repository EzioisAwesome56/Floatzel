package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Bagle extends FCommand {
    public Bagle(){
        name = "bagle";
        help = "Purchase bagles to eat for 100 french breads";
        category = money;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // considering this command is old as fuck and unfinished, just leave it. lol
        /*String uid = event.getAuthor().getId();
        if (!Database.dbcheckifexist(uid)){
            event.getChannel().sendMessage("Error: you do not have enough fucking money to afford this").queue();
            return;
        }
        int bal = Database.dbloadint(uid);
        if (bal < 100){
            event.getChannel().sendMessage("You dont have enough fucking bread to do this dipshit").queue();
            return;
        }*/
        // buy a bagle

    }

}
