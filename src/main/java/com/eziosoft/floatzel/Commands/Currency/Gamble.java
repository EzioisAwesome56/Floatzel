package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.IOException;
import java.util.Random;

public class Gamble extends FCommand {
    private final Random random = new Random();
    public Gamble(){
        name = "gamble";
        description = "test your luck by betting 5 french breads!";
        category = money;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        int args = 0;
        String printarg = event.getArgs().replace("@everyone", "a fucking ping").replace("@here", "a fucking ping");
        try {
            args = Integer.parseInt(event.getArgs());
        } catch (NumberFormatException
                e) {
            if (event.getArgs().length() < 1){
                event.getChannel().sendMessage("You didnt enter a guess jackass!").queue();
            } else if (event.getArgs().length() > 50) {
                event.getChannel().sendMessage("That isnt a number and its also way to fucking big jackass!").queue();
            } else {
                event.getChannel().sendMessage("`" + printarg + "`" + " isnt a number dumbass!").queue();
            }
            return;
        }
        if (args > 5){
            event.getChannel().sendMessage("That guess is not between 1 and 5 jackass!").queue();
            return;
        }
        Random random = new Random();
        int bal = Database.dbloadint(event.getAuthor().getId());
        // check if the user even has a db entry
        if (!Database.dbcheckifexist(event.getMessage().getAuthor().getId())){
            event.getChannel().sendMessage("You dont have enough fucking bread to do this dumbass! You need atleast 5!").queue();
            return;
        } else if (bal < 5){
            event.getChannel().sendMessage("You dont have enough french bread to fucking do this dumbass!").queue();
            return;
        } else {
            bal = bal - 5;
            Database.dbsave(event.getAuthor().getId(), Integer.toString(bal));
        }
        int number = random.nextInt(5) + 1;
        // generate the amount of bread to give to the user
        int payout = (random.nextInt(20) + 1) + 5;
        event.getChannel().sendMessage("**You guessed:** "+Integer.toString(args)+"\n**The number drawn was:** "+Integer.toString(number)).queue();
        if (number == args){
            event.getChannel().sendMessage("**YOU WIN!**\nYou got "+Integer.toString(payout)+"\uD83E\uDD56 as a reward!").queue();
            bal = bal + payout;
            Database.dbsave(event.getAuthor().getId(), Integer.toString(bal));
            return;
        } else {
            event.getChannel().sendMessage("**YOU LOST!**\nTry again next time fucker").queue();
            return;
        }
    }
}
