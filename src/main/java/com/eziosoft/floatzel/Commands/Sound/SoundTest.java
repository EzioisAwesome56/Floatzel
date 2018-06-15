package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SoundTest extends FCommand {
    public SoundTest(){
        name = "fuck";
        description = "Fuck you";
        category = sound;
    }

    @Override
    protected void execute(CommandEvent event){
        Floatzel.musicPlayer.loadAndMeme(event, "https://www.youtube.com/watch?v=mmVI16Vd-aY");
    }
}
