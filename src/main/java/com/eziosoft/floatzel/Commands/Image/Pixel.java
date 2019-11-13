package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Commands.FImageCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.InputStream;

public class Pixel extends FImageCommand {
    public Pixel(){
        name = "pixel";
        help = "converts your image into a single pixel!";
        category = FCommand.image;
    }

    protected void imageRun(CommandEvent event, InputStream Source){
        // do stuff here
    }
}
