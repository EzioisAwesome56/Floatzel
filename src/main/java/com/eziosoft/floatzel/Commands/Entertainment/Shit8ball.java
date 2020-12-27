package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Shit8ball extends FCommand {

    public Shit8ball() {
        name = "8ball";
        description = "ask the assholey 8ball a question";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent h) {
        Random random = new Random();
        // yes responses
        String[] notassball = {"sure!", "no", "heck off and try again", "I don't care mate", "go away", "yes, now leave", "no no no no no no",
        "damn right", "nope!", "fbi's at my door, please try again", "why are you asking me???????"};
        // assuming this is how you check if the message is too long
        if (h.getArgs().length() < 0 || h.getArgs().length() == 0) {
            h.reply("You appear to have forgotten to ask me something");
        } else if (h.getArgs().length() > 1000) {
            h.reply("that question is too long");
        } else {
            // get the question by combining arguments apparently (this is so fucking simple holy shit)
            String question = h.getArgs();
            // generate a random number
            int thonk = random.nextInt(16)+1;
            // get the response
            String shit;
            String response;
            shit = notassball[random.nextInt(notassball.length)];
            // form the message
            response = "**You asked:** " + question + "\n**Answer:** " + shit;
            // finally: send the message
            h.getChannel().sendMessage(response).queue();

        }
    }
}
