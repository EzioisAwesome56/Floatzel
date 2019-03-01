package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.Route;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.SSLHandshakeException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Resize extends FCommand {
    public Resize() {
        name = "jpeg";
        description = "jpeg-ifys your image";
        category = other;
        aliases = Utils.makeAlias("resize");
    }

    protected void cmdrun(CommandEvent event){
        InputStream source = null;
        event.getChannel().sendTyping().queue();
        Message a = null;
        BufferedImage what = null;
        boolean fail = true;
        // try and find a thingy??????
        if (event.getMessage().getAttachments().size() > 0 && event.getMessage().getAttachments().get(0).isImage()){
            // this is an image attachment message
            a = event.getMessage();
            try {
                source = a.getAttachments().get(0).getInputStream();
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
                source = connection.getInputStream();
                if (source == null) {
                    fail = true;
                } else {
                    fail = false;
                }

            } catch (MalformedURLException | UnknownHostException | IllegalArgumentException | FileNotFoundException e) {
                fail = true;
                System.out.println(e.getMessage());
            } catch (SSLHandshakeException | SocketException e) {
                fail = true;
                System.out.println(e.getMessage());
            } catch (IOException e) {
                fail = true;
                System.out.println(e.getMessage());
            }
        } else {
            // try and pull a message from the history
            List<Message> gay = event.getChannel().getHistory().retrievePast(50).complete();
            for (Message m : gay){
                if (m.getAttachments().size() > 0){
                    if (m.getAttachments().get(0).getHeight() > 0){
                        a = m;
                        fail = false;
                        break;
                    }
                }
            }
            if (a == null){
                fail = true;
            } else {
                try {
                    source = a.getAttachments().get(0).getInputStream();
                } catch (IOException e){
                    Error.Catch(e);
                    return;
                }
            }
        }

        // did it fail?
        if (fail){
            event.getChannel().sendMessage("No fucking images was found you dumbass!").queue();
            return;
        }
        // prepare for magick stuff
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
            byte[] done = stream.toByteArray();
            stream.close();
            event.getChannel().sendFile(done, "wat.jpg").queue();
        } catch (IOException | InterruptedException | IM4JavaException e){
            Error.Catch(e);
        }
    }
}
