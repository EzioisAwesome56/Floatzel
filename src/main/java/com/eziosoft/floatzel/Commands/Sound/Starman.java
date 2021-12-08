package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Starman extends FSlashableCommand {
    public Starman(){
        name = "starman";
        description = "peter";
        category = sound;
        sag = SlashActionGroup.AUDIO;
    }

    private static String url = "https://www.youtube.com/watch?v=t-LA1yaF_mI";

    @Override
    protected void cmdrun(CommandEvent event){
        Floatzel.musicPlayer.loadAndMeme(event, url);
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        Floatzel.musicPlayer.loadAndMeme(event, url);
    }
}
