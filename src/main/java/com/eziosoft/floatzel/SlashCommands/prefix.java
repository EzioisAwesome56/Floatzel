package com.eziosoft.floatzel.SlashCommands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class prefix extends FSlashCommand{

    public prefix(){
        name = "prefix";
        help = "set the bot prefix for this server";
        needsServerAdmin = true;
        hasoptions = true;
        optiontype = OptionType.STRING;
        optionHelp = "Change prefix for bot in current-guild";
        optionName = "prefix";
    }

    @Override
    public void execute(SlashCommandEvent e) {
        e.getHook().sendMessage("sup").queue();
    }
}
