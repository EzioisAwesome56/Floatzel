package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Avatar extends FCommand {
    public Avatar(){
        name = "avatar";
        aliases = Utils.makeAlias("ava");
        description = "get the avatar of a mentioned user!";
        category = other;
    }


    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        // did they mention a user?
        if (event.getMessage().getMentionedUsers().isEmpty()){
            event.reply("You did not mention the user who you want their avatar!");
            return;
        }
        event.reply(event.getMessage().getMentionedUsers().get(0).getAvatarUrl());
    }
}
