package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Random;

public class DiceRoll extends FSlashableCommand {
    private final Random random = new Random();
    public DiceRoll(){
        name = "dice";
        description = "rolls a virtual dice for you";
        category = fun;
        sag = SlashActionGroup.FUN;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        int h = random.nextInt(6) + 1;
        event.reply(Integer.toString(h));
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        int h = random.nextInt(6) + 1;
        event.getHook().editOriginal(Integer.toString(h)).queue();
    }
}
