package com.eziosoft.floatzel.kekbot;

import com.eziosoft.floatzel.Floatzel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class KekGlue {
    // general glue code for supporting kekbot features & commands

    // glue code for localization utils
    public static class LocaleUtils {
        public static String getString(String raw, String ignore, Object... objects){
            return raw;
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

    public enum Token {
        GRAND_DAD("GRAND DAD", "granddad.png");

        private String name;
        private String file;

        Token(String e, String f){
            this.name = e;
            this.file = f;
        }

        public BufferedImage drawToken() throws IOException {
            return ImageIO.read(new File(file));
        }
    }
}
