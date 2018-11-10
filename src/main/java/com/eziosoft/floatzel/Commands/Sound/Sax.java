package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Sax extends FCommand {
    public Sax(){
        name = "sax";
        description = "busts out a fucking awesome sax solo";
        category = sound;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        Floatzel.musicPlayer.loadAndMeme(event, "https://www.youtube.com/watch?v=92UVL1FvZa4");
    }
}
