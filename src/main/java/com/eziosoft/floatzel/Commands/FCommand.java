package com.eziosoft.floatzel.Commands;

import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.InputStream;
import java.util.Random;

public abstract class FCommand extends Command {

    protected String description = "no description";
    protected String cmdgroup = "not grouped";

    public static Random random = new Random();

    // basic categories
    public static Category money = new Category("Currency");
    public static Category test = new Category("Test");
    public static Category spam = new Category("Spam");
    public static Category fun = new Category("Entertainment");
    public static Category owner = new Category("Owner Commands");
    public static Category asshole = new Category("Assholery");
    public static Category sound = new Category("Audio");
    public static Category other = new Category("Other");
    public static Category waifu = new Category("Waifu");
    public static Category buyshit = new Category("Purchasable Commands");
    public static Category stocks =  new Category("Stock Market");
    public static Category admin = new Category("Admin Commands");
    public static Category smm = new Category("Super Mario Maker");
    public static Category image = new Category("Image");
    // new! plugin group
    public static Category plugin = new Category("Loadee Plugins");

    // for holding the split args
    public static String[] argsplit = null;

    // bot admin shitto
    public boolean adminCommand = false;

    // string to store the money emote
    public static String moneyicon = "\uD83E\uDD56";

    // stuff for help caching
    public boolean madehelp = false;
    public String helpthing = null;
    public String helpthing2 = null;
    public String mischelp = null;

    // check to see if the user who ran the command is an admin
    private boolean isAdmin(String uid){
        // moved this to utils
        return Utils.isAdmin(uid);
    }

    // for the error handler
    public static CommandEvent erevent;

    @Override
    protected void execute(CommandEvent event){
        // split the arguments
        argsplit = event.getArgs().split("\\s+");
        // store the event for the error catcher
        erevent = event;
        String uid = event.getMessage().getAuthor().getId().toString();
        // check to see if its an admin only command
        if (adminCommand && !event.isOwner()){
            // is the user that is running it an admin?
            if (!isAdmin(uid)){
                event.getChannel().sendMessage("Error: You do not have permission to run this").queue();
                return;
            }
        }
        cmdrun(event);
    }

    protected abstract void cmdrun(CommandEvent event);







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
