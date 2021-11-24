package com.eziosoft.floatzel.Commands.Entertainment;

import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Random;

public class Shit8ball extends FSlashableCommand {

    public Shit8ball() {
        name = "8ball";
        description = "ask the magical 8ball a question";
        category = fun;
        sag = SlashActionGroup.FUN;
    }

    @Override
    protected void cmdrun(CommandEvent h) {
        // assuming this is how you check if the message is too long
        if (h.getArgs().length() < 0 || h.getArgs().length() == 0) {
            h.reply("You appear to have forgotten to ask me something");
        } else if (h.getArgs().length() > 1000) {
            h.reply("that question is too long");
        } else {
            h.getChannel().sendMessage(genMsg(h.getArgs())).queue();
        }
    }

    private String genMsg(String q){
        q = q.replace("@everyone", "stupid ping").replace("@here", "stupid ping");
        Random random = new Random();
        // yes responses
        String[] notassball = {"sure!", "no", "heck off and try again", "I don't care mate", "go away", "yes, now leave", "no no no no no no",
                "damn right", "nope!", "fbi's at my door, please try again", "why are you asking me???????"};
        // get the response
        String shit;
        String response;
        shit = notassball[random.nextInt(notassball.length)];
        // form the message
        response = "**You asked:** " + q + "\n**Answer:** " + shit;
        // finally: send the message
        return response;
    }

    @Override
    public void SlashCmdRun(SlashCommandEvent event) {
        if (event.getOption("arg") == null){
            event.getHook().editOriginal("You did not ask me a question!").queue();
            return;
        }  else if (event.getOption("arg").getAsString().length() > 200){
            event.getHook().editOriginal("Error: that message is too long!").queue();
            return;
        }
        event.getHook().editOriginal(genMsg(event.getOption("arg").getAsString())).queue();
    }
}
