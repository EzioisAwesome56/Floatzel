package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.ImageDownloadException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.JsonConfig;
import com.eziosoft.floatzel.Objects.gameTexts;
import com.eziosoft.floatzel.Res.Files;
import com.google.gson.Gson;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    @Deprecated
    // get the first entire from a cursor list
    public static String getValue(Cursor cur) throws IndexOutOfBoundsException, NoSuchMethodException {
        throw new NoSuchMethodException("this method is deprecated!");
    }

    public static InputStream getResource(String path, String filename){
        return Files.class.getResourceAsStream(path + filename);
    }

    public static InputStream getResource(String path){
        return Files.class.getResourceAsStream(path.startsWith("/") ? path : "/" + path);
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
        if (Floatzel.conf.getOwnerid().equals(uid)) return true;
        boolean match = false;
        for (String s : Floatzel.conf.getAdmins()) {
            if (uid.contains(s)){
                match = true;
                break;
            }
        }
        return match;
    }

    // check if the config file exists
    public static boolean configExist(){
        File config = new File("config.json");
        return config.exists();
    }

    // check to see if the game text file exists
    public static boolean gameTextExist(){
        File file = new File("games.json");
        return file.exists();
    }

    // make default games.json file
    public static void makeGames(Gson g){
        // load defaults
        Floatzel.gameTexts.loadDefaults();
        // convert to string
        String json = g.toJson(Floatzel.gameTexts);
        // save file
        try {
            FileWriter writer = new FileWriter("games.json");
            writer.write(json);
            writer.close();
        } catch (IOException e){
            System.err.println("ERROR WHILE SAVING GAMES");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

    // load the games to play from the file
    public static boolean loadGames(Gson g){
        System.out.println("Now loading games file...");
        try {
            BufferedReader br = new BufferedReader(new FileReader("games.json"));
            Floatzel.gameTexts = g.fromJson(br, gameTexts.class);
            br.close();
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void makeConfig(Gson g){
        // load default configuration data
        Floatzel.conf.loadDefaults();
        // convert the file to a string
        String json = g.toJson(Floatzel.conf);
        // save the file
        try {
            FileWriter writer = new FileWriter("config.json");
            writer.write(json);
            writer.close();
        } catch (IOException e){
            System.err.println("ERROR WHILE SAVING CONFIG");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println("A default config file has been generated. Please edit this file with your own information!");
    }

    public static boolean loadConfig(Gson g){
        System.out.println("Now loading configuration file...");
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.json"));
            Floatzel.conf = g.fromJson(br, JsonConfig.class);
            br.close();
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    static String[] cirnoText = {"I'm so stupid I can't figure out how to use that command! Sorry!", "I accidentally froze this command! whoops!", "I shot a bullet at my hand and now I can't run this command!",
    "I'm too lazy to run this command, sorry!", "Stop trying to run this command before I freeze you!"};

    public static String getCirnoText(){
        int index = random.nextInt(cirnoText.length);
        return cirnoText[index];
    }

    // code borrowed from stackoverflow as always
    // https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
    // modified to use httpurlconnection to support status codes (404, 403, etc)
    public static InputStream downloadImageAsHuman(String url) throws ImageDownloadException, MalformedURLException {
        // This will open a socket from client to server
        URL dank = new URL(url);
        // This user agent is for if the server wants real humans to visit
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
        // This socket type will allow to set user_agent
        try {
            HttpURLConnection con = (HttpURLConnection) dank.openConnection();
            // Setting the user agent
            con.setRequestProperty("User-Agent", USER_AGENT);
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ImageDownloadException(con.getResponseCode(), con.getResponseMessage());
            }
            // Requesting input data from server
            return con.getInputStream();
        } catch (IOException io){
            throw new ImageDownloadException(-69, "An error occurred while downloading image!", io);
        }
    }

    public static void defaultTimeoutAction(Event event){
        String error = "Error: you took too long to respond!";
        if (event instanceof SelectionMenuEvent){
            ((SelectionMenuEvent) event).getInteraction().editSelectionMenu(null).queue();
            ((SelectionMenuEvent) event).getHook().editOriginal(error).queue();
        } else if (event instanceof SlashCommandEvent){
            ((SlashCommandEvent) event).getHook().editOriginalComponents().queue();
            ((SlashCommandEvent) event).getHook().editOriginal(error).queue();
        }
    }
}
