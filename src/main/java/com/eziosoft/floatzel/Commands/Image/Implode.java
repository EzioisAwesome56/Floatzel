package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
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

public class Implode extends FImageCommand {
    public Implode(){
        name = "implode";
        description = "Fucking explosions but REVERSE!";
        category = image;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, IM4JavaException, InterruptedException {
        // setup imagemagick
        InputStream source = new ByteArrayInputStream(dink);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        // actual image shit
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        IMOperation op = new IMOperation();
        cmd.setOutputConsumer(pipeOut);
        // then start the actual image shit
        op.addImage();
        op.format("png");
        op.implode((double) 1);
        op.addImage("png:-");
        try {
            cmd.run(op, ImageIO.read(source));
            stream.flush();
            event.getChannel().sendFile(stream.toByteArray(), "implode.png").queue();
            stream.close();
            source.close();
        } catch (IOException | IM4JavaException | InterruptedException e){
            throw e;
        }
    }
}
