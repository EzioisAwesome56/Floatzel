package com.eziosoft.floatzel.Commands.PayForCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BetterLoan extends FCommand {
    private final Random random = new Random();
    public BetterLoan(){
        name = "betterloan";
        description = "Go to a better fucking bank and take out a loan";
        category = money;
    }

    @Override
    protected void execute(CommandEvent event){
        String uid = event.getAuthor().getId();
        //check if they havent bought the command yet
        if (!Database.dbcheckbloan(uid)){
            event.getChannel().sendMessage("You didnt fucking buy this command yet jackass!").queue();
            return;
        }
        // lets be lazy as shit!
        // copy pasta'a from loan.java
        Random random = new Random();
        // check to see if the user doesnt have a db entry, if they dont, make one
        if (!Database.dbcheckifexist(uid)){
            System.out.println("New bank account created!");
        }
        // check if the user has a bank loan, if they havent claimed one at all, make one!
        boolean uwhat = Database.dbcheckifloan(uid);
        if (!uwhat){
            Database.dbdefaultsave(uid, 2);
        }
        Long prevloan = Database.dbloadtime(uid);
        Long day = Integer.toUnsignedLong(86400000);
        Long passed = System.currentTimeMillis() - prevloan;
        Long passh = day - passed;
        // calculate human readable formats
        long hours = TimeUnit.MILLISECONDS.toHours(passh) -
                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(passh));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(passh) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(passh));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(passh) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(passh));
        if (System.currentTimeMillis() != prevloan + day && prevloan != 0 && System.currentTimeMillis() > prevloan && seconds >= 0L){
            String rentime = "";
            rentime = Long.toString(hours) + " hours, " + Long.toString(minutes) + " minutes and " + Long.toString(seconds) + " seconds ";
            event.getChannel().sendMessage("Error: You must fucking wait "+rentime+"longer to get another loan jackass").queue();
            return;
        }
        int payout = random.nextInt(500) + 67;
        event.getChannel().sendMessage("You fucking took out a loan of fucking "+Integer.toString(payout)+"\uD83E\uDD56 !").queue();
        int bal = Database.dbloadint(uid);
        bal = bal + payout;
        Database.dbsave(uid, Integer.toString(bal));
        long takeout = System.currentTimeMillis();
        // save the time the loan wa taken out
        Database.dbsavetime(uid, takeout, uwhat);
    }
}