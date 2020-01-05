package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class Pay extends FCommand {
    public Pay(){
        name = "pay";
        description = "give you friend some of your fucking french bread";
        category = money;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        String uid = event.getAuthor().getId();
        String unsplit = event.getArgs();
        String rawamount = "";
        if (unsplit.isEmpty()){
            if (ass) {
                event.getChannel().sendMessage("You didnt give me any arguments dickface!").queue();
            } else {
                event.reply("You forgot to provide arguments for this command!");
            }
            return;
        }
        String[] args = unsplit.split(" \\| ");
        try {
            rawamount = args[1];
        } catch (ArrayIndexOutOfBoundsException e){
            if (ass) {
                event.getChannel().sendMessage("You didn't correctly provide fucking arguments").queue();
            } else{
                event.reply("You provided invalid command arguments!");
            }
            return;
        }
        int amount = 0;
        try{
            amount = Integer.valueOf(rawamount);
        } catch (NumberFormatException e){
            if (ass) {
                event.getChannel().sendMessage(rawamount + " is not a valid number asshole").queue();
            } else {
                event.reply(rawamount + "is not a valid number, please try again");
            }
            return;
        }
        if (!Database.dbcheckifexist(uid)){
            if (ass) {
                event.getChannel().sendMessage("You don't have enough fucking french bread to pay someone dipshit").queue();
            } else {
                event.reply("you do not have any "+moneyicon+" to pay someone!");
            }
            return;
        }
        int bal = Database.dbloadint(uid);
        if (bal < 1){
            event.getChannel().sendMessage("YOu don't have enough fucking bread to pay someone jackass!").queue();
            return;
        }
        List<User> mentions = event.getMessage().getMentionedUsers();
        if (mentions.size() < 1){
            if (ass) {
                event.getChannel().sendMessage("You didnt mention a user dumbass!").queue();
            } else {
                event.reply("you forgot to mention who you want to pay!");
            }
            return;
        }
        String monget = mentions.get(0).getId();
        if (!Database.dbcheckifexist(monget)){
            System.out.println("bank account created for user who is going to be paid");
        }
        // load balances
        int recvbal = Database.dbloadint(monget);
        if (amount > bal){
            event.getChannel().sendMessage("You dont have enough bread to afford sending "+rawamount+" to someone!").queue();
            return;
        }
        if (uid == monget){
            if (ass) {
                event.getChannel().sendMessage("You cant pay yourself dumbass").queue();
            } else {
                event.reply("You cannot pay yourself, sorry");
            }
            return;
        }
        if (amount < 0){
            if (ass) {
                event.getChannel().sendMessage("You cant pay someone minus bread you fuckface moron").queue();
            } else {
                event.reply("No, you cannot give someone negative "+moneyicon);
            }
            return;
        }
        if (mentions.get(0).isBot()){
            if (ass) {
                event.getChannel().sendMessage("You cant fucking pay a robot, they have no fucking use for it dumbass").queue();
            } else {
                event.reply("While im sure they would like it, bots have no use for "+moneyicon);
            }
            return;
        }
        // alright fuck the error walls, lets go
        bal = bal - amount;
        recvbal = recvbal + amount;
        String newbal = Integer.toString(bal);
        String oofbal = Integer.toString(recvbal);
        // save back to the db
        Database.dbsave(uid, newbal);
        Database.dbsave(monget, oofbal);
        event.getChannel().sendMessage("You paid "+rawamount+"\uD83E\uDD56 to user "+mentions.get(0).getName()+"!").queue();
    }
}
