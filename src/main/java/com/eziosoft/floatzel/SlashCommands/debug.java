package com.eziosoft.floatzel.SlashCommands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class debug extends FSlashCommand{

    public debug(){
        name = "debug";
        help = "debug command. it stucks";
        needsServerAdmin = true;
        ephemeral = true;
    }

    @Override
    public void execute(SlashCommandEvent e) {
        e.getHook().sendMessage("sup").queue();
    }
}
