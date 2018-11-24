package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Floof extends FCommand {
    public Floof(){
        name = "floof";
        help = "Floof up your day!";
        category =  other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        File gif = null;
        event.getChannel().sendTyping().queue();
        // reuse the same old code for a 3rd time
        Random random = new Random();
        BufferedImage img = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String filepath = "";
        // generate a number
        int card = random.nextInt(Files.floof.length);
        // get dat floofy boi
        try {
            String filename = Files.floof[card];
            if (!filename.equals("26.gif")) {
                img = ImageIO.read(Files.class.getResource("/floof/" + filename));
                ImageIO.setUseCache(false);
                stream.flush();
                ImageIO.write(img, "png", stream);
            } else {
                gif = new File(Files.class.getResource("/floof/" + filename).getFile());
            }
            //unimplimeted feature
            //event.getChannel().sendMessage(Uno.unorep[random.nextInt(Uno.unorep.length)]).queue();
            if (!filename.equals("26.gif")) {
                event.getChannel().sendFile(stream.toByteArray(), "floofboi.png", null).queue();
            } else {
                event.getChannel().sendFile(gif, "floofboi.gif", null).queue();
            }
            stream.close();
        } catch (IOException e) {
            Error.Catch(e);
        }
    }
}
