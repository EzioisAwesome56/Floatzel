package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.github.ricksbrown.cowsay.Cowsay;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Cow extends FSlashableCommand {
    public Cow(){
        name = "cowsay";
        description = "provide a cowfile after -f for a custom cow. ex cowsay -f tux for tux instead of cow\nsee here for list of cows:\nhttps://github.com/schacon/cowsay/tree/master/cows";
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
    public void SlashCmdRun(SlashCommandEvent event, String... stuff) {
        if (stuff.length < 1){
            event.getHook().sendMessage(Cowsay.say(new String[]{"moooooo"})).queue();
        } else if (stuff[0].length() > 200){
            event.getHook().sendMessage(Cowsay.say(new String[]{"that is tooooooo long!"})).queue();
        } else {
            // TODO: make using different cows work
            event.getHook().sendMessage(Cowsay.say(new String[]{stuff[0]})).queue();
        }
    }
}
