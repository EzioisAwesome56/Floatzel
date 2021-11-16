package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Invite extends FSlashableCommand {
    public Invite(){
        name = "invite";
        description = "Self-explanitory";
        category = other;
        sag = SlashActionGroup.OTHER;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage(genMsg()).queue();
    }

    private String genMsg(){
        // TODO: do this
        return "this command is getting a rewrite";
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        event.getHook().sendMessage(genMsg()).queue();
    }
}
