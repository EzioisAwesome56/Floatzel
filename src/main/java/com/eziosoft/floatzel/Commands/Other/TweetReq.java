package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TweetReq extends FCommand {
    public TweetReq(){
        name = "a";
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage("re").queue();
    }
}
