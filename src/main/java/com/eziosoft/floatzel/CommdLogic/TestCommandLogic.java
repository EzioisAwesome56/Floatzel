package com.eziosoft.floatzel.CommdLogic;

import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

public class TestCommandLogic {

    public static String makeMessage(){
        return "fuck you, this command does jack shit";
    }

    public static void slackRun(SlackletRequest req, SlackletResponse resp){
        resp.reply(makeMessage());
    }
}
