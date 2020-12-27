package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Boyfriend extends FCommand {
    public Boyfriend(){
        name = "bf";
        description = "Rates your a boyfriend";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // rattings for if asshole mode is off
        String[] notassrate = {"I don't think this is a boy...",
                "This guy, well, isn't great",
                "honestly, hes comparable to a trash can",
                "He smells like actual horse shit, you know",
                "Honestly, this guy isn't much better then a 4",
                "I have no options, im outta here",
                "even though he is a 6, hes still pretty shit",
                "Sorry lad, im stealing your man",
                "I don't think slave owners would like him...",
                "I guess hes nice, but that only goes so far",
                "Marry him if you wish, I don't personally support it",
                "3 words: actual horse shit",
                "His penis was tasty - uh I mean FUCK OFF I'M EATING LUNCH"};
        // generate a rating score
        int rating = random.nextInt(12);
        String name = event.getArgs();
        String bad = rating > 0 ? "\uD83D\uDC80" : "\uD83D\uDCA3";
        String good = "❤";
        // checks to see if someone is trying to be dumb
        if (name.length() < 1){
            event.reply("you forgot to enter a name!");
            return;
        } else if (name.length() > 1500 || name.length() == 1500){
            event.reply("That name is way to fucking long!");
            return;
        }
        // start making the bar
        String bar = Utils.genBar(good, bad, 10, rating < 11 ? rating : 10);
        // next, form the message itself
        String msg;
        msg = "**Boy: **" + name + "\n**Rating: **" + bar + "\n**Thoughts: **" + notassrate[rating];
        event.getChannel().sendMessage(msg).queue();

    }
}
