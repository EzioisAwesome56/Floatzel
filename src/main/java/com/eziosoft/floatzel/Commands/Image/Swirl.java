package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import sun.swing.SwingLazyValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Swirl extends FImageCommand {
    public Swirl(){
        name = "swirl";
        help = "slap a swirl onto that image my guy";
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
        op.addImage();
        op.format("png");
        op.swirl((double) 180);
        op.addImage("png:-");
        cmd.run(op, what);
        stream.flush();
        event.getChannel().sendFile(stream.toByteArray(), "small.jpg").queue();
        stream.close();
        source.close();
    }
}
