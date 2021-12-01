package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Avatar extends FSlashableCommand {
    public Avatar(){
        name = "avatar";
        aliases = Utils.makeAlias("ava");
        description = "get the avatar of a mentioned user!";
        category = other;
        sag = SlashActionGroup.OTHER;
    }


    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        // did they mention a user?
        if (event.getMessage().getMentionedUsers().isEmpty()){
            event.reply("You did not mention the user who you want their avatar!");
            return;
        }
        event.reply(event.getMessage().getMentionedUsers().get(0).getAvatarUrl());
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (event.getOption("user") == null){
            event.getHook().editOriginal("Error: you need to mention a user using \"user\" option!").queue();
            return;
        }
        event.getHook().editOriginal(event.getOption("user").getAsUser().getAvatarUrl()).queue();
    }
}
