package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.TwitterManager;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Tweet extends FCommand {
    public Tweet(){
        name = "tweet";
        description = "for debugging purposes: sends a tweet";
        adminCommand = true;
        category = admin;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // i guess just try sending whatever the args are
        String args = event.getArgs();
        TwitterManager.tweet(args);
        event.reply("Tweet should have been sent. If it wasnt, check <#512787662344814622>!");
    }
}
