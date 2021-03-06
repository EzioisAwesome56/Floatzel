package com.eziosoft.floatzel.Commands.Spam;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Spam extends FCommand {
    public Spam() {
        name = "spam";
        description = "spam one character 2000 times";
        category = spam;
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        String character = event.getArgs();
        // taken from the old spam.java
        String[] bad = {"`", "*", "_"};
        // main code
        if (character.length() == 0) {
            event.reply("You didn't give me anything to spam!");
            return;
        } else if (character.length() > 1) {
            event.reply("That is not one single character!");
            return;
        } else {
            if (Arrays.stream(bad).anyMatch(s -> s.equals(character))) {
                event.reply("you can't use that character!");
                return;
            }
        }
        event.reply(StringUtils.repeat(character, 2000));
    }
}

