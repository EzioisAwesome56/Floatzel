package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashDataContainer;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SlashCommandManager extends ListenerAdapter {

    // map of registerable slash commands; allows for mods to add slash commands without force-registering them
    private Map<String, FSlashCommand> registerable = new HashMap<>();
    public void addRegisterable(String name, FSlashCommand fsc){
        this.registerable.put(name, fsc);
    }
    public boolean hasRegisterable(String name){
        return this.registerable.containsKey(name);
    }
    public FSlashCommand getRegisterable(String name){
        return this.registerable.get(name);
    }

    // actual slash commands go here
    private HashMap<String, FSlashCommand> globalmap = new HashMap<>();
    private Map<SlashDataContainer, FSlashCommand> guildmap = new HashMap<>();

    /* slashable commands, which are normal commands with slash functionality,
     will be placed into a hashmap of commands, to be called by the bigger slash commands
     as some form of like "command action" system
     */
    private Map<SlashableCommandEntry, FSlashableCommand> actions = new HashMap<>();
    // images need a different map because i can't extend 2 classes in java
    private Map<SlashableCommandEntry, FSlashableImageCommand> imageActions = new HashMap<>();

    // getting and checking for actions
    public void addSlashableAction(String name, FSlashableCommand fsc){
        this.actions.put(new SlashableCommandEntry(fsc.sag, name), fsc);
    }
    public boolean hasSlashAction(SlashableCommandEntry sce){
        return this.actions.containsKey(sce);
    }
    public FSlashableCommand getSlashAction(SlashableCommandEntry sce){
        return this.actions.get(sce);
    }
    public void addSlashableImageAction(String name, FSlashableImageCommand fsic){
        this.imageActions.put(new SlashableCommandEntry(fsic.sag, name), fsic);
    }
    public boolean hasSlashableImageAction(SlashableCommandEntry sce){
        return this.imageActions.containsKey(sce);
    }
    public FSlashableImageCommand getSlashImageAction(SlashableCommandEntry sce){
        return this.imageActions.get(sce);
    }



    public SlashCommandManager(){}


    public void addGlobalCmd(String name, FSlashCommand fsc){
        this.globalmap.put(name, fsc);
    }

    public void addGuildCmd(SlashDataContainer data, FSlashCommand fsc){
        this.guildmap.put(data, fsc);
    }

    public HashMap<String, FSlashCommand> getGlobalmap() {
        return this.globalmap;
    }

    public Map<SlashDataContainer, FSlashCommand> getGuildmap() {
        return this.guildmap;
    }

    public boolean RemoveGuildCommand(SlashDataContainer sdc){
        // is the commmand even registered?
        if (!this.guildmap.containsKey(sdc)){
            return false;
        }
        this.guildmap.remove(sdc);
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
        for (Map.Entry<SlashDataContainer, FSlashCommand> e : this.guildmap.entrySet()){
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
    public void onReady(@NotNull ReadyEvent e){
        if (e.getJDA().getShardInfo().getShardId() == 1) {
            // TODO: reload guild registered commands...probably store them in the database
            System.out.println("SlashCommandManager ready!");
            //Floatzel.scm.RegisterGuildCommands();
        }
    }
}
