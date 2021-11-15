package com.eziosoft.floatzel.SlashCommands.Globals;

import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.SlashOption;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class GManage extends FSlashCommand {

    public GManage(){
        name = "gmanage";
        help = "Manage slash commands for the bot in this server";
        optlist.add(new SlashOption(OptionType.STRING, "Action to preform", "action", true));
        optlist.add(new SlashOption(OptionType.STRING, "argument for said action", "arg1"));
        needsServerAdmin = true;
        hasoptions = true;
        ephemeral = true;
    }
    @Override
    public void execute(SlashCommandEvent e) {
        String action = e.getOption("action").getAsString();
        if (action.equals("register")){
            e.getHook().sendMessage("you have requested to register a slash command for this guild!").queue();
        } else {
            // TODO: write help for this command
            e.getHook().sendMessage("help goes here").queue();
        }
    }
}
