package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public abstract class FSlashableCommand extends FCommand {

    public SlashActionGroup sag;

    public abstract void SlashCmdRun(SlashCommandEvent event, String... stuff);
}
