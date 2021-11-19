package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
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

public class Shrink extends FSlashableImageCommand {
    public Shrink(){
        name = "shrink";
        help = "shrinks an image";
        category = FCommand.image;
        aliases = Utils.makeAlias("resize");
        sag = SlashActionGroup.IMAGE;
    }

    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException {
        event.getChannel().sendFile(genImage(ImageIO.read(new ByteArrayInputStream(dink))), "small.jpg").queue();

    }

    private byte[] genImage(BufferedImage in) throws IOException, InterruptedException, IM4JavaException {
        BufferedImage what;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        IMOperation op = new IMOperation();
        // shrink
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        cmd.setOutputConsumer(pipeOut);
        what = in;
        op.addImage();
        op.format("jpg");
        op.resize(25,25);
        op.addImage("jpg:-");
        cmd.run(op, what);
        stream.flush();
        return stream.toByteArray();
    }


    @Override
    protected void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff) {
        try {
            event.getHook().sendFile(genImage(stuff), "small.jpg").queue();
        } catch (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
