package com.eziosoft.floatzel.Commands.Test;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.CommdLogic.TestCommandLogic;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

public class TestCommand extends FCommand {

    public TestCommand() {
        name = "test";
        category = test;
        help = "tests to see if the bot is alive";
    }

    @Override
    protected void cmdrun(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(TestCommandLogic.makeMessage()).queue();
    }
}
