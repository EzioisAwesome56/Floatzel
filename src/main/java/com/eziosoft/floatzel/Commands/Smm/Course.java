package com.eziosoft.floatzel.Commands.Smm;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.smm4j.Level;
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
    protected void cmdrun(CommandEvent event) throws NullPointerException{
        event.getChannel().sendTyping().queue();
        String id = "";
        Level levelinf;
        try {
            id = argsplit[0];
        } catch (ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Error: you didn't give me a fucking level id!").queue();
            return;
        }
        try {
            levelinf = Level.getLevel(id);
        } catch (NullPointerException e){
            throw e;
        }
        event.getChannel().sendMessage(Utils.buildSmm(levelinf)).queue();
    }
}
