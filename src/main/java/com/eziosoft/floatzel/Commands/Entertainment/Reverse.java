package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.io.InputStream;

public class Reverse extends FSlashableCommand {

    public Reverse(){
        name = "reverse";
        description = "Pull out everyone's favorite uno card";
        category = fun;
        sag = SlashActionGroup.FUN;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendTyping().queue();
        String card = getFileName();
        event.getChannel().sendFile(getCard(card), "reverse." + Utils.getFileType(card)).queue();
    }

    private String getFileName(){
        int card = random.nextInt(Files.unocards.length);
        // get card
        return Files.unocards[card];
    }

    private InputStream getCard(String fn){
        // generate a number
        return Utils.getResource("/uno/", fn);
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event, String... stuff) {
        String card = getFileName();
        event.getHook().sendFile(getCard(card), "reverse." + Utils.getFileType(card)).queue();
    }
}
