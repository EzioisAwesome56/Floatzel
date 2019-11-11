package com.eziosoft.floatzel.Commands.Asshole;

import com.eziosoft.floatzel.Commands.FCommand;
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
        // why did i think the slack support was a good idea? it wasnt.
        Random random = new Random();;
        String[] words = {"fuck", "ass", "shit", "what", "die", "fucking", "hecking", "you", "nibba", "gay ass pingu", "fucktart", "asswipe", "fuckhead", "asshat", "shitface",
                "assholefuckface", "dickface", "fucktard", "fuckfart", "nibba"};
        int maxnumb = words.length;
        int sentlong = random.nextInt(10) + 1;
        int count = 0;
        String msg = "";
        // form the sentance
        while (count != sentlong){
            msg = msg + words[random.nextInt(maxnumb)] + " ";
            count = count + 1;
        }
        // return it
        if (!Floatzel.joke) {
            event.getChannel().sendMessage(msg).queue();
        } else {
            event.reply("** **");
        }
    }
}
