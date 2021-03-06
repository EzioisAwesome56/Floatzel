package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Repeat extends FCommand {
    public Repeat(){
        name = "repeat";
        description = "sets repeat on playing music";
        category = sound;
        aliases = Utils.makeAlias("loop");
    }

    @Override
    protected void cmdrun(CommandEvent event){
        if (!event.getGuild().getAudioManager().isConnected()){
            event.getChannel().sendMessage("Im not playing anything to repeat!").queue();
            return;
        } else {
            Floatzel.musicPlayer.repeat(event);
        }
    }
}
