package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;

public class Tweet extends FCommand {
    public Tweet(){
        name = "tweet";
        description = "for debugging purposes: sends a tweet";
        ownerCommand = true;
        category = owner;
    }
}
