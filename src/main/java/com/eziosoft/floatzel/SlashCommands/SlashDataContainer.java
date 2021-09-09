package com.eziosoft.floatzel.SlashCommands;

import java.util.Objects;

public class SlashDataContainer {

    private final String guildid;
    private final String name;

    public SlashDataContainer(String n, String g){
        this.guildid = g;
        this.name = n;
    }

    public final String getGuildid() {
        return guildid;
    }

    public final String getName() {
        return name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlashDataContainer that = (SlashDataContainer) o;
        return guildid.equals(that.guildid) && name.equals(that.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(guildid, name);
    }
}
