package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.github.ricksbrown.cowsay.Cowsay;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Cow extends FCommand {
    public Cow(){
        name = "cowsay";
        description = "moooo";
        aliases = Utils.makeAlias("cow");
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgs())){
            event.reply("What the fuck man, you gave me no arguments!!!!!!!!!!!!!!!!!");
            return;
        }
        String[] wat = {"-f", "tux", event.getArgs()};
        event.reply("```\n"+Cowsay.say(wat)+"\n```");


    }
}
