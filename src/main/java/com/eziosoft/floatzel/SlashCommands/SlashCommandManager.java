package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Floatzel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandManager {

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

    public void RegisterGuildCommands(){
        for (Map.Entry<SlashDataContainer, FSlashCommand> e : guildmap.entrySet()){
            FSlashCommand c = e.getValue();
            SlashDataContainer info = e.getKey();
            if (c.hasoptions){
                Floatzel.jda.getGuildById(info.getGuildid()).upsertCommand(info.getName(), c.help).addOption(c.optiontype, c.optionName, c.optionHelp).queue();
            } else {
                Floatzel.jda.getGuildById(info.getGuildid()).upsertCommand(info.getName(), c.help).queue();
            }
        }
    }
}
