package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Commands.FImageCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public abstract class FSlashableImageCommand extends FImageCommand {

    public SlashActionGroup sag;

    public abstract void SlashCmdRun(SlashCommandEvent event, String... stuff);
}
