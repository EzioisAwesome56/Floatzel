package com.eziosoft.floatzel.CommdLogic;

import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

import java.util.Random;

public class SwearCombineLogic {

    public static String makeMessage(){
        Random random = new Random();;
        String[] words = {"fuck", "ass", "shit", "kill yourself", "die", "fucking", "hecking", "you", "nibba", "gay ass pingu", "fucktart", "asswipe", "fuckhead", "asshat", "shitface",
                "assholefuckface", "dickface", "fucktard", "fuckfart", "nigger"};
        int maxnumb = words.length;
        int sentlong = random.nextInt(10) + 1;
        int count = 0;
        String msg = "";
        // form the sentance
        while (count != sentlong){
            msg = msg + words[random.nextInt(maxnumb)] + " ";
            count = count + 1;
        }
        // return it
        return msg;
    }

    public static void slackRun(SlackletRequest req, SlackletResponse resp){
        resp.reply(makeMessage());
    }
}
