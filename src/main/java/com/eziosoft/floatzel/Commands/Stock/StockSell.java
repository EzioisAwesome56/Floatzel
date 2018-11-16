package com.eziosoft.floatzel.Commands.Stock;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class StockSell extends FCommand {
    public StockSell(){
        name = "stocksell";
        help = "Sells your stock back to the market";
        category = stocks;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    }
}
