package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.github.ricksbrown.cowsay.Cowsay;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Cow extends FCommand {
    public Cow(){
        name = "cowsay";
        description = "provide a cowfile after -f for a custom cow. ex cowsay -f tux for tux instead of cow";
        aliases = Utils.makeAlias("cow");
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        int length;
        int tick = 0;
        if (StringUtils.isEmpty(event.getArgs())){
            event.reply("What the fuck man, you gave me no arguments!!!!!!!!!!!!!!!!!");
            return;
        }
        // check to see if -f was provided
        if (argsplit[0].equals("-f")){
            if (argsplit.length == 1 || argsplit.length == 2){
                event.reply("Fuckface, you didnt provide enough arguments!");
                return;
            }
            // okay so clearly we have something
            length = argsplit.length - 2;
            StringBuilder builder = new StringBuilder();
            while (tick != length){
                builder.append(argsplit[2+tick]);
                builder.append(" ");
                tick++;
            }
            // then form the message
            String[] wat = {"-f", argsplit[1], builder.toString()};
            event.reply("```\n"+Cowsay.say(wat)+"\n```");
            return;
        }
        String[] wat = {event.getArgs()};
        event.reply("```\n"+Cowsay.say(wat)+"\n```");


    }
}
