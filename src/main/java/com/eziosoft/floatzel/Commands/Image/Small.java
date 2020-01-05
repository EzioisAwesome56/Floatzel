package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Small extends FImageCommand {
    public Small(){
        name = "small";
        description = "makes an image half as big";
        category = image;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException {
        InputStream source = new ByteArrayInputStream(dink);
        BufferedImage what;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        IMOperation op = new IMOperation();
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        cmd.setOutputConsumer(pipeOut);
        what = ImageIO.read(source);
        // get the new image size
        int width = what.getWidth() / 2;
        int height = what.getHeight() / 2;
        op.addImage();
        op.format("jpg");
        op.resize(width,height);
        op.addImage("jpg:-");
        cmd.run(op, what);
        stream.flush();
        event.getChannel().sendFile(stream.toByteArray(), "small.jpg").queue();
        stream.close();
        source.close();
    }
}
