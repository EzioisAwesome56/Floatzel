package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Floatzel;
import com.rethinkdb.net.Cursor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    public static MessageEmbed buildSmm(String[] a){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setThumbnail(a[3]);
        builder.setAuthor(a[0], a[6]);
        builder.setTitle("Level by: "+a[2]);
        builder.addField("Difficulty", a[1], true);
        builder.addField("Level ID", a[5], true);
        builder.setImage(a[4]);
        return builder.build();
    }
}
