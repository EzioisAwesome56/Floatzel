package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.commons.lang3.StringUtils;

public class Play extends FSlashableCommand {
    public Play(){
        name = "play";
        description = "queue a song to be played by the audio player";
        category = sound;
        sag = SlashActionGroup.AUDIO;
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

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (event.getOption("url") == null){
            event.getHook().editOriginal("Error: you did not provide a url to play via the \"url\" option!").queue();
            return;
        }
        String url = event.getOption("url").getAsString();
        if (url.contains(" ")){
            event.getHook().editOriginal("Error: urls cannot have spaces in them!").queue();
            return;
        }
        Floatzel.musicPlayer.loadAndPlaySlash(event, event.getOption("url").getAsString());
    }
}
