package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Explode extends FImageCommand {
    public Explode(){
        name = "explode";
        description = "Explode your dumbass images!";
        category = image;
        aliases = Utils.makeAlias("bang");
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException{
        InputStream source = new ByteArrayInputStream(dink);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        // actual image shit
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        IMOperation op = new IMOperation();
        // start doing the shitto
        cmd.setOutputConsumer(pipeOut);
        op.addImage();
        op.format("png");
        op.implode((double) -2);
        op.addImage("png:-");
        try {
            cmd.run(op, ImageIO.read(source));
            stream.flush();
            event.getChannel().sendFile(stream.toByteArray(), "explode.png").queue();
            stream.close();
            source.close();
        } catch (IOException | InterruptedException | IM4JavaException e){
            throw e;
        }
    }
}
