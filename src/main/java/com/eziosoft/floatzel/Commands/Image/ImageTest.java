package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ImageTest extends FCommand {
    public ImageTest(){
        name = "img";
        description = "Test to see if image commands work";
        category = test;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws InterruptedException, ExecutionException {
        if (event.getMessage().getAttachments().size() < 1) {
            return;
        }
        try {
            BufferedImage picture = ImageIO.read(event.getMessage().getAttachments().get(0).retrieveInputStream().get());
            Graphics2D g = picture.createGraphics();
            g.setStroke(new BasicStroke(4));
            g.setColor(Color.ORANGE);
            g.drawRect(10, 10, 2, 2);
            g.setColor((Color.BLUE));
            g.drawRect(5, 3, 1, 9);
            g.dispose();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.flush();
            ImageIO.setUseCache(false);
            ImageIO.write(picture, "png", stream);
            event.getChannel().sendFile(stream.toByteArray(), "fuckinimage.png").queue();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
