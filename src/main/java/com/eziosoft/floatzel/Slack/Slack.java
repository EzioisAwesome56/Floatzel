package com.eziosoft.floatzel.Slack;

import com.eziosoft.floatzel.CommdLogic.*;
import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.riversun.xternal.simpleslackapi.SlackAttachment;
import org.riversun.xternal.simpleslackapi.SlackFile;

import java.io.IOException;

public class Slack {

    public static void StartSlack(String token) throws IOException {
        System.out.println("Floatzel is starting Slack frontend...");

        SlackletService slackService = new SlackletService(token);

        slackService.addSlacklet(new Slacklet() {

            @Override
            public void onMessagePosted(SlackletRequest req, SlackletResponse resp) {
                // user posted message and BOT intercepted

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
                        case "think":
                            ThinkLogic.slackRun(req, resp);
                            break;
                        case "spam":
                            SpamLogic.slackRun(req, resp, raw[1]);
                            break;
                        case "stats":
                            StatsLogic.slackRun(req, resp);
                            break;
                        case "gensent":
                            SwearCombineLogic.slackRun(req, resp);
                            break;
                        default:
                            // do nothing because this isnt a command
                            break;
                    }
                }
            }
        });

        slackService.start();
        System.out.println("Floatzel has started slack!");
        return;
    }
}
