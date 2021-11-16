package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Admin;
import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.Globals.GManage;
import com.eziosoft.floatzel.SlashCommands.SlashDataContainer;
import com.eziosoft.floatzel.SlashCommands.Local.debug;
import com.eziosoft.floatzel.Util.StockUtil;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Icon;

import java.io.IOException;

public class Force extends FCommand {
    public Force(){
        name = "force";
        description = "Event list:\n" +
                "1- force stock market update\n" +
                "2- trigger error handler\n" +
                "3- enable joke mode\n" +
                "4- disable joke mode\n" +
                "5- enable tweetbot\n" +
                "6- register manage command for debugging";
        category = owner;
        ownerCommand = true;
        aliases = Utils.makeAlias("event");
    }

    @Override
    protected void cmdrun(CommandEvent event) throws GenericException, DatabaseException, IOException {
        int arg;
        try {
            arg = Integer.parseInt(event.getArgs());
        } catch (NumberFormatException e){
            event.getChannel().sendMessage("that is not a number! Please only input a number!").queue();
            return;
        }
        if (arg == 1){
            // force a stock update
            StockUtil.updateStock();
            event.getChannel().sendMessage("Forced stock update!").queue();
        } else if (arg == 2){
            throw new GenericException("Caused by force command");
        } else if (arg == 3){
            Floatzel.joke = true;
            // load the pfp while we are here
            Icon jokeicon = Icon.from(Utils.getResource("/pfps/", "cirno.png"));
            Floatzel.jda.getShards().get(0).getSelfUser().getManager().setAvatar(jokeicon).queue();
            // also set the name
            event.getSelfMember().modifyNickname(Floatzel.jokename).queue();
        } else if (arg == 4){
            Floatzel.joke = false;
            // set it back to normal
            Icon normalicon = Icon.from(Utils.getResource("/pfps/", Floatzel.isdev ? "floatdev.png" : "float.png"));
            Floatzel.jda.getShards().get(0).getSelfUser().getManager().setAvatar(normalicon).queue();
            event.getSelfMember().modifyNickname(Floatzel.isdev ? Floatzel.normalname + "Dev" : Floatzel.normalname).queue();
        } else if (arg == 5){
            event.reply(Boolean.toString(Admin.tweettog()));
        } else if (arg == 6){
            Floatzel.scm.addGuildCmd(new SlashDataContainer("devmanage", event.getGuild().getId()), new GManage());
            Floatzel.scm.RegisterGuildCommands();
        }


        else {
            event.replyWarning("invalid command argument!");
        }
    }
}
