package com.eziosoft.floatzel.Commands.Image;

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

public class Explode extends FSlashableImageCommand {
    public Explode(){
        name = "explode";
        description = "Explode your images!";
        category = image;
        aliases = Utils.makeAlias("bang");
        sag = SlashActionGroup.IMAGE;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException{
        InputStream source = new ByteArrayInputStream(dink);
        event.getChannel().sendFile(genImage(ImageIO.read(source)).toByteArray(), "explode.png").queue();
        source.close();

    }

    private ByteArrayOutputStream genImage(BufferedImage in) throws IOException, InterruptedException, IM4JavaException{
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
        cmd.run(op, in);
        stream.flush();
        return stream;
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event, String... stuff) {
        try {
            ByteArrayOutputStream stream = genImage(ImageIO.read(Utils.downloadImageAsHuman(event.getOption("image").getAsString())));
            event.getHook().sendFile(stream.toByteArray(), "explode.png").queue();
            stream.close();
        } catch (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
