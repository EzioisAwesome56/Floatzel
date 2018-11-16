package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.StockUtil;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ViewStocks extends FCommand {
    public ViewStocks(){
        name = "viewstocks";
        help = "View the current stock market pricing";
        category = stocks;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String list = "```md\n#Floatzel Stock Market\n\n";
        StringBuilder builder = new StringBuilder();
        // are the stocks open right now?
        if (!StockUtil.canstocks){
            event.getChannel().sendMessage("Error: the stocks are being updated right now. Please try again later").queue();
            return;
        }
        // load the total amount of stocks
        int total = Database.dbgetcount();
        for (int x = 1; x <= total; x++) {
            String name = Database.dbgetname(x);
            int price = Database.dbgetprice(x);
            // impliment this later lol
            int diff = Database.dbgetdiff(x);
            // also add this at alater point in time
            int unit = Database.dbgetunits(x);
            // form the list
            builder.append("[" + name + "](Stock id: " + Integer.toString(x) + ")\n[Cost](" + Integer.toString(price) + ")\n[Difference](" + Integer.toString(diff) + ")\n[Units](" + Integer.toString(unit) + ")\n\n");
        }
        list = list + builder.toString() + "```";
        event.getChannel().sendMessage(list).queue();
        return;
    }
}
