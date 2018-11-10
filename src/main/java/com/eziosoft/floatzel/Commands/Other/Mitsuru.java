package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class Mitsuru extends FCommand {
    public Mitsuru(){
        name = "mitsuru";
        description = "sends a picture of mitsuru from persona 3";
        category = waifu;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // copy paste form yukari.class
        Random random = new Random();
        BufferedImage img = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String filepath = "";
        // generate a number
        int card = random.nextInt(Files.mitsurus.length);
        // get photo
        try {
            img = ImageIO.read(Files.class.getResource("/mit/"+ Files.mitsurus[card]));
            ImageIO.setUseCache(false);
            stream.flush();
            ImageIO.write(img, "png", stream);
            //unimplimeted feature
            //event.getChannel().sendMessage(Uno.unorep[random.nextInt(Uno.unorep.length)]).queue();
            event.getChannel().sendFile(stream.toByteArray(), "mitsuru.png", null).queue();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
