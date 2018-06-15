package com.eziosoft.floatzel.Commands.Asshole;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class SwearCombine extends FCommand {
    private final Random random = new Random();
    public SwearCombine(){
        name = "gensent";
        description = "Generates a sentance";
        category = asshole;
    }

    @Override
    protected void execute(CommandEvent event){
        Random random = new Random();;
        String[] words = {"fuck", "ass", "shit", "kill yourself", "die", "fucking", "hecking", "you", "nibba", "gay ass pingu", "fucktart", "asswipe", "fuckhead", "asshat", "shitface",
        "assholefuckface", "dickface", "fucktard", "fuckfart", "nigger"};
        int maxnumb = words.length;
        int sentlong = random.nextInt(10) + 1;
        int count = 0;
        String msg = "";
        // form the sentance
        while (count != sentlong){
            msg = msg + words[random.nextInt(maxnumb)] + " ";
            count = count + 1;
        }
        // sennd it
        event.getChannel().sendMessage(msg).queue();
    }
}
