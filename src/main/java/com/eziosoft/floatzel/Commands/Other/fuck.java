package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Brainfuck;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

public class fuck extends FCommand {
    public fuck(){
        name = "brainfuck";
        help = "Parse and execute a Floatzel BrainFuck program";
        category = other;
        aliases = Utils.makeAlias("exec");
    }

    protected void cmdrun(CommandEvent event){
        event.reply("Parsing brainfuck, please wait...");
        event.getChannel().sendTyping().queue();
        event.reply(Brainfuck.ParseBrainFuck(argsplit[0]));
        return;
    }
}
