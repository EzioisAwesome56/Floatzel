package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Uno.Uno;
import com.eziosoft.floatzel.Res.Yukari.Files;
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
        // im lazy, so copy pasta
        Random random = new Random();
        BufferedImage img = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String filepath = "";
        // generate a number
        int card = random.nextInt(Files.yukaris.length);
        // get get yukari
        try {
            img = ImageIO.read(Uno.class.getResource(Files.yukaris[card]));
            ImageIO.setUseCache(false);
            stream.flush();
            ImageIO.write(img, "png", stream);
            event.getChannel().sendFile(stream.toByteArray(), "yukari.png", null).queue();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
    }
}
