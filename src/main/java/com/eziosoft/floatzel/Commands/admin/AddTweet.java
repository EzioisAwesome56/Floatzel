package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;

public class AddTweet extends FCommand {
    public AddTweet(){
        name = "addtweet";
        description = "Adds a tweet to the tweet database";
        ownerCommand = true;
        category = owner;
    }
}
