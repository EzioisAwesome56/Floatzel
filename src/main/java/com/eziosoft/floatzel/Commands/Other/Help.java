package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.lang3.ObjectUtils;

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
    protected void cmdrun(CommandEvent event) {
        if (!madehelp) {
            event.getChannel().sendMessage("Generating help message for first time, this may take some time").queue();
            // as of 2.4.1 we need 2 string builders
            StringBuilder builder = new StringBuilder();
            StringBuilder build2 = new StringBuilder();
            StringBuilder misc = new StringBuilder();
            // now start actually doing things
            List<String> rawcats = new ArrayList<String>();
            // for making 2 help messages
            List<String> one = new ArrayList<String>();
            List<String> two = new ArrayList<String>();
            // flip through all commands and create a list of all catogories
            commands.forEach(cmd -> {
                if (cmd.getCategory() != null) {
                    String cmdcat = cmd.getCategory().getName();
                    if (!rawcats.contains(cmdcat)) {
                        rawcats.add((String) cmdcat);
                    }
                }
            });
            // split the categories into the 2 lists
            int size = rawcats.size();
            for (int i = 0; i < size; i++){
                if (i < (size + 1)/2){
                    one.add(rawcats.get(i));
                } else {
                    two.add(rawcats.get(i));
                }
            }
            // now sort the commands into categories
            String[] catsone = new String[one.size()];
            String[] catstwo = new String[two.size()];
            one.toArray(catsone);
            two.toArray(catstwo);
            String curcat = "";
            // run this twice to build the 2 seperate help messages
            for (int i = 0; i < catsone.length; i++) {
                curcat = catsone[i];
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
            // now run it again
            for (int i = 0; i < catstwo.length; i++) {
                curcat = catstwo[i];
                final String h = curcat;
                build2.append("#" + curcat + "\n");
                commands.forEach(cmd -> {
                    if (cmd.getCategory() != null) {
                        if (cmd.getCategory().getName().equals(h)) {
                            build2.append("[" + cmd.getName() + "](" + cmd.getHelp() + ")\n");
                        }
                    }
                });
                build2.append("\n");
            }
            // deal with unsorted commands now
            misc.append("#Unsorted\n");
            commands.forEach(cmd -> {
                if (cmd.getCategory() == null) {
                    misc.append("[" + cmd.getName() + "](" + cmd.getHelp() + ")\n");
                }
            });


            //commands.forEach(command -> builder.append("[" + command.getName() + "](" + command.getHelp() + ")\n"));

            // form the nice looking help box
            String helpmsg = "```md\n#Floatzel Version " + Floatzel.version + " help\n" + builder.toString() + "```";
            // debug string: print to console how long the help message is
            helpthing = helpmsg;
            helpthing2 = "```md\n#Floatzel Help Continued\n"+build2.toString()+"```";
            mischelp = "```md\n#Floatzel Help Continued\n"+misc.toString()+"\n\n\n" +
                    "#important floatzel notice!\n" +
                    "#Floatzel BOT does not take any credit for any of the artwork featured in this bot. All works where sourced from the internet for user enjoyment.\n" +
                    "#Most ralsei images can be found here: https://imgur.com/a/019wZSk#m2JQKnw ```";
            madehelp = true;
            System.out.println(Integer.toString(helpmsg.length()));
            System.out.println(Integer.toString(helpthing2.length()));
            System.out.println(Integer.toString(mischelp.length()));
        }
        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(helpthing).queue());
        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(helpthing2).queue());
        event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(mischelp).queue());
    }
}
