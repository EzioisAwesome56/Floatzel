package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Stats extends FCommand {
    public Stats(){
        name = "stats";
        description = "Displays stats about the bot";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // start forming the stats message
        String stats = "```js\nFloatzel Stats\n";
        // version
        stats = stats + "Version: " + Floatzel.version + "\n";
        // java vendor
        stats = stats + "Java Vendor: " + System.getProperty("java.vendor") + "\n";
        // java version
        stats = stats + "Java Version: " + System.getProperty("java.version") + "\n";
        // operating system
        stats = stats + "Host OS: " + System.getProperty("os.name") + "\n";
        // operating system version
        stats = stats + "OS Version: " + System.getProperty("os.version") + "\n";
        // operating system architechure
        stats = stats + "OS Artchitecture: " + System.getProperty("os.arch") + "\n";
        // RAMMMMMM
        stats = stats + "Total RAM: "+ Long.toString(Runtime.getRuntime().totalMemory()) + "\n";
        // free mem
        stats = stats + "Free RAM: "+ Long.toString(Runtime.getRuntime().freeMemory()) + "\n";
        // max ram
        stats = stats + "Max RAM: " + Long.toString(Runtime.getRuntime().maxMemory()) + "\n";
        // processors
        stats = stats + "Accessible Processors: "+ Integer.toString(Runtime.getRuntime().availableProcessors()) + "\n";
        // end it
        stats = stats + "```";
        // send the completed message
        event.getChannel().sendMessage(stats).queue();
    }
}
