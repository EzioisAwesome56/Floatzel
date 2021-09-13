package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RunPlugin extends FCommand {
    public RunPlugin(){
        name = "command";
        description = "interfacing with bot commands!";
        category = owner;
        ownerCommand = true;
        aliases = Utils.makeAlias("cmd");
    }

    @Override
    protected void cmdrun(CommandEvent event) throws GenericException {
        // check to make sure they gave arguments
        if (event.getArgs().length() < 1) {
            event.getChannel().sendMessage("You didn't provide the function you want to preform!").queue();
            return;
        }
        if (argsplit[0].equals("plugin")) {
            event.reply("this feature has been moved to the floatzel-nashorn mod.");
        } else if (argsplit[0].equals("unload")){
            // try to unload the command given
            try{
                Floatzel.commandClient.removeCommand(argsplit[1]);
            } catch (ArrayIndexOutOfBoundsException e){
                event.reply("You didn't provide what command you want to unload!");
                return;
            }
        }
    }
}
