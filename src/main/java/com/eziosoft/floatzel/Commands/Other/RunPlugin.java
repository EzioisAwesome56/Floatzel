package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Commands.Plugin.BasePlugin;
import com.eziosoft.floatzel.Exception.LoadPluginException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.Plugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.FileNotFoundException;

public class RunPlugin extends FCommand {
    public RunPlugin(){
        name = "plugin";
        description = "Plugin manipulation shitz";
        category = owner;
        ownerCommand = true;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws LoadPluginException{
        // check to make sure they gave arguments
        if (event.getArgs().length() < 1){
            event.getChannel().sendMessage("Fuckhead! You didnt provide the name of the plugin you want to run!").queue();
            return;
        }
        if (argsplit[0].equals("load")){
            // check to make sure the user provided a file name
            if (argsplit[1].isEmpty()){
                event.getChannel().sendMessage("Oi fuckface! you didnt provide a plugin filename for me to register!").queue();
                return;
            }
            String[] info;
            try {
                info = Plugin.getPluginInfo(argsplit[1]);
            } catch (FileNotFoundException e){
                event.getChannel().sendMessage("Error: that plugin doesnt fucking exist!").queue();
                return;
            } catch (LoadPluginException e){
                throw e;
            }
            if (info[0].equals("fuck!")){
                return;
            }
            // then register it
            Floatzel.commandClient.addCommand(new BasePlugin(info[0], argsplit[1], info[1]));
            event.getChannel().sendMessage("Plugin has been fucking loaded!").queue();
            return;
        } else if (argsplit[0].equals("run")){
            Plugin.runPlugin(event, argsplit[2]);
            return;
        }
    }
}
