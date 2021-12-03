package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class Girlfriend extends FSlashableCommand {
    public Girlfriend(){
        name = "gf";
        description = "Rates your girlfriend";
        category = fun;
        sag = SlashActionGroup.FUN;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String name = event.getArgs();
        if (name.length() < 1){
            event.reply("you forgot to enter a name!");
            return;
        } else if (name.length() > 1500 || name.length() == 1500){
            event.reply("that name is far too long");
            return;
        }

        event.reply(genMsg(name));
    }

    private String genMsg(String in){
        in = in.replace("@everyone", "stupid ping").replace("@here", "stupid ping");
        // ratings
        String[] notassrate = {"Words cannot describe how bad this person is",
                "I don't think that's a girl, yo",
                "my advice: get her out of your life",
                "She's still not ideal mind you!",
                "I suppose you could do much worse then her",
                "It's midrange, she's not good, not bad, okay.",
                "Don't let this one slip away bro!",
                "This person would make the world a better fucking place man",
                "I wish I had a girlfriend like this one",
                "Don't just sit there my guy!",
                "Why the fuck haven't you married her yet?",
                "HOLY SHIT IT'S GOD"};
        // generate a rating score
        int rating = random.nextInt(12);
        String bad = rating > 0 ? "\uD83D\uDC80" : "\uD83D\uDCA3";
        String good = "❤";
        // checks to see if someone is trying to be dumb
        // start making the bar
        String bar = Utils.genBar(good, bad, 10, rating < 11 ? rating : 10);
        // next, form the message itself
        String msg;
        msg = "**Girl: **"+in+"\n**Rating: **"+bar+"\n**Thoughts: **"+notassrate[rating];
        return msg;
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (event.getOption("arg") == null){
            event.getHook().editOriginal("You did not provide anything for me to rate!").queue();
        } else if (event.getOption("arg").getAsString().length() > 200){
            event.getHook().editOriginal("that is simply too  long for me to rate!").queue();
        } else {
            event.getHook().editOriginal(genMsg(event.getOption("arg").getAsString())).queue();
        }
    }
}
