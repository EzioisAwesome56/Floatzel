package com.eziosoft.floatzel.Commands.Games;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class GuessNumber extends FCommand {
    private final Random random = new Random();
    public GuessNumber(){
        name = "gnumb";
        description = "Play a little game of guessing numbers";
    }

    @Override
    protected void execute(CommandEvent event){
        Random random = new Random();
        // make the number nerd
        //int numb = random.nextInt(10) + 1;
    }
}
