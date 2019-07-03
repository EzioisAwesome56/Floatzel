package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

public class AddTweet extends FCommand {
    public AddTweet(){
        name = "addtweet";
        description = "Adds a tweet to the tweet database";
        adminCommand = true;
        category = admin;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        String tweet = event.getArgs();
        if (tweet.length() > 280){
            event.getChannel().sendMessage("That tweet is too long "+ Utils.getInsult() + "!").queue();
            return;
        } else if (tweet.isEmpty()){
            event.getChannel().sendMessage("You didnt give me a tweet"+ Utils.getInsult() +"!").queue();
            return;
        }
        // save the tweet
        int id = Database.dbcounttweets() + 1;
        boolean a = Database.dbsavetweet(tweet, id);
        if (a){
            event.getChannel().sendMessage("Tweet saved!").queue();
            return;
        }
    }
}
