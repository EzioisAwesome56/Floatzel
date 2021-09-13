package com.eziosoft.floatzel.kekbot;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class KekGlue {
    // general glue code for supporting kekbot features & commands

    // glue code for localization utils
    public static class LocaleUtils {
        public static String getString(String raw, String ignore, Object... objects){
            return getString(raw);
        }

        public static String getString(String e){
            return e;
        }
    }

    // Gluecode for anything needed from the main KekBot class
    public static class KekBot {
        public static ShardManager jda = Floatzel.jda;
        public static GamesManager gamesManager = new GamesManager();

        public static String getGuildLocale(Guild g){
            return "N/A";
        }

        public static String getGuildPrefix(Guild g){
            return Floatzel.isdev ? Floatzel.conf.getDevprefix() : Floatzel.conf.getPrefix();
        }

        public static ComamndClient getCommandClient(){
            return new ComamndClient();
        }
    }
    // basically gluecode for getting locale via command client
    public static class ComamndClient {

        public ComamndClient(){}

        public String getLocale(String e){
            return "N/A";
        }

        public String getDefaultLocale(){
            return "N/A";
        }
    }

    // fuck it, lets add some basic profile gluecode to help with porting
    public static class Profile {
        public Profile(){}

        public static Profile getProfile(User e){
            return new Profile();
        }

        public boolean hasTokenEquipped(){
            return false;
        }

        public Token getToken(){
            return Token.GRAND_DAD;
        }
    }

    // token compatibility
    public enum Token {
        GRAND_DAD("GRAND DAD", "granddad.png");

        private String name;
        private String file;

        Token(String e, String f){
            this.name = e;
            this.file = f;
        }

        public BufferedImage drawToken() throws IOException {
            return javax.imageio.ImageIO.read(Utils.getResource("/" + "resources/profile/token/" + file));
        }
    }

    // kekbot 1.6.1 commandevent compatibility
    public static class CommandEvent {
        private com.jagrosh.jdautilities.command.CommandEvent e;
        private String[] args;

        public CommandEvent(com.jagrosh.jdautilities.command.CommandEvent e, String[] args){
            this.e = e;
            this.args = args;
        }

        public TextChannel getTextChannel(){
            return e.getTextChannel();
        }

        public String[] getArgs() {
            return this.args;
        }

        public String combineArgs(int start, int end) {
            if (end > args.length) throw new IllegalArgumentException("End value specified is longer than the arguments provided.");
            return StringUtils.join(Arrays.copyOfRange(args, start, end), " ");
        }

        public User getAuthor(){
            return e.getAuthor();
        }

        public String getString(String raw, Object... objects){
            return LocaleUtils.getString(raw);
        }

        public String getLocale(){
            return "N/A";
        }

        public TextChannel getChannel(){
            return e.getTextChannel();
        }

        public Member getMember(){
            return e.getMember();
        }

        public String getPrefix(){
            return Floatzel.isdev ? Floatzel.conf.getDevprefix() : Floatzel.conf.getPrefix();
        }
    }

    public static class ImageIO {

        public static BufferedImage read(File e) throws IOException{
            return javax.imageio.ImageIO.read(Utils.getResource(e.getPath().replace("\\", "/")));
        }

        public static BufferedImage read(InputStream in) throws IOException{
            return javax.imageio.ImageIO.read(in);
        }

        public static void setUseCache(boolean set){
            javax.imageio.ImageIO.setUseCache(set);
        }

        public static void write(BufferedImage g, String format, ByteArrayOutputStream out) throws IOException{
            javax.imageio.ImageIO.write(g, format, out);
        }
    }
}
