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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Wall extends FImageCommand {
    public Wall(){
        name = "wall";
        description = "fucking plaster a image like wallpaper";
        category = image;
    }

    @Override
    protected void imageRun(CommandEvent event, InputStream source) throws IOException, InterruptedException, IM4JavaException {
        // setup im
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        // actual image shit
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        IMOperation op = new IMOperation();
        cmd.setOutputConsumer(pipeOut);
        // then run the shit tm
        op.addImage();
        op.format("png");
        op.resize(128, 128);
        op.virtualPixel("tile");
        op.mattecolor("none");
        op.background("none");
        op.resize(512, 512);
        op.distort("Perspective");
        op.addRawArgs("0,0,57,42  0,128,63,130  128,0,140,60  128,128,140,140");
        op.addImage("png:-");
        try {
            cmd.run(op, ImageIO.read(source));
            stream.flush();
            event.getChannel().sendFile(stream.toByteArray(), "wall.png").queue();
            stream.close();
            source.close();
        } catch (IOException | InterruptedException | IM4JavaException e){
            throw e;
        }

    }
}
