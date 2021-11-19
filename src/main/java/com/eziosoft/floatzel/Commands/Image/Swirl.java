package com.eziosoft.floatzel.Commands.Image;

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

public class Swirl extends FSlashableImageCommand {
    public Swirl(){
        name = "swirl";
        help = "slap a swirl onto that image my guy";
        category = image;
        sag = SlashActionGroup.IMAGE;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException {
        event.getChannel().sendFile(genImage(ImageIO.read(new ByteArrayInputStream(dink))), "swirl.png").queue();

    }

    private byte[] genImage(BufferedImage in) throws IOException, InterruptedException, IM4JavaException {
        BufferedImage what;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Pipe pipeOut = new Pipe(null, stream);
        IMOperation op = new IMOperation();
        ConvertCmd cmd = new ConvertCmd();
        if (Floatzel.isdev) cmd.setSearchPath("C:\\magick");
        cmd.setOutputConsumer(pipeOut);
        what = in;
        op.addImage();
        op.format("png");
        op.swirl((double) 180);
        op.addImage("png:-");
        cmd.run(op, what);
        stream.flush();
        return stream.toByteArray();
    }

    @Override
    protected void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff) {
        try {
            event.getHook().sendFile(genImage(stuff), "swirl.png").queue();
        } catch (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
