package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
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

public class Resize extends FCommand {
    public Resize() {
        name = "jpeg";
        description = "jpeg-ifys your image";
        category = other;
        aliases = Utils.makeAlias("resize");
    }

    private String failMessage = "No fucking images was found you dumbass!";

    protected void cmdrun(CommandEvent event){
        event.getChannel().sendTyping().queue();
        // try and find a thingy??????
        if (event.getMessage().getAttachments().size() > 0 && event.getMessage().getAttachments().get(0).isImage()){
            // this is an image attachment message
            try {
                resizeImage(event, event.getMessage().getAttachments().get(0).getInputStream());
            } catch (IOException e){
                Error.Catch(e);
                return;
            }
        } else if (event.getArgs().length() > 0){
            // try to load the link i guess
            try {
                URL image = new URL(argsplit[0]);
                URLConnection connection = image.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.connect();
                InputStream stream = connection.getInputStream();
                // We use ImageIO because it won't accept anything that's not a proper image. Checking if the stream is null is 100% useless.
                if (ImageIO.read(stream) == null) {
                    event.getChannel().sendMessage(failMessage).queue();
                }
                resizeImage(event, new ByteArrayInputStream(IOUtils.toByteArray(connection.getInputStream())));

            } catch (MalformedURLException | UnknownHostException | IllegalArgumentException | FileNotFoundException | SSLHandshakeException | SocketException e) {
                event.getChannel().sendMessage(failMessage).queue();
            } catch (IOException e){
                Error.Catch(e);
                return;
            }
        } else {
            // try and pull a message from the history
            event.getChannel().getHistory().retrievePast(50).queue(messages -> {
                for (Message m : messages){
                    if (m.getAttachments().size() < 1) {
                        continue;
                    }

                    if (m.getAttachments().get(0).isImage()) {
                        try {
                            resizeImage(event, m.getAttachments().get(0).getInputStream());
                            return;
                        } catch (IOException e){
                            Error.Catch(e);
                        }
                    }
                }
                event.getChannel().sendMessage(failMessage).queue();
            });
        }

    }

    // We moved this process into its own method, for both tidiness and since we'll be calling it three times.
    private void resizeImage(CommandEvent event, InputStream source) {
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
            Error.Catch(e);
        }
    }
}
