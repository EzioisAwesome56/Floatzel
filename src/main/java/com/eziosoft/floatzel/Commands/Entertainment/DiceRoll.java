package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class DiceRoll extends FCommand {
    private final Random random = new Random();
    public DiceRoll(){
        name = "dice";
        description = "rolls a virtual dice for you";
    }

    @Override
    protected void execute(CommandEvent event){
        Random random = new Random();
    }
}
