package com.eziosoft.floatzel.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.eziosoft.floatzel.Config.admins;

public abstract class FCommand extends Command {

    protected String description = "no description";
    protected String cmdgroup = "not grouped";

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
        boolean match = false;
        for (String s : admins) {
            if (uid.contains(s)){
                match = true;
                break;
            }
        }
        return match;
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
        // if all checks are passed, run the command
        cmdrun(event);
    }

    protected abstract void cmdrun(CommandEvent epic);





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
