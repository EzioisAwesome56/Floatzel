package com.eziosoft.floatzel.Commands.Stock;

import com.eziosoft.floatzel.Commands.FCommand;
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
        // nothing
    }
}
