package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.lang.Math.*;

public class Pi extends FCommand {

    public Pi() {
        name = "pi";
        description = "Shows you what fucking pi is";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent commandEvent) {
        String gay = Double.toString(Math.PI);
        commandEvent.getChannel().sendMessage("Did you fucking fail math you dumbass?\npi is clearly "+gay).queue();
  }
}
