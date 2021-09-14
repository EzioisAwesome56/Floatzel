package com.eziosoft.floatzel.kekbot;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.floatzel.kekbot.Games.Game;
import com.sun.xml.internal.bind.v2.model.core.ID;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class KekGlue {
    public static HashMap<String, String> text = new HashMap<>();

    public static void initWords(){
        text.put("game.tictactoe.aifirst", "Floatzel got the first turn");
        text.put("game.tictactoe.botwin", "Floatzel wins!");
        text.put("game.tictactoe.spaceownedopponent", "Your opponent already owns that space.");
        text.put("game.tictactoe.spaceowned", "You already claimed that space!");
        text.put("game.tictactoe.draw", "It's a tie!");
        text.put("game.tictactoe.playerfirst", ", you're up first!");
        text.put("command.fun.game.quit.lobby", " has left the lobby.");
        text.put("game.playerjoined", " has joined the lobby!");
        text.put("game.tictactoe.win", " has won the game!");
        text.put("game.rules.tictactoe", "A board game classic! As soon as the game starts, players have to take turns trying to create a line. It's exactly what you'd expect from TicTacToe. Players are able to place their piece down by typing the number representing a space on the board in chat. The player who finishes their line first wins!");
        text.put("command.fun.game.nolobby", "There is no open game lobby in this text channel.");
        text.put("game.snailrace.start", "The race has begun!");
        text.put("game.multiplier", "x multiplyer is active!");
        text.put("game.snailrace.end", "The race is over!");
        text.put("game.rules.snailrace", "It's a race to the finish! As soon as the game starts, everyone will have a snail trying to race for the finish line. Players don't have to do anything to win, snails move automatically, making this a game of chance!\n\nFirst snail to reach the finish wins!\nDepending on how many players are playing, there can also be second and third place winners!");
        text.put("command.fun.game.readystatus", "Game ready to play!");
        text.put("command.fun.game.awaitingstatus", "Waiting for more players...");
        text.put("command.fun.game.start.error", "Only the player who opened the lobby can start the game!");
        text.put("command.fun.game.lobby.currentgame", "Current game:");
        text.put("command.fun.game.lobby.minplayers", "Minimum Players Required:");
        text.put("command.fun.game.lobby.maxplayers", "Maximum Players:");
        text.put("command.fun.game.lobby.numplayers", "Players in lobby:");
        text.put("command.fun.game.lobby.status", "Current lobby status:");
        text.put("command.fun.game.lobby.players", "Current list of players");
        text.put("command.noargs", "No arguments provided!");
    }
    // general glue code for supporting kekbot features & commands

    // glue code for localization utils
    public static class LocaleUtils {
        public static String getString(String raw, String ignore, Object... objects){
            if (raw.equals("game.tictactoe.win")){
                return objects[0] + text.get(raw);
            } else if (raw.equals("game.tictactoe.playerfirst")){
                return objects[0] + text.get(raw);
            } else if (raw.equals("game.playerjoined")) {
                return objects[0] + text.get(raw) + " " + objects[1];
            } else if (raw.equals("command.fun.game.quit.lobby")){
                return objects[0] + text.get(raw) + " " + objects[1];
            } else if (raw.equals("game.multiplier")){
                return objects[0] + text.get(raw);
            } else {
                return getString(raw);
            }
        }

        public static String getString(String e) {
            return text.getOrDefault(e, "Fatal String Delocalization error!\n" + e);
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
        private int bal;
        private String id;

        public Profile(){}

        public Profile(User e){
            id = e.getId();
        }

        public static Profile getProfile(User e){
            return new Profile(e);
        }

        public void wonGame(double keks, int kxp){
            // FILTHY HACK: use old methods to save shit
            // save new bal
            System.out.println(id);
            com.eziosoft.floatzel.Objects.User ur = Database.dbdriver.getProfile(id);
            int newbal = ur.getBal() + (int) keks;
            ur.setBal(newbal);
            Database.dbdriver.saveProfile(ur);
        }

        public void tieGame(double topkeks, int KXP) {
            // load profile
            System.out.println(id);
            com.eziosoft.floatzel.Objects.User ur = Database.dbdriver.getProfile(id);
            int newbal = ur.getBal() + (int) topkeks;
            ur.setBal(newbal);
            Database.dbdriver.saveProfile(ur);
        }

        public void save(){
            return;
        }

        public boolean hasTokenEquipped(){
            return false;
        }

        public Token getToken(){
            return Token.GRAND_DAD;
        }

        public void spendTopKeks(double topkek){
            com.eziosoft.floatzel.Objects.User u = Database.dbdriver.getProfile(id);
            // save new bal
            u.setBal(u.getBal() - (int) topkek);
            Database.dbdriver.saveProfile(u);
        }

        public void takeKXP(int kxp){
            return;
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

    public static class BetManager {

        public BetManager(boolean betsEnabled, boolean betsEnabled1) {
        }

        public Double declareWinners(Game game, Object... shit) {
            // we dont give a fuck about bets, so lets just give them 1 or something
            return 1D;
        }

        public boolean hasPlayerBets(){
            return false;
        }

        public void declareTie(){}
    }

    public static class CustomEmote {
        private static String TOPKEK = "\uD83E\uDD56";

        public static String printPrice(double price) {
            if (price % 1 == 0) return (int) price + TOPKEK;
            else return price + TOPKEK;
        }

        private final static String[] TROPHIES = {"\uD83D\uDCA3", "\uD83D\uDDD1", "\uD83D\uDC1F"};

        public static String getTrophy(int place) {
            if (place < 0 || place > TROPHIES.length) return TROPHIES[0];
            else return TROPHIES[place];
        }
    }

    public static class FileUtils {
        public static List<String> readLines(File e, String trash) throws IOException{
            return IOUtils.readLines(Utils.getResource(e.getPath()), StandardCharsets.UTF_8);
        }
    }
}
