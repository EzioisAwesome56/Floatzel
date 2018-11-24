package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import com.eziosoft.floatzel.Util.Error;

public class Yukari extends FCommand {
    public Yukari(){
        name = "yukari";
        description = "sends a picture of the best waif-i mean girl";
        category = waifu;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendTyping().queue();
        // generate a number
        int card = random.nextInt(Files.yukaris.length);
        // get file
        String filename = Files.yukaris[card];
        event.getChannel().sendFile(Utils.getResourse("/yuk/", filename), "yukari." + Utils.getFileType(filename), null).queue();
    }
}
