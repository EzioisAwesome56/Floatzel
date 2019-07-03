package com.eziosoft.floatzel.CommdLogic;

import com.eziosoft.floatzel.Floatzel;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;

import java.util.Random;

public class SwearCombineLogic {

    public static String makeMessage(){
        Random random = new Random();;
        String[] words = {"fuck", "ass", "shit", "what", "die", "fucking", "hecking", "you", "nibba", "gay ass pingu", "fucktart", "asswipe", "fuckhead", "asshat", "shitface",
                "assholefuckface", "dickface", "fucktard", "fuckfart", "nibba"};
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
        if (!Floatzel.joke) {
            return msg;
        } else {
            return "** **";
        }
    }

    public static void slackRun(SlackletRequest req, SlackletResponse resp){
        resp.reply(makeMessage());
    }
}
