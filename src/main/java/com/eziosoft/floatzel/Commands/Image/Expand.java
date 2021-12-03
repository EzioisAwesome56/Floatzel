package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableImageCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import com.eziosoft.floatzel.Util.Error;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Expand extends FSlashableImageCommand {
    public Expand(){
        name = "expand";
        help = "makes an image larger";
        category = image;
        aliases = Utils.makeAlias("grow");
        sag = SlashActionGroup.IMAGE;
    }

    @Override
    protected void imageRun(CommandEvent event, byte[] dink) throws IOException, InterruptedException, IM4JavaException {
        // copy paste from shrink, but change a few things
        InputStream source = new ByteArrayInputStream(dink);
        event.getChannel().sendFile(genImage(ImageIO.read(source)).toByteArray(), "big.jpg").queue();
    }

    private ByteArrayOutputStream genImage(BufferedImage in) throws IOException, InterruptedException, IM4JavaException{
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
        op.resize(5000,5000);
        op.addImage("jpg:-");
        cmd.run(op, what);
        stream.flush();
        return stream;
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff) {
        try {
            event.getHook().editOriginal(genImage(stuff).toByteArray(), "big.jpg").queue();
        } catch (Exception e){
            Error.CatchSlash(e, event);
        }
    }
}
