package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class Reverse extends FCommand {
    private static Random random = new Random();

    public Reverse(){
        name = "reverse";
        description = "Pull out everyone's favorite fucking uno card";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        Random random = new Random();
        BufferedImage img = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String filepath = "";
        // generate a number
        int card = random.nextInt(Files.unocards.length);
        // get card
        try {
            img = ImageIO.read(Files.class.getResource("/uno/"+ Files.unocards[card]));
            ImageIO.setUseCache(false);
            stream.flush();
            ImageIO.write(img, "png", stream);
            event.getChannel().sendMessage(Files.unorep[random.nextInt(Files.unorep.length)]).queue();
            event.getChannel().sendFile(stream.toByteArray(), "a.png", null).queue();
            stream.close();
        } catch (IOException e) {
            Error.Catch(e.getStackTrace().toString(), e.getMessage());
        }
    }
}
