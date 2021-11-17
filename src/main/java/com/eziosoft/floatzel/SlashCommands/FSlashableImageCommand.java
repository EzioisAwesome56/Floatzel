package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public abstract class FSlashableImageCommand extends FImageCommand {

    public SlashActionGroup sag;

    public void SlashImageCmdRun(SlashCommandEvent e){
        BufferedImage buf;
        try {
            buf = ImageIO.read(Utils.downloadImageAsHuman(e.getOption("image").getAsString()));
        } catch (Exception er){
            Error.CatchSlash(er, e);
            return;
        }
        SlashCmdRun(e, buf);
    }

    protected abstract void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff);
}
