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
        String[] ball = {"Of course its yes you dipshit", "Its not that fucking hard to figure out that its true", "Fuck you, its yes", "haha funny yes",
        "YOUR MOM IS FUCKING GAY, also the answer is yes but thats not fucking important", "Fuck off mr wrongface", "<redacted> you wrong-ass person!", "The fucking answer is no", "you wouldn\'t be GAY if this was right", "Jump off a sofa its WRONG!",
                "Im dying on the inside, try again later", "I don't like you, ask again when you have my respect", "Eating pant, please try again",
                "What was that?! I CANT FUCKING HEAR YOU! Please try again", "Kill yourself and try again"};
        String[] notassball = {"sure!", "no", "heck off and try again", "I don't care mate", "go away", "yes, now leave", "no no no no no no",
        "damn right", "nope!", "fbi's at my door, please try again", "why are you asking me???????"};
        // assuming this is how you check if the message is too long
        if (h.getArgs().length() < 0 || h.getArgs().length() == 0) {
            if (ass) {
                h.getChannel().sendMessage("Hey dipshit, you fucking forgot to ask me something!").queue();
            } else {
                h.reply("You appear to have forgotten to ask me something");
            }
        } else if (h.getArgs().length() > 1000) {
            if (ass) {
                h.getChannel().sendMessage("fucking asshole, that question is way to **long**, try a shorter one instead asswipe").queue();
            } else {
                h.reply("that question is too long");
            }
        } else {
            // get the question by combining arguments apparently (this is so fucking simple holy shit)
            String question = h.getArgs();
            // generate a random number
            int thonk = random.nextInt(16)+1;
            // get the response
            String shit;
            String response;
            if (ass) {
                shit = ball[random.nextInt(ball.length)];
                // form the message
                response = "**You asked:** " + question + "\n**Answer:** " + shit;
            } else {
                shit = notassball[random.nextInt(notassball.length)];
                // form the message
                response = "**You asked:** " + question + "\n**Answer:** " + shit;
            }
            // finally: send the message
            h.getChannel().sendMessage(response).queue();

        }
    }
}
