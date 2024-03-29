package com.eziosoft.floatzel.SlashCommands.Globals;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.GuildSlashSettings;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashDataContainer;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.Util.Utils;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

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
                    .addOption("Register", "register", "used to enable slash commands")
                    .addOption("Remove", "remove", "disable slash commands for this server")
                    .addOption("Cancel", "cancel", "Cancel this operation")
                    .setRequiredRange(1, 1)
                    .build();
            e.getHook().sendMessage("Please select an action").addActionRow(menu).queue();
            Floatzel.waiter.waitForEvent(Event.class, c -> checkUserAndGuildOrigin(e, c),
                    act -> { doSelectionAction(e, act); }, 1, TimeUnit.MINUTES,
                    () -> {
                        Utils.defaultTimeoutAction(e);
                    });
        }
    }

    private boolean checkUserAndGuildOrigin(SlashCommandEvent e, Event et){
        return (et instanceof SelectionMenuEvent) &&
                ((SelectionMenuEvent) et).getInteraction().getMember().getUser().getId().equals(e.getMember().getUser().getId())
                && ((SelectionMenuEvent) et).getGuild().getId().equals(e.getGuild().getId());
    }

    private void doRegisterAction(SlashCommandEvent e, Event act){
        SelectionMenuEvent sce = (SelectionMenuEvent) act;
        sce.editSelectionMenu(null).queue();
        if (sce.getInteraction().getValues().contains("cancel")){
            e.getHook().editOriginal("Operation cancelled").queue();
            return;
        }
        if (sce.getInteraction().getValues().size() > 1){
            sce.getHook().editOriginal("Error: you're not supposed to choose more then 1 option! Did you break your client?").queue();
            return;
        }
        // just call the register function
        Floatzel.scm.addGuildCmd(new SlashDataContainer(sce.getInteraction().getValues().get(0), sce.getGuild().getId()), Floatzel.scm.getRegisterable(sce.getInteraction().getValues().get(0)));
        sce.getHook().editOriginal("Command registered successfully!").queue();
    }

    private void doSelectionAction(SlashCommandEvent e, Event act){
        // get the current guild slash settings
        boolean hasSettings = Floatzel.scm.hasSlashSettings(e.getGuild().getId());
        GuildSlashSettings gss = null;
        if (hasSettings){
            gss = Floatzel.scm.getGuildSlashSettings(e.getGuild().getId());
        }
        SelectionMenuEvent sce = (SelectionMenuEvent) act;
        if (sce.getInteraction().getValues().size() == 1) {
            if (sce.getInteraction().getValues().contains("register")) {
                sce.deferEdit().queue();
                SelectionMenu.Builder b = SelectionMenu.create("manage:register");
                for (Map.Entry<String, FSlashCommand> ent : Floatzel.scm.getAllRegisterables().entrySet()) {
                    if (hasSettings) {
                        if (gss.getRegistered().contains(ent.getKey())) {
                            // this command is already registered, dont let them register it again!
                            continue;
                        }
                    }
                    b.addOption(ent.getValue().name, ent.getKey(), ent.getValue().help);
                }
                b.addOption("Cancel", "cancel", "Cancel this operation");
                b.setPlaceholder("list of commands");
                b.setRequiredRange(1, 1);
                sce.getHook().editOriginalComponents(ActionRow.of(b.build())).queue();
                sce.getHook().editOriginal("Please pick a command to enable!").queue();
                Floatzel.waiter.waitForEvent(Event.class, c -> checkUserAndGuildOrigin(e, c),
                        act2 -> { doRegisterAction(e, act2); }, 1, TimeUnit.MINUTES, () -> Utils.defaultTimeoutAction(sce));
            } else if (sce.getInteraction().getValues().contains("remove")){
                // do remove gui here
                sce.deferEdit().queue();
                // build the menu
                if (hasSettings) {
                    SelectionMenu.Builder b = SelectionMenu.create("manage:remove");
                    if (gss.getRegistered().size() < 1){
                        sce.getHook().editOriginal("This guild has no commands to disable!").queue();
                        return;
                    }
                    for (String s : gss.getRegistered()) {
                        FSlashCommand c = Floatzel.scm.getRegisterable(s);
                        b.addOption(c.name, s, c.help);
                    }
                    b.addOption("Cancel", "cancel", "Cancel this operation");
                    b.setPlaceholder("List of currently active commands");
                    b.setRequiredRange(1, 1);
                    sce.getHook().editOriginalComponents(ActionRow.of(b.build())).queue();
                    sce.getHook().editOriginal("Please pick a command to remove from this list").queue();
                    Floatzel.waiter.waitForEvent(Event.class, c -> checkUserAndGuildOrigin(e, c), act2 -> doRemoveSelectionAction(e, act2),
                            1, TimeUnit.MINUTES, () -> Utils.defaultTimeoutAction(sce));
                } else {
                    sce.getHook().editOriginal("This guild has no commands to disable!").queue();
                    return;
                }
            } else if (sce.getInteraction().getValues().contains("cancel")){
                e.getHook().editOriginalComponents().queue();
                e.getHook().editOriginal("Operation cancelled").queue();
            }
        } else {
            sce.editSelectionMenu(null).queue();
            sce.getHook().editOriginal("Error: you're not supposed to choose more then 1 option! Did you break your client?").queue();
        }
    }

    private void doRemoveSelectionAction(SlashCommandEvent e, Event ent){
        SelectionMenuEvent sce = (SelectionMenuEvent) ent;
        if (sce.getInteraction().getValues().size() == 1){
            sce.deferEdit().queue();
            if (sce.getInteraction().getValues().get(0).equals("cancel")){
                e.getHook().editOriginalComponents().queue();
                e.getHook().editOriginal("Operation cancelled.").queue();
            } else {
                e.getHook().editOriginalComponents().queue();
                // try to unregister the command
                boolean win = Floatzel.scm.RemoveGuildCommand(new SlashDataContainer(sce.getInteraction().getValues().get(0), sce.getGuild().getId()));
                e.getHook().editOriginal(win ? "Command has been removed successfully!" : "Command failed to remove, was it enabled in the first place?").queue();
            }
        } else {
            e.getHook().editOriginalComponents().queue();
            e.getHook().editOriginal("Error: you're not supposed to choose more then 1 option! Did you break your client?").queue();
        }
    }
}
