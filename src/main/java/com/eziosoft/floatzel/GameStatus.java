package com.eziosoft.floatzel;

import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import static com.eziosoft.floatzel.Floatzel.jda;

public class GameStatus extends TimerTask {

    public GameStatus() {}

    private Random random = new Random();

    String[] games = { "Windows 7", "mspaint.exe", "&help", "Nintendo Gamecube", "GIMP startup",
            "fucking weed", "Sonic 09", "Dying in a hole", "gay xd", "Exploding North Korea", "Being a terrorist", "BSOD",
            "Extra gay", "memerino and the dank machine", "Dankform", "Half Life 4", "18 is a god", "SaltyPepper", "@here",
            "Simpsons", "Explosive cheese", "Being a bot", "hi Cublex", "Pentium 3", "meme machine", "18 is bisexual", "ezio is bi",
            "PyCharm", "Rectangle Gay", "Making the frogs gay", "eirrac", "what the fuck is a hedgehog", "heck you blur", "enitsirhc", "fuck me harder daddy",
            "Intelij", "Smoking weed", "Persona -12 + GAY DLC", "Circles", "Godson is radical", "Ryan is gay", "Java 10", "with Yukari", "Super Dankio Ocyssey",
            "Fucking Mitsuru with a stick", "Persona: Waifu simulator", "Ezio X Yukari", "Yukari is hot", "Ezio is dumb", "Smelling Ralsei's feet", "Licking Ralsei feetz",
            "Calling 18 a gaylord", "Watching Mitsuru and Ralsei fuck", "DMAN has the largest gay", "TOUHOU", "Nintendo", "Linux, bitch", "Mozilla Chrome", "Shooting Internet Explorer", "hi esmbot",
            "Cirno is dumb lol"};

    String[] stupid = {"I failed math", "buy my water flavoured ice!", "why does everyone hate me :C", "Ice > weed", "I have -2 iq"};

    @Override
    public void run() {
        if (!Floatzel.joke) {
            int index = random.nextInt(games.length);
            int type = random.nextInt(3);
            if (type == 0) {
                jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.watching(games[index])));
            } else if (type == 1){
                jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.listening(games[index])));
            } else {
                jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.playing(games[index])));
            }
        } else {
            int index = random.nextInt(stupid.length);
            jda.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.playing(stupid[index])));
        }
    }
}
