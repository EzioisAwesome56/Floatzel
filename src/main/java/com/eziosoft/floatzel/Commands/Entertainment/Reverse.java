package com.eziosoft.floatzel.Commands.Entertainment;

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

public class Reverse extends FCommand {

    public Reverse(){
        name = "reverse";
        description = "Pull out everyone's favorite fucking uno card";
        category = fun;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendTyping().queue();
        // generate a number
        int card = random.nextInt(Files.unocards.length);
        // get card
        String filename = Files.unocards[card];
        event.getChannel().sendFile(Utils.getResourse("/uno/", filename), "reverse." + Utils.getFileType(filename)).queue();
    }
}
