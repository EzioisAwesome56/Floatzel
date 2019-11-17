package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
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
        event.getChannel().sendTyping().queue();
        // generate a number
        int card = random.nextInt(Files.mitsurus.length);
        // get photo
        String filename = Files.mitsurus[card];
        event.getChannel().sendFile(Utils.getResourse("/mit/", filename), "mitsuru."+ Utils.getFileType(filename)).queue();
    }
}
