package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.time.temporal.ChronoUnit;

public class Ping extends FCommand {
    public Ping(){
        name = "ping";
        category = other;
        description = "find out how slow the bot is";
    }

    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        // this totally wasnt stole from kekbot nope
        event.getChannel().sendMessage("Pinging... ").queue(m -> m.editMessage("\uD83C\uDFD3 Pong! `" + event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS)+ "ms`" +
                "\n\uD83D\uDC93 Heartbeat: `" + event.getJDA().getGatewayPing() + "ms`\n" +
                "Command totally not stolen from kekbot nope").queue());
    }
}
