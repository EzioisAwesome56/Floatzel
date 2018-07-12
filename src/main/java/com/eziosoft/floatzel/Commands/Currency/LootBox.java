package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.ArrayList;
import java.util.Random;

public class LootBox extends FCommand {
    private final Random random = new Random();
    public LootBox(){
        name = "lootbox";
        description = "buy a loot box to win cards";
        category = money;
    }

    @Override
    protected void execute(CommandEvent event){
        String uid = event.getAuthor().getId();
        int bal = Database.dbloadint(uid);
        Random random = new Random();
        // check if the user has a bank account
        if (!Database.dbcheckifexist(uid)){
            System.out.println("New bank account created!");
        }
        // check if they have enough money for a loot box
        if (bal < 50){
            event.getChannel().sendMessage("You don't fucking have 50 \uD83E\uDD56 to afford a lootbox dumbass!").queue();
            return;
        }
        // remove money from there account
        bal = bal - 50;
        Database.dbsaveint(uid, bal);
        // rng for the lootbox type
        int box = random.nextInt(10) + 1;
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

    }
}
