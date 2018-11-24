package com.eziosoft.floatzel.Commands.Smm;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.smm4j.Smm4j;
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
            Error.Catch(e);
            return;
        }
        String[] levelinfo = Smm4j.getLevel(id);

    }
}
