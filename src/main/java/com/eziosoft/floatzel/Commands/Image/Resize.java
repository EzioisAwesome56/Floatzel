package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Resize extends FCommand {
    public Resize(){
        name = "resize";
        description = "resizes an image to be small";
        category = other;
    }

    protected void cmdrun(CommandEvent event){
        // stuff
    }
}
