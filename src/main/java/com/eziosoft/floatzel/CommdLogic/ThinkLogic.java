package com.eziosoft.floatzel.CommdLogic;

import org.apache.commons.lang3.StringUtils;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

public class ThinkLogic {

    public static String makeMessage(){
        return StringUtils.repeat("\uD83E\uDD14", 100);
    }

    public static void slackRun(SlackletRequest req, SlackletResponse resp){
        resp.reply(makeMessage());
        return;
    }
}
