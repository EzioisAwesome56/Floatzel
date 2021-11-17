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

    protected abstract void onExecuted(CommandEvent e);

    public String getExtendedDescription(){
        StringBuilder b = new StringBuilder();
        b.append("```\n");
        b.append("Usage (<> Required, {} Optional):\n\n");
        for (String u : usage){
            b.append(u).append("\n");
        }
        b.append("\n");
        if (extendedDescription != null){
            b.append(extendedDescription);
        }
        b.append("\n").append("```");
        return b.toString();
    }

    // this isnt really used, but it has to be here anyway
    protected enum ExtendedPosition {
        //Before usage.
        BEFORE,
        //After usage.
        AFTER;

        ExtendedPosition() {}
    }
}