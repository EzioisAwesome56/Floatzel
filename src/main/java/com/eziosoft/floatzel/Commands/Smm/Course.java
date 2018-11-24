package com.eziosoft.floatzel.Commands.Smm;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.smm4j.Smm4j;
import com.eziosoft.smm4j.Util;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Course extends FCommand {
    public Course(){
        name = "course";
        description = "Shows you information about provided course id";
        category = smm;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String id = "";
        try {
            id = argsplit[0];
        } catch (ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Error: you didn't give me a fucking level id!").queue();
            return;
        }
        String[] levelinfo = Smm4j.getLevel(id);
        if (levelinfo[0].equals("error")){
            event.getChannel().sendMessage("Smm4j returned error. You may have requested an invalid course or are being rate limited").queue();
            return;
        }
        event.getChannel().sendMessage(Utils.buildSmm(levelinfo)).queue();
    }
}
