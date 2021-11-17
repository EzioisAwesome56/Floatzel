package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableImageCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Jpeg extends FSlashableImageCommand {
    public Jpeg() {
        name = "jpeg";
        description = "jpeg-ifys your image";
        category = FCommand.image;
        sag = SlashActionGroup.IMAGE;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException, NullPointerException{
        event.getChannel().sendFile(resizeImage(ImageIO.read(new ByteArrayInputStream(dink))).toByteArray(), "jpeg.jpg").queue();
    }

    // We moved this process into its own method, for both tidiness and since we'll be calling it three times.
    private ByteArrayOutputStream resizeImage(BufferedImage in) throws IOException, InterruptedException, IM4JavaException, NullPointerException {
        BufferedImage what;// prepare for magick stuff
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
        cmd.run(op, in);
        stream.flush();
        InputStream source = new ByteArrayInputStream(stream.toByteArray());
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
        return stream;
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff) {
        try {
            event.getHook().sendFile(resizeImage(stuff).toByteArray(), "jpeg.jpg").queue();
        } catch (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
