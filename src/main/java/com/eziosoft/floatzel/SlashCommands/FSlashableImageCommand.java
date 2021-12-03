package com.eziosoft.floatzel.SlashCommands;

import com.eziosoft.floatzel.Commands.FImageCommand;
import com.eziosoft.floatzel.Exception.ImageDownloadException;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashActionGroup;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

public abstract class FSlashableImageCommand extends FImageCommand {

    public SlashActionGroup sag;

    public void SlashImageCmdRun(SlashCommandEvent e){
        BufferedImage buf;
        try {
            buf = ImageIO.read(Utils.downloadImageAsHuman(e.getOption("image").getAsString()));
        } catch (MalformedURLException malformedURLException){
            e.getHook().sendMessage("Error: that url is invalid").queue();
            return;
        } catch (ImageDownloadException ide){
            if (ide.getStatuscode() != -69){
                e.getHook().sendMessage("Error: that image returned " + ide.getStatuscode() + "\n" + ide.getMessage()).queue();
            } else {
                Error.CatchSlash(ide, e);
            }
            return;
        } catch (IOException io){
            Error.CatchSlash(io, e);
            return;
        }
        SlashCmdRun(e, buf);
    }

    protected abstract void SlashCmdRun(SlashCommandEvent event, BufferedImage stuff);
}
