package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
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
        return "https://discord.com/api/oauth2/authorize?client_id=" + Floatzel.conf.getClientid() + "&permissions=277096033280&scope=bot%20applications.commands";
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        event.getHook().editOriginal(genMsg()).queue();
    }
}
