package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FunPorts extends FSlashCommand {

    public FunPorts(){
        name = "fun";
        help = "Runs floatzel's normal commands from the \"fun\" category";
        ephemeral = false;
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "name of command to run", "cmdname"));
        optlist.add(new SlashOption(OptionType.STRING, "additional arguments for command", "arg"));
        optlist.add(new SlashOption(OptionType.STRING, "name of cowfile for use with cowsay", "cow"));
    }
    @Override
    protected void execute(SlashCommandEvent e) {
        if (e.getOption("cmdname") != null) {
            SlashableCommandEntry sce = new SlashableCommandEntry(SlashActionGroup.FUN, e.getOption("cmdname").getAsString());
            if (Floatzel.scm.hasSlashAction(sce)) {
                Floatzel.scm.getSlashAction(sce).SlashCmdRun(e);
            } else {
                e.getHook().sendMessage("Error: that command does not exist!").queue();
            }
        } else {
            // first build the dropdown menu
            SelectionMenu.Builder b = SelectionMenu.create("fun:actions");
            for (Map.Entry<SlashableCommandEntry, FSlashableCommand> ent : Floatzel.scm.getActions()){
                if (ent.getKey().getGroup() == SlashActionGroup.FUN){
                    b.addOption(ent.getValue().getName(), ent.getKey().getName(), ent.getValue().getHelp());
                }
            }
            // finish the rest of it
            b.setPlaceholder("list of actions...");
            b.setRequiredRange(1, 1);
            b.addOption("Cancel", "cancel", "Cancels this operation");
            e.getHook().sendMessage("Please select an action from this list\n" +
                    "Protip: if you hate this menu, you can directly pass an action's name to the \"cmdname\" option to run it directly!")
                    .addActionRow(b.build()).queue();
            Floatzel.waiter.waitForEvent(Event.class, c -> doInteractionChecks(c, e), act -> doSelectionAction(e, act), 1, TimeUnit.MINUTES, () -> Utils.defaultTimeoutAction(e));
        }
    }

    private void doSelectionAction(SlashCommandEvent sce, Event e){
        SelectionMenuEvent sel = (SelectionMenuEvent) e;
        sce.getHook().editOriginalComponents().queue();
        if (sel.getInteraction().getValues().size() > 1){
            sce.getHook().editOriginal("Error: you can only select 1 action! Did you break your client to do this?").queue();
            return;
        }
        if (sel.getInteraction().getValues().contains("cancel")){
            sce.getHook().editOriginal("This operation has been cancelled.").queue();
            return;
        }
        Floatzel.scm.getSlashAction(new SlashableCommandEntry(SlashActionGroup.FUN, sel.getInteraction().getValues().get(0))).SlashCmdRun(sce);
    }

    private boolean doInteractionChecks(Event e, SlashCommandEvent sce){
        return e instanceof SelectionMenuEvent && ((SelectionMenuEvent) e).getInteraction().getMember().getId().equals(sce.getMember().getId())
                && ((SelectionMenuEvent) e).getGuild().getId().equals(sce.getGuild().getId())
                && ((SelectionMenuEvent) e).getChannel().getId().equals(sce.getChannel().getId());
    }
}
