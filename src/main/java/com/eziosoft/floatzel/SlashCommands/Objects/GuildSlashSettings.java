package com.eziosoft.floatzel.SlashCommands.Objects;

import java.util.ArrayList;
import java.util.List;

public class GuildSlashSettings {

    private String guildid;
    private List<String> registered;

    public GuildSlashSettings(String gid){
        this.registered = new ArrayList<String>();
        this.guildid = gid;
    }

    public List<String> getRegistered() {
        return this.registered;
    }

    public void addRegistered(String name){
        this.registered.add(name);
    }

    public String getGuildid() {
        return guildid;
    }
}
