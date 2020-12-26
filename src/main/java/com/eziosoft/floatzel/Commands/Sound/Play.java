package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Play extends FCommand {
    public Play(){
        name = "play";
        description = "queue a song to be played by the audio player";
        category = sound;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String args = event.getArgs();
        // check if thr string is empty
        if (StringUtils.isEmpty(args)){
            event.getChannel().sendMessage("You didn't give me anything to play!").queue();
            return;
        }
        // check if there is a space
        if (args.indexOf(" ") != -1){
            event.getChannel().sendMessage("urls cannot have spaces in them!").queue();
            return;
        }
        // play the music
        Floatzel.musicPlayer.loadAndPlay(event, args);
    }
}
