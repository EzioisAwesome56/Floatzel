package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashableCommandEntry;
import com.eziosoft.floatzel.SlashCommands.SlashActionGroup;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

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
        SelectionMenu menu = SelectionMenu.create("menu:test")
                .setPlaceholder("pick a button...")
                .setRequiredRange(1, 1)
                .addOption("dank", "dank")
                        .addOption("not dank", "bad")
                                .build();
        e.getHook().sendMessage("pick an option!").addActionRow(menu).queue();
        Floatzel.waiter.waitForEvent(Event.class, p -> {
            if (p instanceof SelectionMenuEvent){
                return ((SelectionMenuEvent) p).getInteraction().getMember().getUser().getId().equals(e.getMember().getUser().getId());
            } else {
                return false;
            }
        }, a -> {
            SelectionMenuEvent bce = (SelectionMenuEvent) a;
            String msg;
            if (bce.getInteraction().getValues().contains("dank")){
                msg = "you have selected: dank";
            } else {
                msg = "you have not selected dank. bruh.";
            }
            bce.editSelectionMenu(null).queue();
            bce.getHook().editOriginal(msg).queue();
        }, 1, TimeUnit.MINUTES, () -> {
            e.getHook().deleteOriginal().queue();
            e.getHook().setEphemeral(true).sendMessage("You took too long!").queue();
        });
    }
}
