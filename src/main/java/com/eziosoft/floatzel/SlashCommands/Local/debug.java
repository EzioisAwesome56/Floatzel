package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class debug extends FSlashCommand {

    public debug(){
        name = "debug";
        help = "debug command. it stucks";
        needsServerAdmin = true;
        ephemeral = false;
    }

    @Override
    public void execute(SlashCommandEvent e) {
        if (Floatzel.scm.hasSlashAction(new SlashableCommandEntry(SlashActionGroup.OTHER, "pi"))){
            Floatzel.scm.getSlashAction(new SlashableCommandEntry(SlashActionGroup.OTHER, "pi")).SlashCmdRun(e);
        }
    }
}
