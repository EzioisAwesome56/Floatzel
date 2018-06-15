package com.eziosoft.floatzel.Commands.Spam;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Spam extends FCommand {
    public Spam() {
        name = "spam";
        description = "spam one fucking character 2000 times";
        category = spam;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] bad = {"`", "*", "_"};
        String character = event.getArgs();
        if (character.length() == 0) {
            event.getChannel().sendMessage("You didn't fucking give me anything to spam dumbass!").queue();
        } else if (character.length() > 1) {
            event.getChannel().sendMessage("That is not one single character dumbass!").queue();
        } else {
            if (Arrays.stream(bad).anyMatch(s -> s.equals(character))) {
                event.getChannel().sendMessage("you can't use that fucking character dumbass").queue();
                return;
            }
            
            event.getChannel().sendMessage(StringUtils.repeat(character, 2000)).queue();
        }
    }
}

