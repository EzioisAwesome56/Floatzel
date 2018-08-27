package com.eziosoft.floatzel;

import net.dv8tion.jda.core.entities.Game;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class GameStatus extends TimerTask {

    public GameStatus() {}

    private Random random = new Random();

    String[] games = { "Windows 7", "mspaint.exe", "&help", "Nintendo Gamecube", "GIMP startup",
            "fucking weed", "Sonic 09", "Dying in a hole", "gay xd", "Exploding North Korea", "Being a terrorist", "BSOD",
            "Extra gay", "memerino and the dank machine", "Dankform", "Half Life 4", "18 is a god", "SaltyPepper", "@here",
            "Simpsons", "Explosive cheese", "Being a bot", "hi Cublex", "Pentium 3", "meme machine", "18 is bisexual", "ezio is bi",
            "PyCharm", "Rectangle Gay", "Making the frogs gay", "eirrac", "what the fuck is a hedgehog", "heck you blur", "enitsirhc", "fuck me harder daddy",
            "Intelij", "Smoking weed", "Persona -12 + GAY DLC", "Circles", "Godson is radical", "Ryan is gay", "Java 10", "with Yukari", "Super Dankio Ocyssey",
            "Fucking Mitsuru with a stick", "Persona: Waifu simulator"};

    @Override
    public void run() {
        int index = random.nextInt(games.length);
        Floatzel.jda.getShards().forEach(jda -> jda.getPresence().setGame(Game.playing(games[index])));
    }
}
