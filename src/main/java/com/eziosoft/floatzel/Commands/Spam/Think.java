package com.eziosoft.floatzel.Commands.Spam;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Think extends FCommand {
    public Think(){
        name = "think";
        description = "Fucking make me Think way to hard asshole wtf";
        category = spam;
    }

    @Override
    protected void cmdrun(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(StringUtils.repeat("\uD83E\uDD14", 100)).queue();
    }
}
