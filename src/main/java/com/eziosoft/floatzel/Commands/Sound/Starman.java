package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Starman extends FCommand {
    public Starman(){
        name = "starman";
        description = "fucking peter";
        category = sound;
    }

    @Override
    protected void execute(CommandEvent event){
        Floatzel.musicPlayer.loadAndMeme(event, "https://www.youtube.com/watch?v=t-LA1yaF_mI");
    }
}
