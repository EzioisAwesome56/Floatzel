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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Pixel extends FSlashableImageCommand {
    public Pixel(){
        name = "pixel";
        help = "converts your image into a single pixel!";
        category = FCommand.image;
        sag = SlashActionGroup.IMAGE;
    }

    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException {
        // basically just clone shrink
        // but set the size to be 1x1`
        event.getChannel().sendFile(genImage(ImageIO.read(new ByteArrayInputStream(dink))), "single_pixel_big.png").queue();
    }

    private byte[] genImage(BufferedImage in) throws IOException, InterruptedException, IM4JavaException {
        BufferedImage what;
        InputStream source;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        IMOperation op = new IMOperation();
        // create the 1 pixel big image
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        cmd.setOutputConsumer(pipeOut);
        what = in;
        op.addImage();
        op.format("png");
        op.resize(1,1);
        op.addImage("png:-");
        cmd.run(op, what);
        stream.flush();
        // make it big so people can actually see it
        source = new ByteArrayInputStream(stream.toByteArray());
        stream.reset();
        cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        cmd.setOutputConsumer(pipeOut);
        op = new IMOperation();
        op.addImage();
        op.resize(100, 100);
        op.addImage("png:-");
        cmd.run(op, ImageIO.read(source));
        stream.flush();
        source.close();
        return stream.toByteArray();
    }

    @Override
    protected void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff) {
        try {
            event.getHook().editOriginal(genImage(stuff), "single_pixel_big.png").queue();
        } catch (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
