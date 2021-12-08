package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Stop extends FSlashableCommand {
    public Stop(){
        name = "stop";
        description = "Stops the music";
        category = sound;
        sag = SlashActionGroup.AUDIO;
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

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (!Floatzel.musicPlayer.getHost(event.getGuild()).equals(event.getUser())){
            event.getHook().editOriginal("Error: you are not the host! You cannot end this session.").queue();
            return;
        } else {
            if (event.getGuild().getAudioManager().isConnected()){
                Floatzel.musicPlayer.closeConnection(event.getGuild());
            } else {
                event.getHook().editOriginal("Error: no music is playing for me to stop!").queue();
                return;
            }
        }
    }
}
