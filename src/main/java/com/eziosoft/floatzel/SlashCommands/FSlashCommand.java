package com.eziosoft.floatzel.SlashCommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public abstract class FSlashCommand {

    String name;
    String help = "no help";
    boolean needsServerAdmin = false;
    boolean needsManageMessages =  false;
    boolean hasoptions = false;
    OptionType optiontype;
    String optionHelp = "no help";
    String optionName;

    public void run(SlashCommandEvent e){
        e.deferReply().queue();
        // check if the user has server admin
        if (needsServerAdmin){
            if (!e.getGuild().getMember(e.getUser()).isOwner()){
                if (!e.getGuild().getMember(e.getUser()).hasPermission(Permission.ADMINISTRATOR)){
                    e.reply("You can't run this command!").setEphemeral(true).queue();
                    return;
                }
            }
        }
        execute(e);
    }

    public abstract void execute(SlashCommandEvent e);
}
