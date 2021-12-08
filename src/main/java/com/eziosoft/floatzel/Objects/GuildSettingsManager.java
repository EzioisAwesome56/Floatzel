package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildSettingsManager {
    private final Map<String, String> cachedPrefixes = new HashMap<>();
    private static final String table = "guildSettings";

    public GuildSettingsManager(){
        System.out.println("Floatzel GuildSettings Manager loaded");
    }

    public String getPrefix(String id){
        if (this.cachedPrefixes.containsKey(id)){
            return this.cachedPrefixes.get(id);
        }
        GuildSettings set;
        try {
            set = loadGuildSettings(id);
        } catch (NullPointerException e){
            return Floatzel.isdev ? Floatzel.conf.getDevprefix() : Floatzel.conf.getPrefix();
        }
        this.cachedPrefixes.put(id, set.getPrefix());
        return set.getPrefix();
    }


    public GuildSettings loadGuildSettings(String id) throws NullPointerException{
        Object hell = Database.dbdriver.getDriver().LowLevelDB_Load(table, id, GuildSettings.class);
        if (hell == null){
            throw new NullPointerException("No entry in database!");
        } else {
            return (GuildSettings) hell;
        }
    }

    private void saveGuildSettings(GuildSettings s){
        Database.dbdriver.getDriver().LowLevelDB_Save(s, s.getClass(), table);
    }

    public GuildSettings makeNewGuildSettings(String id){
        return new GuildSettings(id);
    }

    public void save(GuildSettings e){
        // update the prefix
        this.cachedPrefixes.remove(e.getGuildId());
        this.cachedPrefixes.put(e.getGuildId(), e.getPrefix());
        saveGuildSettings(e);
    }
}
