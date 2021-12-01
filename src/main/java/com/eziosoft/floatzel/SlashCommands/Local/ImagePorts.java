package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableImageCommand;
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

public class ImagePorts extends FSlashCommand {

    public ImagePorts(){
        name = "image";
        help = "Runs floatzel's image manipulation commands";
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "Image url to download and manipulate", "image", true));
        optlist.add(new SlashOption(OptionType.STRING, "command to run", "cmdname"));
    }
    @Override
    protected void execute(SlashCommandEvent e) {
        if (e.getOption("cmdname") != null) {
            SlashableCommandEntry sce = new SlashableCommandEntry(SlashActionGroup.IMAGE, e.getOption("cmdname").getAsString());
            if (Floatzel.scm.hasSlashableImageAction(sce)) {
                e.getHook().sendMessage(Floatzel.emojiManager.getRandomLoadEmote() + " now processing image...").queue();
                Floatzel.scm.getSlashImageAction(sce).SlashImageCmdRun(e);
            } else {
                e.getHook().sendMessage("Error: that command does not exist!").queue();
            }
        } else {
            SelectionMenu.Builder men = SelectionMenu.create("image:actions");
            // first generate the list of actions to preform
            for (Map.Entry<SlashableCommandEntry, FSlashableImageCommand> ent : Floatzel.scm.getImageActions()){
                men.addOption(ent.getValue().getName(), ent.getKey().getName(), ent.getValue().getHelp());
            }
            // build the rest of the menu
            men.addOption("Cancel", "cancel", "Cancel this operation");
            men.setPlaceholder("image effects...");
            men.setRequiredRange(1, 1);
            e.getHook().sendMessage("Please select what effect you want to apply to your image\n" +
                    "Protip: if you dont like this menu, you can supply the effect name to the option \"cmdname\" on this command!").addActionRow(men.build()).queue();
            Floatzel.waiter.waitForEvent(Event.class, c -> doInteractionChecks(c, e), act -> doImageAction(e, act), 1, TimeUnit.MINUTES, () -> Utils.defaultTimeoutAction(e));
        }
    }

    private boolean doInteractionChecks(Event e, SlashCommandEvent sce){
        return e instanceof SelectionMenuEvent && ((SelectionMenuEvent) e).getInteraction().getMember().getId().equals(sce.getMember().getId())
                && ((SelectionMenuEvent) e).getGuild().getId().equals(sce.getGuild().getId())
                && ((SelectionMenuEvent) e).getChannel().getId().equals(sce.getChannel().getId());
    }

    private void doImageAction(SlashCommandEvent e, Event event){
        // did the user pick cancel?
        SelectionMenuEvent sce = (SelectionMenuEvent) event;
        sce.deferEdit().queue();
        sce.getHook().editOriginalComponents().queue();
        if (sce.getInteraction().getValues().contains("cancel")){
            sce.getHook().editOriginal("This operation has been cancelled.").queue();
            return;
        }
        if (sce.getInteraction().getValues().size() > 1){
            sce.getHook().editOriginal("Error: you can only select 1 action at a time! how did you even manage this?").queue();
            return;
        }
        // get a random emote for loading gif thing
        sce.getHook().editOriginal(Floatzel.emojiManager.getRandomLoadEmote() + " now processing image...").queue();
        Floatzel.scm.getSlashImageAction(new SlashableCommandEntry(SlashActionGroup.IMAGE, sce.getInteraction().getValues().get(0))).SlashImageCmdRun(e);
    }
}
