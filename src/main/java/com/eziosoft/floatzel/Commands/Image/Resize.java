package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.io.IOUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class Resize extends FImageCommand {
    public Resize() {
        name = "jpeg";
        description = "jpeg-ifys your image";
        category = FCommand.image;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException, NullPointerException{
        try {
            resizeImage(event, new ByteArrayInputStream(dink));
        } catch (IOException | InterruptedException | IM4JavaException | NullPointerException e){
            throw e;
        }
    }

    // We moved this process into its own method, for both tidiness and since we'll be calling it three times.
    private void resizeImage(CommandEvent event, InputStream source) throws IOException, InterruptedException, IM4JavaException, NullPointerException {
        BufferedImage what;// prepare for magick stuff
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Pipe pipeOut = new Pipe(null, stream);
            // actual image shit
            ConvertCmd cmd = new ConvertCmd();
            if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
            IMOperation op = new IMOperation();
            op.addImage();
            // first round of jpeg
            op.format("jpg");
            op.quality((double) 1);
            cmd.setOutputConsumer(pipeOut);
            op.addImage("jpg:-");
            cmd.run(op, ImageIO.read(source));
            stream.flush();
            source.reset();
            source = new ByteArrayInputStream(stream.toByteArray());
            stream.reset();
            // shrinking the image!
            cmd = new ConvertCmd();
            if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
            cmd.setOutputConsumer(pipeOut);
            op = new IMOperation();
            what = ImageIO.read(source);
            int height = what.getHeight();
            int width = what.getWidth();
            op.addImage();
            op.format("jpg");
            op.resize(200,200);
            op.addImage("jpg:-");
            cmd.run(op, what);
            stream.flush();
            // next jpeg
            event.getChannel().sendTyping().queue();
            source.reset();
            source = new ByteArrayInputStream(stream.toByteArray());
            stream.reset();
            cmd = new ConvertCmd();
            if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
            cmd.setOutputConsumer(pipeOut);
            op = new IMOperation();
            op.addImage();
            op.format("jpg");
            op.quality((double) 1);
            op.addImage("jpg:-");
            cmd.run(op, ImageIO.read(source));
            stream.flush();
            // finally: resize it back to original size
            source.reset();
            source = new ByteArrayInputStream(stream.toByteArray());
            stream.reset();
            cmd = new ConvertCmd();
            if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
            cmd.setOutputConsumer(pipeOut);
            op = new IMOperation();
            op.addImage();
            op.resize(width, height);
            op.addImage("jpg:-");
            cmd.run(op, ImageIO.read(source));
            stream.flush();
            event.getChannel().sendFile(stream.toByteArray(), "wat.jpg").queue();
            stream.close();
            source.close();
        } catch (IOException | InterruptedException | IM4JavaException | NullPointerException e){
            throw e;
        }
    }
}
