package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.StockUtil;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Force extends FCommand {
    public Force(){
        name = "force";
        description = "Forces a event to take place";
        category = owner;
        ownerCommand = true;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        int arg = Integer.valueOf(event.getArgs());
        if (arg == 1){
            // force a stock update
            StockUtil.updateStock();
            event.getChannel().sendMessage("Forced stock update!").queue();
        } else if (arg == 2){
            // throw a fake error message
            Error.SpecialError("Caused by Force.java", "The user forced this crash");
        }


        else {
            event.getChannel().sendMessage("Unknown argument!").queue();
        }
    }
}
