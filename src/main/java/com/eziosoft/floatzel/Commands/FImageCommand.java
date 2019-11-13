package com.eziosoft.floatzel.Commands;

import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.io.IOUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.rmi.server.ExportException;
import java.util.concurrent.TimeUnit;

public abstract class FImageCommand extends FCommand {

    private String failMessage = "No fucking images where found you dumbass!";

    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        event.getChannel().sendTyping().queue();
        // try and find a thingy??????
        if (event.getMessage().getAttachments().size() > 0 && event.getMessage().getAttachments().get(0).isImage()){
            // this is an image attachment message
            try {
                imageRun(event, IOUtils.toByteArray(event.getMessage().getAttachments().get(0).retrieveInputStream().join()));
            } catch (IOException e){
                throw e;
            } catch (Exception e){
                throw e;
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
                    return;
                }
                try {
                    imageRun(event, IOUtils.toByteArray(connection.getInputStream()));
                } catch (Exception e){
                    throw e;
                }

            } catch (MalformedURLException | UnknownHostException | IllegalArgumentException | FileNotFoundException | SSLHandshakeException | SocketException e) {
                event.getChannel().sendMessage(failMessage).queue();
            } catch (IOException e){
                throw e;
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
                            imageRun(event, IOUtils.toByteArray(m.getAttachments().get(0).retrieveInputStream().join()));
                            return;
                        } catch (IOException e){
                            // ugh
                            Error.CatchOld(e);
                        } catch (Exception e){
                            Error.CatchOld(e);
                        }
                    }
                }
                event.getChannel().sendMessage(failMessage).queue();
            });
        }
    }

    protected abstract void imageRun(CommandEvent event, byte[] dink) throws Exception;
}
