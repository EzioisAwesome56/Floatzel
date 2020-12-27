package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Stop extends FCommand {
    public Stop(){
        name = "stop";
        description = "Stops the music";
        category = sound;
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        // wow this took way to long for me to find out how to do
        if (!Floatzel.musicPlayer.getHost(event.getGuild()).equals(event.getAuthor())) {
            event.reply("Stop trying to hijack this music session! You ain't the host!");
            return;
        } else {
            if (event.getGuild().getAudioManager().isConnected()) {
                Floatzel.musicPlayer.closeConnection(event.getGuild());
            } else {
                event.getChannel().sendMessage("I can't stop if I'm playing nothing!").queue();
            }
        }
    }
}
