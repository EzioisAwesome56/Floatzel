package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Girlfriend extends FCommand {
    private final Random random = new Random();
    public Girlfriend(){
        name = "gf";
        description = "Rates your bitchy girlfriend";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // ratings
        String[] rate = {"I do not have the fucking words for how shitty that pile of fucking dogshit is, you fucking shit fucker",
                "That isn't a girl, that's fucking dog shit",
                "Dude wtf get that bitch out of your life",
                "She's still a pile of fucking shit though",
                "Of course she's a 4, you could do so much fucking better dumbass",
                "It's midrange, she's not good, not bad, okay.",
                "She's pretty fucking good man, don't throw her away!",
                "This person would make the world a better fucking place man",
                "Damn, I wish my fucking bitch was more like this one",
                "Holy shit she's fucking hot, don't just sit on your ass!",
                "Why the fuck haven't you married this perfect woman yet you dumbass?",
                "HOLY SHIT IT'S GOD"};
        // generate a rating score
        int rating = random.nextInt(12);
        String name = event.getArgs();
        String bad = rating > 0 ? "\uD83D\uDC80" : "\uD83D\uDCA3";
        String good = "❤";
        // checks to see if someone is trying to be dumb
        if (name.length() < 1){
            event.getChannel().sendMessage("You didn't enter a name you fuck face!").queue();
            return;
        } else if (name.length() > 1500 || name.length() == 1500){
            event.getChannel().sendMessage("That fucking name is way to fucking long you fuckwit!").queue();
            return;
        }
        // start making the bar
        String bar = Utils.genBar(good, bad, 10, rating < 11 ? rating : 10);
        // next, form the message itself
        String msg = "**Girl: **"+name+"\n**Rating: **"+bar+"\n**Thoughts: **"+rate[rating];
        //event.getChannel().sendMessage(msg).queue();
        event.reply(msg);
    }
}
