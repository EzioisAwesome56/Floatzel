package com.eziosoft.floatzel.Slack;

import com.eziosoft.floatzel.CommdLogic.TestCommandLogic;
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
                if(content.startsWith("&amp;")) {
                    // clean out a lot of shit
                    String[] raw = content.substring(5).split(" ");
                    String command = raw[0];
                    // TODO: arguments
                    // then find out what command it even is
                    switch (command){
                        case "test":
                            TestCommandLogic.slackRun(req, resp);
                            break;
                        default:
                            // do nothing because this isnt a command
                            break;
                    }
                }
            }
        });

        slackService.start();
        return;
    }
}
