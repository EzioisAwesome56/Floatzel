package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Commands.Plugin.BasePlugin;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Exception.LoadPluginException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Plugin;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.FileNotFoundException;

public class RunPlugin extends FCommand {
    public RunPlugin(){
        name = "command";
        description = "fucking interfacing with bot commands!";
        category = owner;
        ownerCommand = true;
        aliases = Utils.makeAlias("cmd");
    }

    @Override
    protected void cmdrun(CommandEvent event) throws LoadPluginException, GenericException {
        // check to make sure they gave arguments
        if (event.getArgs().length() < 1) {
            event.getChannel().sendMessage("Fuckhead! You didn't provide the function you want to preform!").queue();
            return;
        }
        if (argsplit[0].equals("plugin")) {
            if (argsplit[1].equals("load")) {
                // check to make sure the user provided a file name
                try {
                    if (argsplit[2].isEmpty()) {
                        event.getChannel().sendMessage("Oi fuckface! you didnt provide a plugin filename for me to register!").queue();
                        return;
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    event.reply("You dumb idiot! You didnt provide a plugin filename for me to load!");
                    return;
                }
                String[] info;
                try {
                    info = Plugin.getPluginInfo(argsplit[1]);
                } catch (FileNotFoundException e) {
                    event.getChannel().sendMessage("Error: that plugin doesnt fucking exist!").queue();
                    return;
                } catch (LoadPluginException e) {
                    throw e;
                } catch (ArrayIndexOutOfBoundsException e){
                    event.reply("Oi dumbfuck! You didnt tell me what plugin you wanted to load!");
                    return;
                }
                // then register it
                Floatzel.commandClient.addCommand(new BasePlugin(info[0], argsplit[2], info[1]));
                event.getChannel().sendMessage("Plugin has been fucking loaded!").queue();
                return;
            } else if (argsplit[1].equals("run")) {
                try {
                    Plugin.runPlugin(event, argsplit[2]);
                } catch (ArrayIndexOutOfBoundsException e){
                    event.reply("Oi moron, you didnt tell me what to run!");
                    return;
                } catch (GenericException e){
                    throw e;
                }
            }
        } else if (argsplit[0].equals("unload")){
            // try to unload the command given
            try{
                Floatzel.commandClient.removeCommand(argsplit[1]);
            } catch (ArrayIndexOutOfBoundsException e){
                event.reply("DO'H! You didn't provide what command you want to unload!");
                return;
            }
        }
    }
}
