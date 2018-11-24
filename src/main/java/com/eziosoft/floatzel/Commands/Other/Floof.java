package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
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
        event.getChannel().sendTyping().queue();
        // generate a number
        int card = random.nextInt(Files.floof.length);
        // get dat floofy boi
        String filename = Files.floof[card];
        event.getChannel().sendFile(Utils.getResourse("/floof/", filename), "floofyboi." + Utils.getFileType(filename), null).queue();
    }
}
