package com.eziosoft.floatzel.Commands.Asshole;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.CommdLogic.SwearCombineLogic;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class SwearCombine extends FCommand {
    public SwearCombine(){
        name = "gensent";
        description = "Generates a sentance";
        category = asshole;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage(SwearCombineLogic.makeMessage()).queue();
    }
}
