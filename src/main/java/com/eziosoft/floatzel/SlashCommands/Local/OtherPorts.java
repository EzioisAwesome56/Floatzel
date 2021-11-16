package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class OtherPorts extends FSlashCommand {

    public OtherPorts(){
        name = "other";
        help = "Runs floatzel's normal commands from the \"other\" category";
        ephemeral = false;
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "name of command to run", "cmdname", true));
    }
    @Override
    protected void execute(SlashCommandEvent e) {
        SlashableCommandEntry sce = new SlashableCommandEntry(SlashActionGroup.OTHER, e.getOption("cmdname").getAsString());
        if (Floatzel.scm.hasSlashAction(sce)){
            Floatzel.scm.getSlashAction(sce).SlashCmdRun(e);
        } else {
            e.getHook().sendMessage("Error: that command does not exist!").queue();
        }
    }
}