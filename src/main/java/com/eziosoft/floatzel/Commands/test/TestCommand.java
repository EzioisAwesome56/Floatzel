package com.eziosoft.floatzel.Commands.Test;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TestCommand extends FCommand {

    public TestCommand() {
        name = "test";
        category = test;
        help = "tests to see if the bot is alive";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage("fuck you, this command does jack shit").queue();
    }
}
