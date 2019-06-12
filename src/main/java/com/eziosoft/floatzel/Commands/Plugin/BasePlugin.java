package com.eziosoft.floatzel.Commands.Plugin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Util.Plugin;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BasePlugin extends FCommand {
    private String filename;

    public BasePlugin(String cname, String fname, String phelp){
        name = cname;
        description = phelp;
        this.filename = fname;
        category = plugin;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws GenericException {
        // not much to see, just run the plugin
        Plugin.runPlugin(event, filename);
    }
}
