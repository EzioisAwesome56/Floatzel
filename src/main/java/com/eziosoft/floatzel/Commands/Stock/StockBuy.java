package com.eziosoft.floatzel.Commands.Stock;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class StockBuy extends FCommand {
    public StockBuy(){
        name = "stockbuy";
        description = "Allows you to invest in a stock on the stock market";
        category = stocks;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // check if the user even has stocks yet
        String uid = event.getAuthor().getId();
        boolean hasstocks = Database.dbcheckifstock(uid);
        if (hasstocks){
            event.getChannel().sendMessage("You cant buy more then 1 stock dumbass!").queue();
            return;
        }
        // then start checking for the id
        int id = 0;
        try {
            id = Integer.valueOf(argsplit[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Error: you didnt provide an id you moron!\nyou can get the id by running the viewstocks command!").queue();
            return;
        }
        if (id == 0){
            event.getChannel().sendMessage("Error: Stock id 0 is a known invalid id!").queue();
            return;
        } else if (!Database.dbvalidatestockid(id)){
            event.getChannel().sendMessage("Error: invalid stock id! You can find stock ids in the viewstocks command!").queue();
            return;
        }
    }
}
