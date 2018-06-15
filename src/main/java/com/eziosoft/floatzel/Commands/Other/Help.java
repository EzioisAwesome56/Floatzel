package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.*;
import java.util.stream.Collectors;

public class Help extends FCommand {

    List<FCommand> commands = new ArrayList<>();

    public Help() {
        name = "help";
        description = "fuck";

        Floatzel.commandClient.getCommands().forEach(command -> commands.add((FCommand) command));
    }

    @Override
    protected void execute(CommandEvent event) {
        StringBuilder builder = new StringBuilder();
        List<String> rawcats = new ArrayList<String>();
        // flip through all commands and create a list of all catogories
        commands.forEach(cmd -> {
            if (cmd.getCategory() != null) {
                String cmdcat = cmd.getCategory().getName();
                if (!rawcats.contains(cmdcat)) {
                    rawcats.add((String) cmdcat);
                }
            }
        });
        // now sort the commands into categories
        String[] cats = new String[rawcats.size()];
        rawcats.toArray(cats);
        String curcat = "";
        for(int i = 0; i < cats.length; i++){
            curcat = cats[i];
            final String h = curcat;
            builder.append("#" + curcat + "\n");
            commands.forEach(cmd -> {
                if (cmd.getCategory() != null) {
                    if (cmd.getCategory().getName().equals(h)) {
                        builder.append("[" + cmd.getName() + "](" + cmd.getHelp() + ")\n");
                    }
                }
            });
            builder.append("\n");
        }
        // deal with unsorted commands now
        builder.append("#Unsorted\n");
        commands.forEach(cmd -> {
            if (cmd.getCategory() == null){
                builder.append("[" + cmd.getName() + "](" + cmd.getHelp() + ")\n");
            }
        });


        //commands.forEach(command -> builder.append("[" + command.getName() + "](" + command.getHelp() + ")\n"));

        // form the nice looking help box
        String helpmsg = "```md\n#Floatzel Version " + Floatzel.version + " help\n" + builder.toString() + "```";

        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(helpmsg).queue());
    }
}
