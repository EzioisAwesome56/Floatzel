package com.eziosoft.floatzel.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

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

    // bot admin shitto
    public boolean adminCommand = false;

    // string to store the money emote
    public static String moneyicon = "\uD83E\uDD56";

    // stuff for help caching
    public boolean madehelp = false;
    public String helpthing = null;

    private boolean isAdmin(String uid){
        // something
        return false;
    }

    @Override
    protected void execute(CommandEvent var1){
        // check to see if its an admin only command
        if (adminCommand){

        }
        cmdrun(var1);
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
