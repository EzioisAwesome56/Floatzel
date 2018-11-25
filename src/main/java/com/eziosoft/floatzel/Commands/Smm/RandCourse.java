package com.eziosoft.floatzel.Commands.Smm;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.smm4j.Level;
import com.eziosoft.smm4j.Smm4j;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.eziosoft.smm4j.Util.GetRandomLevelID;

public class RandCourse extends FCommand {
    public RandCourse(){
        name = "randcourse";
        description = "Grabs a persudo random course from the bookmark site";
        category = smm;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendTyping().queue();
        String id = GetRandomLevelID();
        if (id.equals("error")){
            event.getChannel().sendMessage("Smm4j encountered an error! You are probably being rated limited by nintendo. Try again in an hour").queue();
            return;
        }
        // then get level information
        Level levelinf = Smm4j.getLevel(id);
        event.getChannel().sendMessage(Utils.buildSmm(levelinf)).queue();
    }
}
