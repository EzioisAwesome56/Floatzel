package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Stop extends FCommand {
    public Stop(){
        name = "stop";
        description = "Stops the shitty music";
        category = sound;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        if (event.getGuild().getAudioManager().isConnected()) {
            Floatzel.musicPlayer.closeConnection(event.getGuild());
        } else {
            event.getChannel().sendMessage("I cant fucking stop if I'm playing jack shit asshole!").queue();
        }
    }
}
