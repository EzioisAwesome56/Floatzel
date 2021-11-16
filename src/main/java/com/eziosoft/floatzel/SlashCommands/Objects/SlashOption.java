package com.eziosoft.floatzel.SlashCommands.Objects;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SlashOption {
    private OptionType optype;
    private String help;
    private String name;
    private boolean required = false;

    public SlashOption(OptionType t, String help, String name){
        this.optype = t;
        this.help = help;
        this.name = name;
    }

    public SlashOption(OptionType t, String help, String name, boolean required){
        this.optype = t;
        this.help = help;
        this.name = name;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public OptionType getOptype() {
        return optype;
    }

    public String getHelp() {
        return help;
    }

    public String getName() {
        return name;
    }
}
