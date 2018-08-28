package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.YukName;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class Yukari extends FCommand {
    public Yukari(){
        name = "yukari";
        description = "sends a picture of the best waif-i mean girl";
        category = other;
    }

    @Override
    protected void execute(CommandEvent event){
        // copy paste from Reverse.class
        Random random = new Random();
        BufferedImage img = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String filepath = "";
        // generate a number
        int card = random.nextInt(YukName.yukaris.length);
        // get card
        try {
            img = ImageIO.read(YukName.class.getResource("/yuk/"+YukName.yukaris[card]));
            ImageIO.setUseCache(false);
            stream.flush();
            ImageIO.write(img, "png", stream);
            //unimplimeted feature
            //event.getChannel().sendMessage(Uno.unorep[random.nextInt(Uno.unorep.length)]).queue();
            event.getChannel().sendFile(stream.toByteArray(), "yukari.png", null).queue();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
