package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.StockUtil;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ViewStocks extends FCommand {
    public ViewStocks(){
        name = "viewstocks";
        help = "View the current stock market pricing";
        category = money;
    }

    @Override
    protected void execute(CommandEvent event){
        // are the stocks open right now?
        if (!StockUtil.canstocks){
            event.getChannel().sendMessage("Error: the stocks are being updated right now. Please try again later").queue();
            return;
        }
    }
}
