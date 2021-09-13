package com.eziosoft.floatzel.Listeners;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.SlashDataContainer;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;
import java.util.Locale;

public class SlashListen extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event){
        // first check the global slash commands
        if (Floatzel.scm.getGlobalmap().containsKey(event.getName().toLowerCase(Locale.ROOT))){
            Floatzel.scm.getGlobalmap().get(event.getName().toLowerCase(Locale.ROOT)).run(event);
        } else if (Floatzel.scm.getGuildmap().containsKey(new SlashDataContainer(event.getName().toLowerCase(Locale.ROOT), event.getGuild().getId()))){
            Floatzel.scm.getGuildmap().get(new SlashDataContainer(event.getName().toLowerCase(Locale.ROOT), event.getGuild().getId())).run(event);
        } else {
            event.reply("this command is invalid and will be removed from the command list.").setEphemeral(true).queue();
            // find and deleted that mf
            event.getJDA().retrieveCommands().queue(s -> {
                System.err.println("started command deletion search...");
                boolean found = false;
                for (Command c : s){
                    if (c.getIdLong() == event.getCommandIdLong()){
                        c.delete().queue(d -> System.err.println("command with id " + c.getId() + " deleted from global!"));
                        found = !found;
                    }
                }
                if (!found){
                    event.getGuild().retrieveCommands().queue(ss -> {
                        for (Command c : ss){
                            if (c.getIdLong() == event.getCommandIdLong()){
                                c.delete().queue(d -> System.err.println("command with id " + c.getId() + " deleted from server!"));
                        }
                    }});
                }
            });
        }

    }

    @Override
    public void onReady(ReadyEvent e){
        Floatzel.scm.RegisterGuildCommands();
    }
}
