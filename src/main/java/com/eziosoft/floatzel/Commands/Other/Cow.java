package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.github.ricksbrown.cowsay.Cowsay;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Cow extends FSlashableCommand {
    public Cow(){
        name = "cowsay";
        description = "make a ascii cow say something";
        aliases = Utils.makeAlias("cow");
        category = fun;
        sag = SlashActionGroup.FUN;
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        int length;
        int tick = 0;
        if (StringUtils.isEmpty(event.getArgs())){
            event.reply("you gave me no arguments!");
            return;
        }
        // check to see if -f was provided
        if (argsplit[0].equals("-f")){
            if (argsplit.length == 1 || argsplit.length == 2){
                event.reply("you didn't provide enough arguments!");
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

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (event.getOption("arg") == null){
            event.getHook().editOriginal("```\n" + Cowsay.say(new String[]{"moooooo, you forgot to tell me what to say in the \"arg\" option!"}) + "\n```").queue();
        } else if (event.getOption("arg").getAsString().length() > 200){
            event.getHook().editOriginal("```\n" + Cowsay.say(new String[]{"that is tooooooo long!"}) + "\n```").queue();
        } else {
            if (event.getOption("cow") != null){
                if (event.getOption("arg") != null) {
                    event.getHook().editOriginal("```\n" + Cowsay.say(new String[]{"-f", event.getOption("cow").getAsString(), event.getOption("arg").getAsString()}) + "\n```").queue();
                } else {
                    event.getHook().editOriginal("```\n" + Cowsay.say(new String[]{"-f", event.getOption("cow").getAsString(), "moo, you didn't tell me what to say in the \"arg\" option!"}) + "\n```").queue();
                }
            } else {
                event.getHook().editOriginal("```\n" + Cowsay.say(new String[]{event.getOption("arg").getAsString()}) + "\n```").queue();
            }
        }
    }
}
