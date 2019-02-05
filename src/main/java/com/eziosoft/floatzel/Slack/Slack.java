package com.eziosoft.floatzel.Slack;

import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;

import java.io.IOException;

public class Slack {

    public static void StartSlack(String token) throws IOException {

        SlackletService slackService = new SlackletService(token);

        slackService.addSlacklet(new Slacklet() {

            @Override
            public void onMessagePosted(SlackletRequest req, SlackletResponse resp) {
                // user posted message and BOT intercepted it

                // get message content
                String content = req.getContent();

                // reply to the user
                resp.reply("You say '" + content + "'.");
            }
        });

        slackService.start();
        return;
    }
}
