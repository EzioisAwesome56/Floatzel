package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.Objects.GuildSlashSettings;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashDataContainer;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Error;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SlashCommandManager extends ListenerAdapter {

    // map of registrable slash commands; allows for mods to add slash commands without force-registering them
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
    public Map<String, FSlashCommand> getAllRegisterables(){ return this.registerable; }

    // actual slash commands go here
    private HashMap<String, FSlashCommand> globalmap = new HashMap<>();
    private Map<SlashDataContainer, FSlashCommand> guildmap = new HashMap<>();

    /*
    this array will be used for saving and loading the per-guild slash command settings
    basically, just which commands are enabled and which commands are not enabled
    side note: these functions are private because they get automatically
    called by the functions to un/re-register slash commands for ease of coding it.
     */
    private final Map<String, GuildSlashSettings> settings = new HashMap<String, GuildSlashSettings>();
    public GuildSlashSettings getGuildSlashSettings(String id){ return this.settings.get(id); }
    public boolean hasSlashSettings(String id){ return  this.settings.containsKey(id); }


    /* slashable commands, which are normal commands with slash functionality,
     will be placed into a hashmap of commands, to be called by the bigger slash commands
     as some form of like "command action" system
     */
    private Map<SlashableCommandEntry, FSlashableCommand> actions = new HashMap<>();
    // images need a different map because i can't extend 2 classes in java
    private Map<SlashableCommandEntry, FSlashableImageCommand> imageActions = new HashMap<>();

    // getting and checking for actions
    public void addSlashableAction(String name, FSlashableCommand fsc){this.actions.put(new SlashableCommandEntry(fsc.sag, name), fsc);}
    public boolean hasSlashAction(SlashableCommandEntry sce){
        return this.actions.containsKey(sce);
    }
    public FSlashableCommand getSlashAction(SlashableCommandEntry sce){
        return this.actions.get(sce);
    }
    public void addSlashableImageAction(String name, FSlashableImageCommand fsic){this.imageActions.put(new SlashableCommandEntry(fsic.sag, name), fsic);}
    public boolean hasSlashableImageAction(SlashableCommandEntry sce){
        return this.imageActions.containsKey(sce);
    }
    public FSlashableImageCommand getSlashImageAction(SlashableCommandEntry sce){return this.imageActions.get(sce);}
    public Set<Map.Entry<SlashableCommandEntry, FSlashableImageCommand>> getImageActions(){ return this.imageActions.entrySet(); }
    public Set<Map.Entry<SlashableCommandEntry, FSlashableCommand>> getActions(){ return this.actions.entrySet(); }



    public SlashCommandManager(){}


    public void addGlobalCmd(String name, FSlashCommand fsc){
        this.globalmap.put(name, fsc);
    }

    public void addGuildCmd(SlashDataContainer data, FSlashCommand fsc){
        this.guildmap.put(data, fsc);
        if (!data.getName().equals("devmanage")) {
            if (this.settings.containsKey(data.getGuildid())) {
                // get the command array, add new command
                this.settings.get(data.getGuildid()).addRegistered(data.getName());
            } else {
                List<String> blank = new ArrayList<>();
                blank.add(data.getName());
                GuildSlashSettings tempslash = new GuildSlashSettings(data.getGuildid());
                tempslash.addRegistered(data.getName());
                this.settings.put(data.getGuildid(), tempslash);
            }
            Database.dbdriver.saveGuildSlashSettings(this.settings.get(data.getGuildid()));
        }
        upsertGuildCmd(data, fsc);
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
        // remove it from the saved settings thing
        this.settings.get(sdc.getGuildid()).getRegistered().remove(sdc.getName());
        Database.dbdriver.saveGuildSlashSettings(this.settings.get(sdc.getGuildid()));
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

    // could maybe be used to force a command update if something is wrong
    public void updateGuildCommandsForGuild(String id){
        for (Map.Entry<SlashDataContainer, FSlashCommand> ent : this.guildmap.entrySet()){
            if (!ent.getKey().getGuildid().equals(id)){
                // not the guild we care about
                continue;
            }
            FSlashCommand c = ent.getValue();
            if (c.hasoptions){
                CommandCreateAction cca = Floatzel.jda.getGuildById(id).upsertCommand(ent.getKey().getName(), c.help);
                for (SlashOption so : c.optlist){
                    cca = cca.addOption(so.getOptype(), so.getName(), so.getHelp(), so.isRequired());
                }
                cca.queue();
            } else {
                Floatzel.jda.getGuildById(ent.getKey().getGuildid()).upsertCommand(ent.getKey().getName(), c.help).queue();
            }
        }
    }

    /* i feel like its a bad idea to run thru every guild cmd in the main map when we just need to register a single command
    this is the fix for that
    called from addGuildCmd, it just registers the single command with discord, without looping thru all of the
    other commands in the array.
     */
    private void upsertGuildCmd(SlashDataContainer sdc, FSlashCommand fsc){
        if (fsc.hasoptions){
            CommandCreateAction cca = Floatzel.jda.getGuildById(sdc.getGuildid()).upsertCommand(sdc.getName(), fsc.help);
            for (SlashOption so : fsc.optlist){
                cca = cca.addOption(so.getOptype(), so.getName(), so.getHelp(), so.isRequired());
            }
            try {
                cca.queue();
            } catch (Exception e){
                Error.CatchOld(e);
            }
        } else {
            try {
                Floatzel.jda.getGuildById(sdc.getGuildid()).upsertCommand(sdc.getName(), fsc.help).queue();
            } catch (Exception e){
                Error.CatchOld(e);
            }
        }
    }

    // call this function to upsert all registered global cmds
    private void upsertGlobalCmds(){
        if (!Floatzel.isdev) {
            System.out.println("Now upserting all global slash commands...");
            for (Map.Entry<String, FSlashCommand> ent : this.globalmap.entrySet()) {
                Floatzel.jda.getShards().forEach(jda -> {
                    jda.upsertCommand(ent.getKey(), ent.getValue().help).queue();
                });
                System.out.println("Upserted " + ent.getKey());
            }
            System.out.println("All done!");
        } else {
            System.err.println("Error: global commands are not used in development mode.\n" +
                    "if you need the manage command, use force 6 to register it guild-side");
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

    private void loadAllRegisteredSlashCommands(){
        System.out.println("Loading saved slash command settings...");
        // first get the list
        GuildSlashSettings[] gss = Database.dbdriver.loadAllSlashSettings();
        for (GuildSlashSettings dank : gss){
            String gid = dank.getGuildid();
            // first add it to the settings hashmap
            this.settings.put(gid, dank);
            // now we have to add them to the guildcmd hashmap
            for (String s : dank.getRegistered()){
                this.guildmap.put(new SlashDataContainer(s, gid), this.getRegisterable(s));
            }
        }
        System.out.println("Done loading slash command settings!");
    }

    @Override
    public void onReady(@NotNull ReadyEvent e){
        if (e.getJDA().getShardInfo().getShardId() == 1) {
            loadAllRegisteredSlashCommands();
            upsertGlobalCmds();
            System.out.println("SlashCommandManager ready!");
            //Floatzel.scm.RegisterGuildCommands();
        }
    }
}
