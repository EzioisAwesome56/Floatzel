package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Res.Files;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Utils;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class LootBox extends FCommand {
    public LootBox(){
        name = "lootbox";
        description = "buy a totally-not-rigged box to win money\n" +
                "one box costs 50"+ moneyicon;
        category = money;
        aliases = Utils.makeAlias("box");
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException, IOException {
        BufferedImage boximg = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String uid = event.getAuthor().getId();
        // check if the user has a bank account
        if (!Database.dbcheckifexist(uid)){
            event.reply("You don't even have 50"+ moneyicon + " to fucking pay for this!");
            return;
        }
        int bal = Database.dbloadint(uid);
        // check if they have enough money for a loot box
        if (bal < 50){
            event.getChannel().sendMessage("You don't fucking have 50" + moneyicon + " to afford a lootbox dumbass!").queue();
            return;
        }
        // remove money from there account
        Database.dbsaveint(uid, bal - 50);
        // rng for the lootbox type
        int box = random.nextInt(10) + 1;
        // this is for later
        int reward = 0;
        // arrays
        // numbers for tier one
        ArrayList<Integer> shit = new ArrayList<Integer>(){{
            add(1);
            add(3);
            add(5);
            add(7);
            add(8);
            add(9);
        }};
        // tier 2
        ArrayList<Integer> ok = new ArrayList<Integer>(){{
            add(2);
            add(4);
            add(6);
        }};
        // tier 3
        ArrayList<Integer> best = new ArrayList<Integer>(){{
            add(10);
        }};
        // test for what box got picked
            if (shit.contains(box)) {
                boximg = ImageIO.read(Files.class.getResource("/box/"+ Files.boxes[0]));
                ImageIO.setUseCache(false);
                stream.flush();
                ImageIO.write(boximg, "png", stream);
                event.getChannel().sendMessage("Take this shitty **level 1** box!").addFile(stream.toByteArray(), "box.png").queue();
                // generate reward
                reward = random.nextInt(5) + 50;
                event.reply("Opening the box, you find "+ Integer.toString(reward) + moneyicon + " in the box!");
                Database.dbsaveint(uid, Database.dbloadint(uid) + reward);
                return;
            } else if (ok.contains(box)) {
                boximg = ImageIO.read(Files.class.getResource("/box/"+Files.boxes[1]));
                ImageIO.setUseCache(false);
                stream.flush();
                ImageIO.write(boximg, "png", stream);
                event.getChannel().sendMessage("have this fucking AVERAGE **level 2** box").addFile(stream.toByteArray(), "box.png").queue();
                // generate reward
                reward = random.nextInt(20) + 75;
                event.reply("Opening the box, you find "+ Integer.toString(reward) + moneyicon + " in the box!");
                Database.dbsaveint(uid, Database.dbloadint(uid) + reward);
                return;
            } else if (best.contains(box)) {
                boximg = ImageIO.read(Files.class.getResource("/box/"+Files.boxes[2]));
                ImageIO.setUseCache(false);
                stream.flush();
                ImageIO.write(boximg, "png", stream);
                event.getChannel().sendMessage("You have my blessing, good sir, have a **level 3** box").addFile(stream.toByteArray(), "box.png").queue();
                // generate reward
                reward = random.nextInt(100) + 125;
                event.reply("Opening the box, you find "+ Integer.toString(reward) + moneyicon + " in the box!");
                Database.dbsaveint(uid, Database.dbloadint(uid) + reward);
                return;
            }

    }
}
