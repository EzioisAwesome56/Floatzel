package com.eziosoft.floatzel.Commands.PayForCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Tag extends FCommand {
    public Tag(){
        name = "tag";
        help = "adds a tag to the server tag list";
        category = buyshit;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String args = argsplit[0];
        String tn = null;
        int howlong = argsplit.length;
        System.out.println(howlong);
        boolean perm = Database.dbcheckiftag(event.getGuild().getId());
        if(args.equals("check")){
            if(!perm){
                event.getChannel().sendMessage("This guild has not purchased the tag command!").queue();
                return;
            } else {
                event.getChannel().sendMessage("This guild can use the tag command!").queue();
                return;
            }
        } else if (args.equals("buy")){
            if (!Database.dbcheckifexist(event.getAuthor().getId())){
                event.getChannel().sendMessage("Error: You need atleast 500"+moneyicon+" to buy this command, moron!").queue();
                return;
            }
            int balance = Database.dbloadint(event.getAuthor().getId());
            if (balance < 500){
                event.getChannel().sendMessage("Error: You need atleast 500"+moneyicon+" to buy this command, moron!").queue();
                return;
            } else {
                balance = balance - 500;
                Database.dbsaveint(event.getAuthor().getId(), balance);
                Database.dbsettagperm(event.getGuild().getId());
                event.getChannel().sendMessage("You bought the tag command for the guild!").queue();
                return;
            }
        } else if (args.equals("add")){
            try {
                tn = argsplit[1];
            } catch (NullPointerException e){
                event.getChannel().sendMessage("Error: either something broke or you didn't give me a tag name!").queue();
                return;
            }
            // run checks before actually adding the tag
            if (!perm){
                event.getChannel().sendMessage("Error: this guild has not bought the tag command!").queue();
                return;
            } else if (!Database.dbcheckifexist(event.getAuthor().getId())){
                event.getChannel().sendMessage("Error: adding a tag costs 50"+moneyicon+", You have 0"+moneyicon+"!").queue();
                return;
            } else if(Database.dbchecktag(event.getGuild().getId(), tn)){
                event.getChannel().sendMessage("Error: a tag with that name already exists!").queue();
                return;
            } else if(howlong < 3){
                event.getChannel().sendMessage("Error: you did not specify tag contents dumbass!").queue();
                return;
            } else if (Database.dbloadint(event.getAuthor().getId()) < 50){
                String curbal = Integer.toString((Database.dbloadint(event.getAuthor().getId())));
                event.getChannel().sendMessage("Error: adding a tag costs 50"+moneyicon+", You have "+curbal+moneyicon+"!").queue();
                return;
            }
        }

    }
}
