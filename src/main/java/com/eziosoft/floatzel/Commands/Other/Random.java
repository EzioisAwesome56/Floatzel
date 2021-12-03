package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Random extends FSlashableCommand {
    public Random(){
        name = "random";
        aliases = Utils.makeAlias("rng");
        description = "Generates a random number";
        category = fun;
        sag = SlashActionGroup.OTHER;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        event.reply(Integer.toString(random.nextInt()));
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        event.getHook().editOriginal(Integer.toString(random.nextInt())).queue();
    }
}
