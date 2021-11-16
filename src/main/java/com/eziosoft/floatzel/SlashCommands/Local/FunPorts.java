package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class FunPorts extends FSlashCommand {

    public FunPorts(){
        name = "fun";
        help = "Runs floatzel's normal commands from the \"fun\" category";
        ephemeral = false;
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "name of command to run", "cmdname", true));
        optlist.add(new SlashOption(OptionType.STRING, "additional arguments for command", "arg"));
    }
    @Override
    protected void execute(SlashCommandEvent e) {
        SlashableCommandEntry sce = new SlashableCommandEntry(SlashActionGroup.FUN, e.getOption("cmdname").getAsString());
        if (Floatzel.scm.hasSlashAction(sce)){
            if (e.getOption("arg") != null){
                Floatzel.scm.getSlashAction(sce).SlashCmdRun(e, e.getOption("arg").getAsString());
            } else {
                Floatzel.scm.getSlashAction(sce).SlashCmdRun(e);
            }
        } else {
            e.getHook().sendMessage("Error: that command does not exist!").queue();
        }
    }
}
