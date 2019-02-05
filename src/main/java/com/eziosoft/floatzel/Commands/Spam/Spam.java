package com.eziosoft.floatzel.Commands.Spam;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.CommdLogic.SpamLogic;
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
    protected void cmdrun(CommandEvent event) {
        String character = event.getArgs();
        // send back whatever message comes back
        event.reply(SpamLogic.makeMessage(character));
    }
}

