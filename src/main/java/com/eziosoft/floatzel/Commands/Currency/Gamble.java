package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
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
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        int args = 0;
        String printarg = event.getArgs().replace("@everyone", "a fucking ping").replace("@here", "a fucking ping");
        try {
            args = Integer.parseInt(event.getArgs());
        } catch (NumberFormatException
                e) {
            if (event.getArgs().length() < 1){
                if (ass) {
                    event.getChannel().sendMessage("You didnt enter a guess jackass!").queue();
                } else {
                    event.reply("you forgot to enter a guess!");
                }
            } else if (event.getArgs().length() > 50) {
                if (ass) {
                    event.getChannel().sendMessage("That isnt a number and its also way to fucking big jackass!").queue();
                } else {
                    event.reply("That message is far too long, please try something shorter!");
                }
            } else {
                if (ass) {
                    event.getChannel().sendMessage("`" + printarg + "`" + " isnt a number dumbass!").queue();
                } else {
                    event.reply("That is not even a number, please try again!");
                }
            }
            return;
        }
        if (args > 5){
            if (ass) {
                event.getChannel().sendMessage("That guess is not between 1 and 5 jackass!").queue();
            } else {
                event.reply("That guess is not between 1 and 5!");
            }
            return;
        }
        Random random = new Random();
        int bal = Database.dbloadint(event.getAuthor().getId());
        // check if the user even has a db entry
        if (!Database.dbcheckifexist(event.getMessage().getAuthor().getId())){
            if (ass) {
                event.getChannel().sendMessage("You dont have enough fucking bread to do this dumbass! You need atleast 5!").queue();
            } else {
                event.reply("You need atleast 5"+ moneyicon+ " to do this, but you don't have any!");
            }
            return;
        } else if (bal < 5){
            if (ass) {
                event.getChannel().sendMessage("You dont have enough french bread to fucking do this dumbass!").queue();
            } else {
                event.reply("you do not have enough "+moneyicon+" to afford this!");
            }
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
            if (ass) {
                event.getChannel().sendMessage("**YOU LOST!**\nTry again next time fucker").queue();
            } else {
                event.reply("**YOU LOST!**\naw, darn. perhaps you should try again?");
            }
            return;
        }
    }
}
