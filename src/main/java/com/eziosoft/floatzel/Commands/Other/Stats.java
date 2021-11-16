package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.List;

public class Stats extends FSlashableCommand {
    public Stats(){
        name = "stats";
        description = "Displays stats about the bot";
        category = other;
        sag = SlashActionGroup.OTHER;
    }

    public static List<String> ext = new ArrayList<String>();

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage(genMsg()).queue();
    }

    private String genMsg(){
        // start forming the stats message
        StringBuilder stats = new StringBuilder("```");
        stats.append("Floatzel Stats\n");
        // version
        stats.append("Version: ").append(Floatzel.version).append("\n");
        if (!ext.isEmpty()) {
            for (String s : ext) {
                stats.append(s).append("\n");
            }
        }
        // java vendor
        stats.append("Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");
        // java version
        stats.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        // operating system
        stats.append("Host OS: ").append(System.getProperty("os.name")).append("\n");
        // operating system version
        stats.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
        // operating system architechure
        stats.append("OS Artchitecture: ").append(System.getProperty("os.arch")).append("\n");
        // RAMMMMMM
        stats.append("Total RAM: ").append(Double.toString(((double) Runtime.getRuntime().totalMemory() / (double) (1024.0 * 1024.0)))).append(" MB\n");
        // free mem
        stats.append("Free RAM: ").append(Double.toString(((double) Runtime.getRuntime().freeMemory() / (double) (1024.0 * 1024.0)))).append(" MB\n");
        // used ram
        stats.append("Used RAM: ").append(Double.toString(((double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())) / (double) (1024.0 * 1024))).append(" MB\n");
        // max ram
        stats.append("Max RAM: ").append(Double.toString(((double) Runtime.getRuntime().maxMemory() / (double) (1024.0 * 1024.0)))).append(" MB\n");
        // processors
        stats.append("Accessible Processors: ").append(Integer.toString(Runtime.getRuntime().availableProcessors())).append("\n");
        // end it
        stats.append("```");
        // return the completed message
        return stats.toString();
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        event.getHook().sendMessage(genMsg()).queue();
    }
}
