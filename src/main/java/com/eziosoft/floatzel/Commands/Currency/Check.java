package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class Check extends FCommand {
    public Check(){
        name = "check";
        description = "check's the bank account of another user";
        category = money;
    }



    @Override
    protected void execute(CommandEvent event) {
        // get mentions, if any
        List<User> mentions = event.getMessage().getMentionedUsers();
        if (mentions.isEmpty()){
            event.getChannel().sendMessage("You didn't mention anyone!").queue();
            return;
        }

    }
}
