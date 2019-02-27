package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.smm4j.Level;
import com.eziosoft.smm4j.Util;
import com.rethinkdb.net.Cursor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.eziosoft.floatzel.Config.admins;

public class Utils {
    private static Random random = new Random();


    public static String genBar(String filled, String empty, int maxLength, int fill) {
        String full = StringUtils.repeat(filled, maxLength);
        String emp = StringUtils.repeat(empty, maxLength);
        return full.substring(0, filled.length() * fill) + emp.substring(empty.length() * fill, emp.length());
    }

    public static String repeatString(String text, int amount){
        StringUtils.repeat(text, amount);
        String msg = "";
        for (int x = 0; x == amount; x++){
            msg = msg + text;
        }
        return msg;
    }

    /**
     * Converts milliseconds to a H:Mm:Ss format. (Example: 1:02:30)
     * @param millis The milliseconds to convert.
     * @return The converted H:Mm:Ss format.
     */
    public static String convertMillisToHMmSs(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        return (hours > 0 ? hours + ":" : "") +
                (minutes > 0 ? (minutes > 9 ? minutes + ":" : (hours > 0 ? "0" + minutes + ":" : minutes + ":" )) : (hours > 0 ? "00:" : "0:")) +
                (seconds > 0 ? (seconds > 9 ? seconds : "0" + seconds) : "00");
    }

    /**
     * Converts milliseconds to a "Time" format. (Example, 1 Day, 20 Hours, 10 minutes, and 5 Seconds.)
     * @param millis The milliseconds to convert.
     * @return The converted "Time" format.
     */
    public static String convertMillisToTime(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) -
                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        return (days != 0 ? days + (days > 1 ? " Days, " : " Day, ") : "") +
                (hours != 0 ? hours + (hours > 1 ? " Hours, " : " Hour, ") : "") + minutes +
                (minutes != 1 ? " Minutes and " : " Minute and ") + seconds + (seconds != 1 ? " Seconds." : " Second.");
    }

    /**
     * Converts two millis to a H:Mm:Ss format. Mostly to compare one to the other.
     * This is mostly meant to be used within the music player, since the first variable is the current timestamp, while the second variable is the length of the track.
     * @param current Current position in track.
     * @param length Total track length.
     * @return The converted H:Mm:Ss format.
     */
    //This may wind up being a private method in the music player in a later version.
    public static String songTimestamp(long current, long length) {
        return convertMillisToHMmSs(current) + "/" + convertMillisToHMmSs(length);
    }

    // get the first entire from a cursor list
    public static String getValue(Cursor cur){
        try {
            List curlist = cur.toList();
            String value = curlist.get(0).toString();
            return value;
        } catch (IndexOutOfBoundsException e){
            Error.Catch(e);
            return "ERROR IN getValue!";
        }
    }

    public static MessageEmbed buildSmm(Level lvl){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setThumbnail(lvl.imgURL());
        builder.setAuthor(lvl.name(), Util.makeUrl(lvl.id()));
        builder.setTitle("Level by: "+lvl.author());
        builder.addField("Difficulty", lvl.difficulty(), true);
        builder.addField("Level ID", lvl.id(), true);
        builder.addField("Stars", Integer.toString(lvl.liked()), true);
        builder.addField("Total Players", Integer.toString(lvl.played()), true);
        builder.addField("Total Attempts", Integer.toString(lvl.attempts()), true);
        builder.addField("Total Clears", Integer.toString(lvl.clears()), true);
        builder.addField("Upload Date", lvl.date(), true);
        builder.setImage(lvl.fullimgURL());
        return builder.build();
    }

    public static InputStream getResourse(String path, String filename){
        return Files.class.getResourceAsStream(path + filename);
    }

    public static String getFileType(String filename){
        String[] parts = filename.split("\\.");
        return parts[1];
    }

    // make sigel aliases easier to define
    public static String[] makeAlias(String name){
        return new String[]{name};
    }

    // image -> buffered image
    // stolen from https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    // moved from fcommand
    public static boolean isAdmin(String uid){
        boolean match = false;
        for (String s : admins) {
            if (uid.contains(s)){
                match = true;
                break;
            }
        }
        return match;
    }
}
