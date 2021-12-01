package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtherPorts extends FSlashCommand {

    public OtherPorts(){
        name = "other";
        help = "Runs floatzel's normal commands from the \"other\" category";
        ephemeral = false;
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "name of command to run", "cmdname"));
        optlist.add(new SlashOption(OptionType.USER, "mention required for certain commands", "user"));
    }
    @Override
    protected void execute(SlashCommandEvent e) {
        if (e.getOption("cmdname") != null) {
            SlashableCommandEntry sce = new SlashableCommandEntry(SlashActionGroup.OTHER, e.getOption("cmdname").getAsString());
            if (Floatzel.scm.hasSlashAction(sce)) {
                Floatzel.scm.getSlashAction(sce).SlashCmdRun(e);
            } else {
                e.getHook().sendMessage("Error: that command does not exist!").queue();
            }
        } else {
            // first, built the list of actions a user can preform
            SelectionMenu.Builder b = SelectionMenu.create("other:actions");
            for (Map.Entry<SlashableCommandEntry, FSlashableCommand> ent : Floatzel.scm.getActions()){
                if (ent.getKey().getGroup() == SlashActionGroup.OTHER){
                    b.addOption(ent.getValue().getName(), ent.getKey().getName(), ent.getValue().getHelp());
                }
            }
            // get the other menu crap out of the way
            b.setRequiredRange(1, 1);
            b.setPlaceholder("list of actions...");
            b.addOption("Cancel", "cancel", "Cancel this operation");
            // send the messages
            e.getHook().sendMessage("Please pick an action from this list\n" +
                    "Protip: if you hate this menu, you can supply action name to the \"cmdname\" option!").addActionRow(b.build()).queue();
            Floatzel.waiter.waitForEvent(Event.class, c -> doInteractionChecks(c, e), act -> doSelectionAction(act, e), 1, TimeUnit.MINUTES, () -> Utils.defaultTimeoutAction(e));
        }
    }

    private boolean doInteractionChecks(Event e, SlashCommandEvent sce){
        return e instanceof SelectionMenuEvent && ((SelectionMenuEvent) e).getInteraction().getMember().getId().equals(sce.getMember().getId())
                && ((SelectionMenuEvent) e).getGuild().getId().equals(sce.getGuild().getId())
                && ((SelectionMenuEvent) e).getChannel().getId().equals(sce.getChannel().getId());
    }

    private void doSelectionAction(Event e, SlashCommandEvent se){
        SelectionMenuEvent sce = (SelectionMenuEvent) e;
        se.getHook().editOriginalComponents().queue();
        if (sce.getInteraction().getValues().contains("cancel")){
            se.getHook().editOriginal("Operation cancelled").queue();
            return;
        }
        if (sce.getInteraction().getValues().size() > 1){
            se.getHook().editOriginal("Error: you can only select 1 item! Did you break your client to do this?").queue();
            return;
        }
        Floatzel.scm.getSlashAction(new SlashableCommandEntry(SlashActionGroup.OTHER, sce.getInteraction().getValues().get(0))).SlashCmdRun(se);
    }
}
