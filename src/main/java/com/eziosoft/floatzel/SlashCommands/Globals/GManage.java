package com.eziosoft.floatzel.SlashCommands.Globals;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashDataContainer;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class GManage extends FSlashCommand {

    public GManage(){
        name = "gmanage";
        help = "Manage slash commands for the bot in this server";
        optlist.add(new SlashOption(OptionType.STRING, "Action to preform", "action", true));
        optlist.add(new SlashOption(OptionType.STRING, "String argument for action", "arg1"));
        needsServerAdmin = true;
        hasoptions = true;
        ephemeral = true;
    }
    @Override
    public void execute(SlashCommandEvent e) {
        String action = e.getOption("action").getAsString();
        if (action.equals("register")){
            if (e.getOption("arg1") == null){
                e.getHook().sendMessage("no command name provided! You can use the \"list\" action to see all valid command names!").queue();
                return;
            }
            String input = e.getOption("arg1").getAsString();
            if (!Floatzel.scm.hasRegisterable(input)){
                e.getHook().sendMessage("That is not a registerable slash command!").queue();
                return;
            }
            FSlashCommand cmd = Floatzel.scm.getRegisterable(input);
            Floatzel.scm.addGuildCmd(new SlashDataContainer(cmd.name, e.getGuild().getId()), cmd);
            e.getHook().sendMessage("Registered command " + cmd.name).queue();
        } else if (action.equals("remove")) {
            if (e.getOption("arg1") == null){
                e.getHook().sendMessage("no command name provided!").queue();
                return;
            }
            boolean win = Floatzel.scm.RemoveGuildCommand(new SlashDataContainer(e.getOption("arg1").getAsString(), e.getGuild().getId()));
            if (!win){
                e.getHook().sendMessage("Command failed to remove. Are you sure it was registered?").queue();
            } else {
                e.getHook().sendMessage("Command removed successfully!").queue();
            }
        } else {
            // TODO: write help for this command
            e.getHook().sendMessage("help goes here").queue();
        }
    }
}
