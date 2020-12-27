package com.eziosoft.floatzel.Commands.PayForCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BetterLoan extends FCommand {
    public BetterLoan(){
        name = "betterloan";
        description = "Higher payout for loans";
        category = buyshit;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws GenericException, DatabaseException{
        String uid = event.getAuthor().getId();
        //check if they havent bought the command yet
        try {
            if (!Database.dbcheckbloan(uid)) {
                event.getChannel().sendMessage("You didn't buy this command yet!").queue();
                return;
            }
        } catch (DatabaseException e){
            throw e;
        }
        // lets be lazy as shit!
        // copy pasta'a from loan.java
        // check to see if the user doesnt have a db entry, if they dont, make one
        try {
            if (!Database.dbcheckifexist(uid)) {
                System.out.println("New bank account created!");
            }
        } catch (DatabaseException e){
            throw e;
        }
        // check if the user has a bank loan, if they havent claimed one at all, make one!
        boolean uwhat = Database.dbcheckifloan(uid);
        if (!uwhat){
            try {
                Database.dbdefaultsave(uid, 2);
            } catch (GenericException e){
                throw e;
            }
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
            event.getChannel().sendMessage("Error: You must wait "+rentime+"longer to get another loan").queue();
            return;
        }
        int payout = random.nextInt(500) + 67;
        event.getChannel().sendMessage("You took out a loan of "+Integer.toString(payout)+"\uD83E\uDD56 !").queue();
        int bal = Database.dbloadint(uid);
        bal = bal + payout;
        Database.dbsave(uid, Integer.toString(bal));
        long takeout = System.currentTimeMillis();
        // save the time the loan wa taken out
        Database.dbsavetime(uid, takeout);
    }
}