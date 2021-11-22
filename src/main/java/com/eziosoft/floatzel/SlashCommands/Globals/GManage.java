package com.eziosoft.floatzel.SlashCommands.Globals;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.GuildSlashSettings;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashDataContainer;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ComponentLayout;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GManage extends FSlashCommand {

    public GManage(){
        name = "gmanage";
        help = "Manage slash commands for the bot in this server";
        optlist.add(new SlashOption(OptionType.STRING, "Action to preform", "action"));
        optlist.add(new SlashOption(OptionType.STRING, "String argument for action", "arg1"));
        needsServerAdmin = true;
        hasoptions = true;
        ephemeral = true;
    }
    @Override
    public void execute(SlashCommandEvent e) {
        if (e.getOption("action") != null) {
            String action = e.getOption("action").getAsString();
            if (action.equals("register")) {
                if (e.getOption("arg1") == null) {
                    e.getHook().sendMessage("no command name provided! You can use the \"list\" action to see all valid command names!").queue();
                    return;
                }
                String input = e.getOption("arg1").getAsString();
                if (!Floatzel.scm.hasRegisterable(input)) {
                    e.getHook().sendMessage("That is not a registerable slash command!").queue();
                    return;
                }
                FSlashCommand cmd = Floatzel.scm.getRegisterable(input);
                Floatzel.scm.addGuildCmd(new SlashDataContainer(cmd.name, e.getGuild().getId()), cmd);
                e.getHook().sendMessage("Registered command " + cmd.name).queue();
            } else if (action.equals("remove")) {
                if (e.getOption("arg1") == null) {
                    e.getHook().sendMessage("no command name provided!").queue();
                    return;
                }
                boolean win = Floatzel.scm.RemoveGuildCommand(new SlashDataContainer(e.getOption("arg1").getAsString(), e.getGuild().getId()));
                if (!win) {
                    e.getHook().sendMessage("Command failed to remove. Are you sure it was registered?").queue();
                } else {
                    e.getHook().sendMessage("Command removed successfully!").queue();
                }
            } else {
                // TODO: write help for this command
                e.getHook().sendMessage("help goes here").queue();
            }
        } else {
            // first, present the user with an option of what action they wish to preform
            SelectionMenu menu = SelectionMenu.create("manage:action")
                    .addOption("Register: used to enable slash commands", "register")
                    .addOption("Remove: disable slash commands for this server", "remove")
                    .setRequiredRange(1, 1)
                    .build();
            e.getHook().sendMessage("Please select an action").addActionRow(menu).queue();
            Floatzel.waiter.waitForEvent(Event.class, c -> (c instanceof SelectionMenuEvent) && ((SelectionMenuEvent) c).getInteraction().getMember().getUser().getId().equals(e.getMember().getUser().getId()),
                    act -> {
                        SelectionMenuEvent sce = (SelectionMenuEvent) act;
                        if (sce.getInteraction().getValues().size() != 1) {
                            if (sce.getInteraction().getValues().contains("register")) {
                                sce.deferEdit().queue();
                                SelectionMenu.Builder b = SelectionMenu.create("manage:register");
                                // get the current guild slash settings
                                GuildSlashSettings gss = Floatzel.scm.getGuildSlashSettings(e.getGuild().getId());
                                for (Map.Entry<String, FSlashCommand> ent : Floatzel.scm.getAllRegisterables().entrySet()) {
                                    if (gss.getRegistered().contains(ent.getKey())) {
                                        // this command is already registered, dont let them register it again!
                                        continue;
                                    }
                                    b.addOption(ent.getValue().name + ": " + ent.getValue().help, ent.getKey());
                                }
                                b.setPlaceholder("list of commands");
                                b.setRequiredRange(1, 1);
                                sce.getHook().editOriginalComponents(ActionRow.of(b.build())).queue();
                                sce.getHook().editOriginal("Please pick a command to enable!").queue();
                                // TODO: handle the command selection and enabling it
                            }
                        } else {
                            sce.editSelectionMenu(null).queue();
                            sce.getHook().editOriginal("Error: you're not supposed to choose more then 1 option! Did you break your client?").queue();
                        }
                    }, 1, TimeUnit.MINUTES,
                    () -> {
                       e.getHook().deleteOriginal().queue();
                       e.getHook().setEphemeral(true).sendMessage("You took too long to pick an option!").queue();
                    });
        }
    }
}
