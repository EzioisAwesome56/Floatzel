package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Pi extends FSlashableCommand {

    public Pi() {
        name = "pi";
        description = "Shows you what pi is";
        category = other;
        sag = SlashActionGroup.OTHER;
    }

    @Override
    protected void cmdrun(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(genMsg()).queue();
  }

    @Override
    public void SlashCmdRun(SlashCommandEvent event, String... stuff) {
        event.getHook().sendMessage(genMsg()).queue();
    }

    private String genMsg(){
        return "pi is " + Double.toString(Math.PI);
    }
}
