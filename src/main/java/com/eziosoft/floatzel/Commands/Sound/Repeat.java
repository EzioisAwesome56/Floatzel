package com.eziosoft.floatzel.Commands.Sound;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Repeat extends FSlashableCommand {
    public Repeat(){
        name = "repeat";
        description = "sets repeat on playing music";
        category = sound;
        aliases = Utils.makeAlias("loop");
        sag = SlashActionGroup.AUDIO;
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

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (!event.getGuild().getAudioManager().isConnected()){
            event.getHook().editOriginal("Error: no audio is playing to repeat!").queue();
            return;
        } else {
            Floatzel.musicPlayer.repeat(event);
        }
    }
}
