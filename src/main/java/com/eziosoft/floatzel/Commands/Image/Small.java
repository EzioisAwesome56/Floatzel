package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.im4java.core.IM4JavaException;

import java.io.IOException;

public class Small extends FImageCommand {
    public Small(){
        name = "small";
        description = "makes an image half as big";
        category = image;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException {
        // do stuff here
    }
}
