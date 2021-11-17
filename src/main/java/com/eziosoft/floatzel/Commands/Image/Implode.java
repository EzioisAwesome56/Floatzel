package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableImageCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
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

public class Implode extends FSlashableImageCommand {
    public Implode(){
        name = "implode";
        description = "explosions but REVERSE!";
        category = image;
        sag = SlashActionGroup.IMAGE;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, IM4JavaException, InterruptedException {
        // setup imagemagick
        InputStream source = new ByteArrayInputStream(dink);
        event.getChannel().sendFile(genImage(ImageIO.read(source)).toByteArray(), "implode.png").queue();
        source.close();

    }

    private ByteArrayOutputStream genImage(BufferedImage in) throws IOException, IM4JavaException, InterruptedException{
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
        cmd.run(op, in);
        stream.flush();
        return stream;
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event, String... stuff) {
        try {
            event.getHook().sendFile(genImage(ImageIO.read(Utils.downloadImageAsHuman(event.getOption("image").getAsString()))).toByteArray(), "implode.png").queue();
        } catch  (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
