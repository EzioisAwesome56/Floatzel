package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Commands.Plugin.BasePlugin;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Plugin;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RunPlugin extends FCommand {
    public RunPlugin(){
        name = "run";
        description = "Fucking runs a javascript plugin";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event){
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
            // TODO: check if the file name is valid
            String[] info = Plugin.getPluginInfo(argsplit[1]);
            if (info[0].equals("fuck")){
                return;
            }
            // then register it
            Floatzel.commandClient.addCommand(new BasePlugin(info[0], argsplit[1], info[1]));
            event.getChannel().sendMessage("Plugin has been fucking loaded!").queue();
            return;
        }
        Plugin.runPlugin(event, argsplit[0]);
        return;
    }
}
