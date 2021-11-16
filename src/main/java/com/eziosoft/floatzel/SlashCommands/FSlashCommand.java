package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class FSlashCommand {

    public String name;
    public String help = "no help";
    public boolean needsServerAdmin = false;
    public boolean needsManageMessages =  false;
    public boolean hasoptions = false;
    public boolean ephemeral = false;
    public List<SlashOption> optlist = new ArrayList<SlashOption>();

    public void run(SlashCommandEvent e){
        // check if the user has server admin
        if (needsServerAdmin){
            if (!e.getGuild().getMember(e.getUser()).isOwner()){
                if (!e.getGuild().getMember(e.getUser()).hasPermission(Permission.ADMINISTRATOR)){
                    e.reply("You can't run this command!").setEphemeral(true).queue();
                    return;
                }
            }
        }
        e.deferReply(ephemeral).queue();
        execute(e);
    }

    protected abstract void execute(SlashCommandEvent e);
}
