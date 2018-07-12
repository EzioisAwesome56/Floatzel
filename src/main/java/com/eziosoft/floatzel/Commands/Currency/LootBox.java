package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

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


    }
}
