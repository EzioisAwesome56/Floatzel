package com.eziosoft.floatzel;

import net.dv8tion.jda.api.entities.Activity;

import java.util.Random;
import java.util.TimerTask;

import static com.eziosoft.floatzel.Floatzel.jda;

public class GameStatus extends TimerTask {

    public GameStatus() {}

    private Random random = new Random();


    @Override
    public void run() {
        if (!Floatzel.joke) {
            int index = random.nextInt(Floatzel.gameTexts.getGames().length);
            int type = random.nextInt(3);
            if (type == 0) {
                jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.watching(Floatzel.gameTexts.getGames()[index])));
            } else if (type == 1){
                jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.listening(Floatzel.gameTexts.getGames()[index])));
            } else {
                jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.playing(Floatzel.gameTexts.getGames()[index])));
            }
        } else {
            int index = random.nextInt(Floatzel.gameTexts.getStupid().length);
            jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.playing(Floatzel.gameTexts.getStupid()[index])));
        }
    }
}
