package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.rethinkdb.gen.exc.ReqlError;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class Check extends FCommand {
    public Check(){
        name = "check";
        description = "check's the bank account of another user";
        category = money;
    }



    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        // get mentions, if any
        List<User> mentions = event.getMessage().getMentionedUsers();
        if (mentions.isEmpty()){
            event.getChannel().sendMessage("You didn't mention anyone!").queue();
            return;
        }
        // get the first user mentioned
        String user = mentions.get(0).getId();
        // check if they dont have a bank account at all
        if (!Database.dbcheckifexist(user)){
            System.out.println("New bank account created for "+mentions.get(0).getName());
            // if they dont have a bank account, why waste time trying to query sql when we know its bloody 0
            event.getChannel().sendMessage("User "+mentions.get(0).getName()+" has 0 "+moneyicon).queue();
            return;
        }
        // if we are here, they clearly do have a bank account
        // go get their balanace from the databse
        int bal = Database.dbloadint(user);
        event.getChannel().sendMessage("user "+mentions.get(0).getName()+" has "+Integer.toString(bal)+" "+moneyicon).queue();
        // and thats all folks
        return;
    }
}
