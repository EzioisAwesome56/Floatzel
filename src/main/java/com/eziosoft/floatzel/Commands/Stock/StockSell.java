package com.eziosoft.floatzel.Commands.Stock;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.eziosoft.floatzel.Util.StockUtil.canstocks;

public class StockSell extends FCommand {
    public StockSell(){
        name = "stocksell";
        help = "Sells your stock back to the market";
        category = stocks;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        if (!canstocks){
            event.getChannel().sendMessage("You cannot sell a stock while floatzel is updating the market!").queue();
            return;
        }
        // check if the user even has a stock
        String uid = event.getAuthor().getId();
        boolean exist = Database.dbcheckifstock(uid);
        if (!exist){
            event.getChannel().sendMessage("Error: you do not have a stock to sell!").queue();
            return;
        }
        // then get the stock id
        int id = Database.dbloadstockid(uid);
        // first check to make sure the user put yes on the end
        if (!argsplit[0].equals("yes")){
            event.getChannel().sendMessage("Are you sure you want to sell your stock? run 'stocksell yes' to confirm.").queue();
            return;
        }
        // if the user said yes, start gathering stock information
        int units = Database.dbgetunits(id);
        int price = Database.dbgetprice(id);
        // give the user their new found wealth
        int bal = Database.dbloadint(uid);
        bal = bal + price;
        Database.dbsaveint(uid, bal);
        // then delete their stock table entry
        Database.dbdeletestock(uid);
        // finally, add 1 to the units of the stock
        units = units + 1;
        Database.dbupdatestock(id, true, 0, 0, units);
        event.getChannel().sendMessage("You sold your stock to the stock market and got "+Integer.toString(price)+moneyicon+" from it!").queue();
    }
}
