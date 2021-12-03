package com.eziosoft.floatzel.Objects;

import java.util.HashMap;
import java.util.Map;

public class GuildSettingsManager {
    private Map<String, String> cachedPrefixes = new HashMap<>();

    public GuildSettingsManager(){
        System.out.println("Floatzel GuildSettings Manager now starting up...");
    }
}
