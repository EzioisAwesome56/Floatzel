package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class ImagePorts extends FSlashCommand {

    public ImagePorts(){
        name = "image";
        help = "Runs floatzel's image manipulation commands";
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "command to run", "cmdname", true));
        optlist.add(new SlashOption(OptionType.STRING, "Image url to download and manipulate", "image", true));
    }
    @Override
    protected void execute(SlashCommandEvent e) {
        SlashableCommandEntry sce = new SlashableCommandEntry(SlashActionGroup.IMAGE, e.getOption("cmdname").getAsString());
        if (Floatzel.scm.hasSlashableImageAction(sce)){
            Floatzel.scm.getSlashImageAction(sce).SlashImageCmdRun(e);
        } else {
            e.getHook().sendMessage("Error: that command does not exist!").queue();
        }
    }
}
