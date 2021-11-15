package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.SlashOption;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class prefix extends FSlashCommand {

    public prefix(){
        name = "prefix";
        help = "set the bot prefix for this server";
        needsServerAdmin = true;
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "Change prefix for bot in current-guild", "prefix"));
    }

    @Override
    public void execute(SlashCommandEvent e) {
        e.getHook().sendMessage("sup").queue();
    }
}
