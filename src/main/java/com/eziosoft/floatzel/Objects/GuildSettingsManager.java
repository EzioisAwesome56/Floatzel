package com.eziosoft.floatzel.Objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildSettingsManager {
    private Map<String, String> cachedPrefixes = new HashMap<>();

    public GuildSettingsManager(){
        System.out.println("Floatzel GuildSettings Manager now starting up...");
        GuildSettings f = new GuildSettings();
    }

    private class GuildSettings {
        @PrimaryKey
        private String guildId;

        private boolean commandsEnabled;
        private String prefix;
        private List<String> notAllowedChans;

        public GuildSettings(){};
    }
}
