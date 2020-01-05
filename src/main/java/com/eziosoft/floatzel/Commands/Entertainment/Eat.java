package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class Eat extends FCommand {
    public Eat(){
        name = "eat";
        description = "makes the bot try to fucking eat something";
        category = fun;
        aliases = Utils.makeAlias("vore");
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String[] thonk = {"This is your fucking foot you jackass",
                "Tastes like fucking horse shit when you give a fucking horse fucking WEED",
                "Get a better fucking cook jackass",
                "I don't Think this is fucking inhalable",
                "it's fucking okay BUT MAKE IT BETTER DUMBASS",
                "Are you sure you didn't just fucking buy this from the store asshole?",
                "Fuck, this is pretty good stuff, too bad it took you 5 years to make it arsehole",
                "Fuck you, I'm going to apply for a copyright on this fucking food because it's so fucking good but made by a fuckwit",
                "Dank weed(tm) is the only thing better."};
        String[] notassthonk = {"Hey wait this is your foot!",
                "This tastes like a horse on some heavy drugs",
                "You may want to consider hiring a better chef",
                "this food is not consumable no matter what you say",
                "it's not the worst fucking food I've eaten",
                "I suspect you bought this instead of cooking it",
                "Fuck man, this is good stuff!",
                "I am going to apply for a patent on this food, sorry mate",
                "Dank weed(tm) is the only thing better."};
        int h = thonk.length;
        int rate = random.nextInt(h);
        String good = "\uD83C\uDF5E";
        String bad = "\uD83D\uDC80";
        String food = event.getArgs();
        String didnoteat;
        if (ass) {
            didnoteat = "**Did I eat it?** NO!\n**Why not maaaaannnnn?** Because your food is fucking gay and looks like fucking dog shit you fucking idiot fuckface!";
        } else {
            didnoteat = "**Did I eat it?** NO!\n **Why the fuck not?** I was busy, sorry!";
        }
        int dideat = random.nextInt(5);
        if (food.length() < 1){
            if (ass) {
                event.getChannel().sendMessage("You didn't give me anything to fucking eat dipshit!").queue();
            } else {
                event.reply("You forgot to give me something to eat");
            }
            return;
        } else if (food.length() > 1500 || food.length() == 1500){
            event.getChannel().sendMessage("That is way to fucking long to be food wtf").queue();
        } else {
            if (dideat == 3){
                event.getChannel().sendMessage(didnoteat).queue();
                return;
            } else {
                // start making the bar nerd
                String bar = Utils.genBar(good, bad, 7, rate < 8 ? rate : 7);
                // form the message
                String msg;
                if (ass){
                    msg = "**You gave me: **" + food + "\n**Did I eat it?** Yes\n**Rating:** " + bar + "\n**Thoughts:** " + thonk[rate];
                } else {
                    msg = "**You gave me: **" + food + "\n**Did I eat it?** Yes\n**Rating:** " + bar + "\n**Thoughts:** " + notassthonk[rate];
                }
                event.getChannel().sendMessage(msg).queue();
                return;
            }
        }

    }
}
