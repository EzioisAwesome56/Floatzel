package com.eziosoft.floatzel.Commands.Stock;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.eziosoft.floatzel.Util.StockUtil.canstocks;

public class StockBuy extends FCommand {
    public StockBuy(){
        name = "stockbuy";
        description = "Allows you to invest in a stock on the stock market";
        category = stocks;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        if (!canstocks){
            event.getChannel().sendMessage("You cannot buy a stock while floatzel is updating the market!").queue();
            return;
        }
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
            id = Integer.valueOf(argsplit[0]);
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
        } else if (!Database.dbcheckifexist(uid)){
            event.getChannel().sendMessage("Error: since you just opened a bank account, you can't buy a stock!").queue();
            return;
        }
        // is there any stock left to buy?
        int units = Database.dbgetunits(id);
        if (units == 0){
            event.getChannel().sendMessage("Sorry fucker, but everyone else already bought all of this stock!").queue();
            return;
        }
        // check if they can afford a stock
        int wallet = Database.dbloadint(uid);
        int price = Database.dbgetprice(id);
        if (wallet < price){
            event.getChannel().sendMessage("Error: you are too poor to afford this stock!").queue();
            return;
        }
        // alright, time to buy the stock itself
        wallet = wallet - price;
        Database.dbsaveint(uid, wallet);
        Database.dbbuystock(uid, id);
        units = units - 1;
        Database.dbupdatestock(id, true, 0, 0, units);
        String name = Database.dbgetname(id);
        event.getChannel().sendMessage("You bought 1 stock in "+name+"!").queue();
        return;
    }
}
