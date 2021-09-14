package com.eziosoft.floatzel.kekbot.KekGlueUtil;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.kekbot.KekGlue.CommandEvent;

import java.util.ArrayList;

// kekbot event compatibility using this shim class
public abstract class KekCommand extends FCommand {

    // these probably wont get used unless i rewrite how help works
    protected String extendedDescription = null;
    protected ArrayList<String> usage = new ArrayList<>();
    protected ExtendedPosition exDescPos;

    @Override
    protected void cmdrun(com.jagrosh.jdautilities.command.CommandEvent event) throws Exception {
        if ( Floatzel.isdev) event.getChannel().sendMessage("Running command in KekBot Compatibility Mode...").queue();
        onExecuted(new CommandEvent(event, argsplit));
    }

    public abstract void onExecuted(CommandEvent e);

    // this will do fucking nothing
    protected enum ExtendedPosition {
        //Before usage.
        BEFORE,
        //After usage.
        AFTER;

        ExtendedPosition() {}
    }
}