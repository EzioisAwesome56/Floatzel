package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.InputStream;

public class Shrink extends FImageCommand {
    public Shrink(){
        name = "shrink";
        help = "shrinks an image";
        category = FCommand.image;
        aliases = Utils.makeAlias("resize");
    }

    protected void imageRun(CommandEvent event, InputStream Source){
        // do stuff here
    }


}
