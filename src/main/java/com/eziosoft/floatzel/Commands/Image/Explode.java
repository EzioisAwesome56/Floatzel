package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Explode extends FImageCommand {
    public Explode(){
        name = "explode";
        description = "Explode your dumbass images!";
        category = image;
        aliases = Utils.makeAlias("bang");
    }

    @Override
    protected void imageRun(CommandEvent event, InputStream source){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        // actual image shit
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        IMOperation op = new IMOperation();
    }
}
