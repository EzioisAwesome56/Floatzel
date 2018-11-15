package com.eziosoft.floatzel.Commands.PayForCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.ObjectUtils;

public class Tag extends FCommand {
    public Tag(){
        name = "tag";
        help = "adds a tag to the server tag list";
        category = buyshit;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String args = "";
        try {
            args = argsplit[0];
        } catch (ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Error: no arguments provided!").queue();
            return;
        }
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
            } catch (ArrayIndexOutOfBoundsException e){
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
            } else if(!Database.dbchecktag(event.getGuild().getId(), tn)){
                event.getChannel().sendMessage("Error: a tag with that name already exists!").queue();
                return;
            } else if(howlong < 3){
                event.getChannel().sendMessage("Error: you did not specify tag contents dumbass!").queue();
                return;
            } else if (Database.dbloadint(event.getAuthor().getId()) < 50){
                String curbal = Integer.toString((Database.dbloadint(event.getAuthor().getId())));
                event.getChannel().sendMessage("Error: adding a tag costs 50"+moneyicon+", You have "+curbal+moneyicon+"!").queue();
                return;
            } else if (tn.length() > 50){
                event.getChannel().sendMessage("Error: tag names cannot be fucking longer then 50 characters!").queue();
                return;
            } else {
                // if we made it here, its probably okay to add the tag
                // form the tag content
                String content = "";
                int total = howlong - 2;
                int a = 0;
                try {
                    while (a != total) {
                        content = content + argsplit[2 + a];
                        a++;
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    Error.Catch(e);
                    return;
                }
                // last minute check, see if the tag is too long
                if (content.length() > 1500){
                    event.getChannel().sendMessage("Error: that tag is too long! tags can be 1500 characters long at maximun, moron!").queue();
                    return;
                }
                // subtract 50 from their money
                int newbal = Database.dbloadint(event.getAuthor().getId()) - 50;
                Database.dbsaveint(event.getAuthor().getId(), newbal);
                // then add the new tag
                Database.dbsavetag(event.getGuild().getId(), tn, content);
                event.getChannel().sendMessage("Tag added successfully!").queue();
                return;
            }
        } else if (args.equals("remove")){
            event.getChannel().sendMessage("unfinished feature!").queue();
            return;
        } else {
            // check if the tag exists for the guild
            boolean exist = Database.dbchecktag(event.getGuild().getId(), args);
            if (!exist){
                event.getChannel().sendMessage("Error: you can't delete a tag that doesn't exist, you idiot!").queue();
                return;
            }
        }

    }
}
