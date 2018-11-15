package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Ship extends FCommand {
    public Ship(){
        name = "ship";
        description = "make your love dreams come true!";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String one = "";
        String two = "";
        try {
            one = argsplit[0];
            two = argsplit[1];
        } catch (ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Error: you didnt provide enough arguments dumb fuck").queue();
            return;
        }
        event.getChannel().sendMessage("Unfinished command! Please wait for a future update").queue();
    }
}
