package com.eziosoft.floatzel.Commands.Debug;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Debug extends FCommand {
    public Debug(){
        name = "debug";
        help = "Debugs whatever is coded in";
        category = owner;
        ownerCommand = true;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        event.getChannel().sendMessage("error: nothing").queue();
    }

}
