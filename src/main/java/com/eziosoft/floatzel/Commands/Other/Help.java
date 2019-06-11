package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.floatzel.kekbot.EmbedPaginator;
import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Help extends FCommand {

    List<FCommand> commands = new ArrayList<>();

    public Help() {
        name = "help";
        description = "displays help for the bot's commands";
        category = other;

        Floatzel.commandClient.getCommands().forEach(command -> commands.add((FCommand) command));
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        if (event.getArgs().length() < 1) {
            sendHelp(event);
        } else {
            sendCommandHelp(event, argsplit[0]);
        }
    }

    //Roughly stolen from https://github.com/Godson777/KekBot/blob/master/src/main/java/com/godson/kekbot/command/commands/general/Help.java
    // Sorry, godson!
    private void sendHelp(CommandEvent event){
        List<Category> categories = commands.stream().map(Command::getCategory).distinct().sorted(Comparator.comparing(Category::getName)).collect(Collectors.toList());
        EmbedPaginator.Builder builder = new EmbedPaginator.Builder();
        builder.addUsers(event.getAuthor());
        builder.setEventWaiter(Floatzel.waiter);
        builder.setFinalAction(m -> m.clearReactions().queue());
        builder.waitOnSinglePage(true);
        builder.showPageNumbers(true);

        categories.forEach(c -> {
            if (c.getName().equalsIgnoreCase(FCommand.owner.getName()) && !event.isOwner()) return;
            if (c.getName().equalsIgnoreCase(FCommand.admin.getName()) && !Utils.isAdmin(event.getAuthor().getId()
            )) return;
            if (c.getName().equalsIgnoreCase("test")) return;
            if (c.getName().equals("unassigned")) return;
            List<FCommand> sorted = new ArrayList<>(commands).stream().filter(cmd -> cmd.getCategory().equals(c)).sorted(Comparator.comparing(Command::getName)).collect(Collectors.toList());
            for (int i = 0; i < sorted.size(); i += 10) {
                List<FCommand> currentPage = sorted.subList(i, (i + 10 < sorted.size() ? i + 10 : sorted.size()));
                EmbedBuilder eBuilder = new EmbedBuilder();
                eBuilder.setTitle(c.getName());
                eBuilder.setDescription(StringUtils.join(currentPage.stream().map(cmd -> (Floatzel.isdev ? Floatzel.conf.getDevprefix() : Floatzel.conf.getPrefix()) + cmd.getName() + " - " + cmd.getHelp()).collect(Collectors.toList()), "\n"));
                eBuilder.setFooter("Floatzel v" + Floatzel.version, null);
                eBuilder.setAuthor("Fuck you, I'm floatzel.", null, event.getSelfUser().getAvatarUrl());
                builder.addItems(eBuilder.build());
            }
        });

        builder.build().display(event.getChannel());

    }

    // more kekbot guts!
    private void sendCommandHelp(CommandEvent event, String commandName) {
        boolean found;
        Optional<Command> command = event.getClient().getCommands().stream().filter(c -> c.getName().equalsIgnoreCase(commandName)).findAny();
        //Check if the command exists, if it does, check if they have perms to view the command (or the command is hidden).
        if (command.isPresent()) {
            if (command.get().getCategory().getName().equalsIgnoreCase(owner.getName())) found = event.isOwner();
            else if (command.get().getCategory().getName().equalsIgnoreCase(admin.getName())) found = Utils.isAdmin(event.getAuthor().getId());
            else found = !command.get().getCategory().getName().equalsIgnoreCase("unassigned");
        } else found = false;


        if (found) {
            event.getChannel().sendMessage(getCommandHelp(event, command.get())).queue();
        } else event.getChannel().sendMessage("That is not a mother fucking command!").queue();
    }

    private MessageEmbed getCommandHelp(CommandEvent event, Command command) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("Command:", command.getName(), true);
        builder.addField("Category:", command.getCategory().getName(), true);
        if (command.getAliases().length > 0) builder.addField("Aliases:", StringUtils.join(command.getAliases(), ", "), false);
        builder.addField("Description:", command.getHelp(), false);
        //builder.addField("Usage (<> Required, {} Optional):", StringUtils.join(command.getUsage().stream().map(usage -> event.getPrefix() + usage).collect(Collectors.toList()), "\n"), false);
        builder.setFooter("Fuckin' Floatzel " + Floatzel.version, null);
        builder.setAuthor("Fuck you, I'm Floatzel", null, event.getSelfUser().getAvatarUrl());
        return builder.build();
    }
}
