package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.User;

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
            event.getChannel().sendMessage("You didnt give me any arguments dickface!").queue();
            return;
        }
        String[] args = unsplit.split(" \\| ");
        try {
            rawamount = args[1];
        } catch (ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("You didnt correctly provide fucking arguments").queue();
            return;
        }
        int amount = 0;
        try{
            amount = Integer.valueOf(rawamount);
        } catch (NumberFormatException e){
            event.getChannel().sendMessage(rawamount+" is not a valid number asshole").queue();
            return;
        }
        if (!Database.dbcheckifexist(uid)){
            event.getChannel().sendMessage("You don't have enough fucking french bread to pay someone dipshit").queue();
            return;
        }
        int bal = Database.dbloadint(uid);
        if (bal < 1){
            event.getChannel().sendMessage("YOu don't have enough fucking bread to pay someone jackass!").queue();
            return;
        }
        List<User> mentions = event.getMessage().getMentionedUsers();
        if (mentions.size() < 1){
            event.getChannel().sendMessage("You didnt mention a user dumbass!").queue();
            return;
        }
        String monget = mentions.get(0).getId();
        if (!Database.dbcheckifexist(monget)){
            System.out.println("H");
        }
        // load balances
        int recvbal = Database.dbloadint(monget);
        if (amount > bal){
            event.getChannel().sendMessage("You dont have enough bread to afford sending "+rawamount+" to someone!").queue();
            return;
        }
        if (uid == monget){
            event.getChannel().sendMessage("You cant pay yourself dumbass").queue();
            return;
        }
        if (amount < 0){
            event.getChannel().sendMessage("You cant pay someone minus bread you fuckface moron").queue();
            return;
        }
        if (mentions.get(0).isBot()){
            event.getChannel().sendMessage("You cant fucking pay a robot, they have no fucking use for it dumbass").queue();
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
