package com.eziosoft.floatzel.CommdLogic;

import org.apache.commons.lang3.StringUtils;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

import java.util.Arrays;

public class SpamLogic {
    public static String makeMessage(String what){
        // taken from the old spam.java
        String[] bad = {"`", "*", "_"};
        // main code
        if (what.length() == 0) {
            return  "You didn't fucking give me anything to spam dumbass!";
        } else if (what.length() > 1) {
            return "That is not one single character dumbass!";
        } else {
            if (Arrays.stream(bad).anyMatch(s -> s.equals(what))) {
                return "you can't use that fucking character dumbass";
            }

            return StringUtils.repeat(what, 2000);
        }
    }

    public static void slackRun(SlackletRequest req, SlackletResponse resp, String arg){
        resp.reply(makeMessage(arg));
        return;
    }
}
