package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class debug extends FSlashCommand {

    public debug(){
        name = "debug";
        help = "debug command. it stucks";
        needsServerAdmin = true;
        ephemeral = false;
    }

    @Override
    public void execute(SlashCommandEvent e) {
        e.getHook().sendMessage("test button").addActionRow(
                Button.primary("gamer", "test button")
        ).queue();
        Floatzel.waiter.waitForEvent(Event.class, p -> {
            if (p instanceof ButtonClickEvent){
                boolean a = ((ButtonClickEvent) p).getInteraction().getMember().getUser().getId().equals(e.getMember().getUser().getId());
                System.out.println(a);
                return a;
            } else {
                return false;
            }
        }, a -> {
            ButtonClickEvent bce = (ButtonClickEvent) a;
            bce.editButton(null).queue();
            bce.getHook().editOriginal("You are a pirate!").queue();
        }, 1, TimeUnit.MINUTES, () -> {
            e.getHook().deleteOriginal().queue();
            e.getHook().setEphemeral(true).sendMessage("You took too long!").queue();
        });
    }
}
