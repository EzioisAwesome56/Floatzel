package com.eziosoft.floatzel.Commands;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.InputStream;
import java.util.Random;

public abstract class FCommand extends Command {

    protected String description = "no description";

    public static Random random = new Random();

    // basic categories
    public static Category money = new Category("Currency");
    public static Category test = new Category("Test");
    public static Category spam = new Category("Spam");
    public static Category fun = new Category("Entertainment");
    public static Category owner = new Category("Owner Commands");
    public static Category asshole = new Category("Stupid Stuff");
    public static Category sound = new Category("Audio");
    public static Category other = new Category("Other");
    public static Category waifu = new Category("Waifu");
    public static Category buyshit = new Category("Purchasable Commands");
    public static Category stocks =  new Category("Stock Market");
    public static Category admin = new Category("Admin Commands");
    public static Category smm = new Category("Super Mario Maker");
    public static Category image = new Category("Image");
    // new! plugin group
    public static Category plugin = new Category("Loaded Plugins");

    // for holding the split args
    public static String[] argsplit = null;

    // bot admin shitto
    public boolean adminCommand = false;

    // string to store the money emote
    public static String moneyicon = "\uD83E\uDD56";

    // check to see if the user who ran the command is an admin
    private boolean isAdmin(String uid){
        // moved this to utils
        return Utils.isAdmin(uid);
    }

    // for the error handler
    public static CommandEvent erevent;

    // checking if the user has ass mode on
    public static Boolean ass;

    @Override
    protected void execute(CommandEvent event){
        // is joke mode on?
        if (Floatzel.joke){
            // set the joke name to be the right thing
            if (event.getGuild().getSelfMember().getNickname().isEmpty()){
                event.getGuild().getSelfMember().modifyNickname(Floatzel.jokename).queue();
            } else if (!event.getGuild().getSelfMember().getNickname().equals(Floatzel.jokename)){
                event.getGuild().getSelfMember().modifyNickname(Floatzel.jokename).queue();
            }
            // then fake out and act like it cant do anything if its not an admin command
            if (event.isOwner()) {
               event.reply("Warning: executing command may cause space-time continuum rips. Proceed with caution!");
            } else {
                event.getTextChannel().sendMessage(Utils.getCirnoText()).queue();
                return;
            }
        } else {
            // is the name still the joke name
            try {
                if (event.getGuild().getSelfMember().getNickname().isEmpty()) {
                    event.getGuild().getSelfMember().modifyNickname(Floatzel.normalname).queue();
                } else if (event.getGuild().getSelfMember().getNickname().equals(Floatzel.jokename)) {
                    event.getGuild().getSelfMember().modifyNickname(Floatzel.normalname).queue();
                }
            } catch (NullPointerException e){
                // just set to normal nick
                event.getGuild().getSelfMember().modifyNickname(Floatzel.normalname).queue();
            }

        }
        // split the arguments
        argsplit = event.getArgs().split("\\s+");
        // store the event for the error catcher
        erevent = event;
        String uid = event.getMessage().getAuthor().getId();
        // check to see if its an admin only command
        if (adminCommand && !event.isOwner()){
            // is the user that is running it an admin?
            if (!isAdmin(uid)){
                event.getChannel().sendMessage("Error: You do not have permission to run this").queue();
                return;
            }
        }
        try {
            cmdrun(event);
        } catch (Exception e){
            Error.Catch(e, event);
        }
    }

    protected abstract void cmdrun(CommandEvent event) throws Exception;







    // overwrite get help so i can be lazy as hell
    @Override
    public String getHelp(){
        if(help.equals("no help available")){
            return description;
        } else {
            return help;
        }
    }
}
