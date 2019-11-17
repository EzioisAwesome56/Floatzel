package com.eziosoft.floatzel.Commands.Secret;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Gay extends FCommand {
    private final Random random = new Random();
    // secret command so we dont need description stuff
    public Gay(){
        name = "gay";
        description = "I dunno, you figure out what it does!";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        Random random = new Random();
        String[] gay = {"The person above is very fucking gay", "Above is the gayiest thing ever", "the person above will eat his own virginia/dick"};
        //  call person above gay
        event.getMessage().delete().queue(m -> event.getChannel().sendMessage(gay[random.nextInt(gay.length)]).queue());


    }
}
