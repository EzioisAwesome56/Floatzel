package com.eziosoft.floatzel.Commands.Asshole;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Hello extends FCommand {
    private final Random random = new Random();
    public Hello(){
        name = "hello";
        description = "Says hello to whatever you type";
        category = asshole;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        Random random = new Random();
        String[] responses = {"Fuck you", "Fucking kill yourself asshole you", "Die in a hole", "Fucking HI", "I hope you burn in hell"};
        if (event.getArgs().length() < 1) {
            event.getChannel().sendMessage("You didn't give me someone to fucking say hi to dipshit").queue();
            return;
        } else if (event.getArgs().length() > 1500) {
            event.getChannel().sendMessage("Whoaaaaa fuckwit, that was way to long!").queue();
            return;
        }
        // send the message
        event.getChannel().sendMessage(responses[random.nextInt(responses.length - 1)] + " `" + event.getArgs() + "`").queue();

    }

}
