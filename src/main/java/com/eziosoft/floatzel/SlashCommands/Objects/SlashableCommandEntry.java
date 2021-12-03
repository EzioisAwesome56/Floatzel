package com.eziosoft.floatzel.SlashCommands.Objects;

import java.util.Objects;

public class SlashableCommandEntry {
    private BaseActionGroups group;
    private String name;

    public SlashableCommandEntry(BaseActionGroups g, String n){
        this.group = g;
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public BaseActionGroups getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlashableCommandEntry that = (SlashableCommandEntry) o;
        return group == that.group && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, name);
    }
}
