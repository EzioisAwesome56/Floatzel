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
        String args = argsplit[0];
        boolean perm = Database.dbcheckiftag(event.getGuild().getId());
        if(args.equals("check")){
            if(!perm){
                event.getChannel().sendMessage("This guild has not purchased the tag command!").queue();
                return;
            } else {
                event.getChannel().sendMessage("This guild can use the tag command!").queue();
                return;
            }
        }
    }
}
