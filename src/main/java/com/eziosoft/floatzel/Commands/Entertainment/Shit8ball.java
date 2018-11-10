package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Shit8ball extends FCommand {
    // code slightly based on kekbot's, sorry godson i needed a refrence
    private final Random random = new Random();

    public Shit8ball() {
        name = "8ball";
        description = "ask the assholey 8ball a question";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent h) {
        Random random = new Random();
        // yes responses
        String[] ball = {"Of course its yes you dipshit", "Its not that fucking hard to figure out that its true", "Fuck you, its yes", "Let me answer this question with another question: is saltypepper gay? (Spoiler: yes!)",
        "YOUR MOM IS FUCKING GAY, also the answer is yes but thats not fucking important", "Fuck off mr wrongface", "Kill yourself you wrong-ass person!", "The fucking answer is no", "you wouldn\'t be GAY if this was right", "Jump off a cliff moron, its WRONG!",
                "Im dying on the inside, try again later", "I don't like you, ask again when you have my respect", "Eating pant, please try again",
                "What was that?! I CANT FUCKING HEAR YOU! Please try again", "Kill yourself and try again"};
        // assuming this is how you check if the message is too long
        if (h.getArgs().length() < 0 || h.getArgs().length() == 0) {
            h.getChannel().sendMessage("Hey dipshit, you fucking forgot to ask me something!").queue();
        } else if (h.getArgs().length() > 1000) {
            h.getChannel().sendMessage("fucking asshole, that question is way to **long**, try a shorter one instead asswipe").queue();
        } else {
            // get the question by combining arguments apparently (this is so fucking simple holy shit)
            String question = h.getArgs();
            // generate a random number
            int thonk = random.nextInt(16)+1;
            // get the response
            String shit = ball[random.nextInt(ball.length)];
            // form the message
            String response = "**You asked:** "+question+"\n**Answer:** "+shit;
            // finally: send the message
            h.getChannel().sendMessage(response).queue();

        }
    }
}
