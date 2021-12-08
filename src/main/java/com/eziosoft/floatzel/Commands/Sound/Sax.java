package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Sax extends FSlashableCommand {
    public Sax(){
        name = "sax";
        description = "busts out an awesome sax solo";
        category = sound;
        sag = SlashActionGroup.AUDIO;
    }

    private static String url = "https://www.youtube.com/watch?v=e4iMxrGoifU";

    @Override
    protected void cmdrun(CommandEvent event){
        Floatzel.musicPlayer.loadAndMeme(event, url);
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        Floatzel.musicPlayer.loadAndMeme(event, url);
    }
}
