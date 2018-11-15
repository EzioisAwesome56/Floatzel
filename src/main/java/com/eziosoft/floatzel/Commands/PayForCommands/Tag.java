package com.eziosoft.floatzel.Commands.PayForCommands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Tag extends FCommand {
    public Tag(){
        name = "tag";
        help = "adds a tag to the server tag list";
        category = buyshit;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String args = event.getArgs();
        if (!Database.dbcheckiftag(event.getGuild().getId())){
            event.getChannel().sendMessage("Error: This guild has not purchased the tag command yet!").queue();
            return;
        }
    }
}
