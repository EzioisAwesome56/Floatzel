package com.eziosoft.floatzel.Commands.Image;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Resize extends FCommand {
    public Resize(){
        name = "jpeg";
        description = "jpeg-ifys your image";
        category = other;
        aliases = Utils.makeAlias("resize");
    }

    protected void cmdrun(CommandEvent event){
        if (event.getMessage().getAttachments().size() < 1){
            event.reply("you didn't upload a image dumbass!");
            return;
        }
        try {
            // get attachment
            BufferedImage what = ImageIO.read(event.getMessage().getAttachments().get(0).getInputStream());
            // store shit for later
            int width = what.getWidth();
            int height = what.getHeight();
            // setup temp files
            File temp = File.createTempFile("whattheheck", ".die");
            File temp2 = File.createTempFile("OOF", ".meme");
            // FIRST LAYER OF JPEG
            // init shit
            Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            FileImageOutputStream output = new FileImageOutputStream(temp);
            FileImageOutputStream output2 = new FileImageOutputStream(temp2);
            // set output
            writer.setOutput(output);
            // set quality
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(0);
            // apply jpeg to it
            IIOImage image = new IIOImage(what, null, null);
            writer.write(null, image, iwp);
            // close old output
            output.close();
            // RESIZING THE IMAGE
            // load the image back again
            BufferedImage whoa = ImageIO.read(temp);
            // make output image
            BufferedImage outputImage = new BufferedImage(100,
                    100, what.getType());
            Graphics2D g = outputImage.createGraphics();
            // scales input image to output
            g.drawImage(whoa, 0, 0, 100, 100, null);
            g.dispose();
            // APPLY 2nd JPEG
            writer.setOutput(output2);
            image = new IIOImage(outputImage, null, null);
            writer.write(image);
            output2.close();
            // RESIZE IMAGE BACK TO ORIGINAL SIZE
            whoa = ImageIO.read(temp2);
            outputImage = new BufferedImage(width, height, what.getType());
            g = outputImage.createGraphics();
            g.drawImage(whoa, 0, 0, width, height, null);
            g.dispose();
            // convert to stream
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.flush();
            ImageIO.write(outputImage, "jpg", stream);
            // send
            event.getChannel().sendFile(stream.toByteArray(), "wat.jpg", null).queue();
            // clean
            stream.close();
            temp.delete();
            temp2.delete();
            writer.dispose();
        } catch (IOException e){
            Error.Catch(e);
        } catch (NullPointerException e){
        event.reply("Oi mate, this isnt a fucking image!");
    }
    }
}
