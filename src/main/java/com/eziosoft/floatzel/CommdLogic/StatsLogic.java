package com.eziosoft.floatzel.CommdLogic;

import com.eziosoft.floatzel.Floatzel;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

public class StatsLogic {

    public static String makeMessage(boolean slack){
        // start forming the stats message
        String stats = "```";
        if (!slack){
            stats = stats + "js\n";
        }
        stats = stats + "Floatzel Stats\n";
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
        stats = stats + "Total RAM: "+ Double.toString(((double)Runtime.getRuntime().totalMemory() / (double)(1024.0 * 1024.0))) + " MB\n";
        // free mem
        stats = stats + "Free RAM: "+ Double.toString(((double)Runtime.getRuntime().freeMemory() / (double)(1024.0 * 1024.0))) + " MB\n";
        // used ram
        stats = stats + "Used RAM: "+ Double.toString(((double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())) / (double)(1024.0 * 1024)) + " MB\n";
        // max ram
        stats = stats + "Max RAM: " + Double.toString(((double)Runtime.getRuntime().maxMemory() / (double)(1024.0 * 1024.0))) + " MB\n";
        // processors
        stats = stats + "Accessible Processors: "+ Integer.toString(Runtime.getRuntime().availableProcessors()) + "\n";
        // end it
        stats = stats + "```";
        // return the completed message
        return stats;
    }

    public static void slackRun(SlackletRequest req, SlackletResponse resp){
        resp.reply(makeMessage(true));
    }
}
