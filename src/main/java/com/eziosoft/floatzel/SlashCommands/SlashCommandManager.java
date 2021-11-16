package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Floatzel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SlashCommandManager extends ListenerAdapter {

    // slash command bullshavick here
    private HashMap<String, FSlashCommand> globalmap = new HashMap<>();
    private Map<SlashDataContainer, FSlashCommand> guildmap = new HashMap<>();


    public SlashCommandManager(){}


    public void addGlobalCmd(String name, FSlashCommand fsc){
        globalmap.put(name, fsc);
    }

    public void addGuildCmd(SlashDataContainer data, FSlashCommand fsc){
        guildmap.put(data, fsc);
    }

    public HashMap<String, FSlashCommand> getGlobalmap() {
        return globalmap;
    }

    public Map<SlashDataContainer, FSlashCommand> getGuildmap() {
        return guildmap;
    }

    public boolean RemoveGluildCommand(SlashDataContainer sdc){
        // is the commmand even registered?
        if (!guildmap.containsKey(sdc)){
            return false;
        }
        guildmap.remove(sdc);
        Floatzel.jda.getGuildById(sdc.getGuildid()).retrieveCommands().queue(scs -> {
            for(Command sc : scs){
                if (sc.getName().equals(sdc.getName())){
                    sc.delete().queue();
                    break;
                }
            }
        });
        return true;
    }

    public void RegisterGuildCommands(){
        for (Map.Entry<SlashDataContainer, FSlashCommand> e : guildmap.entrySet()){
            FSlashCommand c = e.getValue();
            SlashDataContainer info = e.getKey();
            if (c.hasoptions){
                CommandCreateAction cca;
                cca = Floatzel.jda.getGuildById(info.getGuildid()).upsertCommand(info.getName(), c.help);
                for (SlashOption so : e.getValue().optlist){
                    // iterate thru every slashoption and add it
                    cca = cca.addOption(so.getOptype(), so.getName(), so.getHelp(), so.isRequired());
                }
                // then queue it
                cca.queue();
            } else {
                Floatzel.jda.getGuildById(info.getGuildid()).upsertCommand(info.getName(), c.help).queue();
            }
        }
    }

    // this doesn't really need its own listener class, so we move it inside
    // of the slash command manager
    @Override
    public void onSlashCommand(SlashCommandEvent event){
        // first check the global slash commands
        if (this.globalmap.containsKey(event.getName().toLowerCase(Locale.ROOT))){
            this.globalmap.get(event.getName().toLowerCase(Locale.ROOT)).run(event);
        } else if (this.guildmap.containsKey(new SlashDataContainer(event.getName().toLowerCase(Locale.ROOT), event.getGuild().getId()))){
            this.guildmap.get(new SlashDataContainer(event.getName().toLowerCase(Locale.ROOT), event.getGuild().getId())).run(event);
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
