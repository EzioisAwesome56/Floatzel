package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Database;
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
        String list = "```md\n#Floatzel Stock Market\n\n";
        StringBuilder builder = new StringBuilder();
        // are the stocks open right now?
        if (!StockUtil.canstocks){
            event.getChannel().sendMessage("Error: the stocks are being updated right now. Please try again later").queue();
            return;
        }
        // load the total amount of stocks
        int total = Database.dbgetcount();
        String name = Database.dbgetname(1);
        int price = Database.dbgetprice(1);
        // impliment this later lol
        int diff = Database.dbgetdiff(1);
        // also add this at alater point in time
        int unit = Database.dbgetunits(1);
        // form the list
        builder.append("["+name+"]\n[Cost]("+Integer.toString(price)+")\n[Difference]("+Integer.toString(diff)+")\n[Units]("+Integer.toString(unit)+")\n\n");
        list = list + builder.toString() + "```";
        event.getChannel().sendMessage(list).queue();
        return;
    }
}
