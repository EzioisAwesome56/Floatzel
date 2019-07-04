package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Admin;
import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.StockUtil;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Icon;

import java.io.IOException;

public class Force extends FCommand {
    public Force(){
        name = "force";
        description = "Forces a event to take place";
        category = owner;
        ownerCommand = true;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws GenericException, DatabaseException, IOException {
        int arg = Integer.valueOf(event.getArgs());
        if (arg == 1){
            // force a stock update
            StockUtil.updateStock();
            event.getChannel().sendMessage("Forced stock update!").queue();
        } else if (arg == 2){
            throw new GenericException("Caused by force command");
        } else if (arg == 3){
            Floatzel.joke = true;
            // load the pfp while we are here
            Icon jokeicon = Icon.from(Utils.getResourse("/pfps/", "cirno.png"));
            Floatzel.jda.getShards().get(0).getSelfUser().getManager().setAvatar(jokeicon).queue();
            // also set the name
            event.getGuild().getController().setNickname(event.getSelfMember(), Floatzel.jokename).queue();
        } else if (arg == 4){
            Floatzel.joke = false;
            // set it back to normal
            Icon normalicon = Icon.from(Utils.getResourse("/pfps/", Floatzel.isdev ? "floatdev.png" : "float.png"));
            Floatzel.jda.getShards().get(0).getSelfUser().getManager().setAvatar(normalicon).queue();
            event.getGuild().getController().setNickname(event.getSelfMember(), Floatzel.isdev ? Floatzel.normalname + "Dev" : Floatzel.normalname).queue();
        } else if (arg == 5){
            event.reply(Boolean.toString(Admin.tweettog()));
        }


        else {
            event.getChannel().sendMessage("Unknown argument!").queue();
        }
    }
}
