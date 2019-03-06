package com.eziosoft.floatzel.Commands.Stock;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.eziosoft.floatzel.Util.StockUtil.canstocks;

public class Stock extends FCommand {
    public Stock(){
        name = "stock";
        help = "view what stock you have purchases";
        category = stocks;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        String uid = event.getAuthor().getId();
        if (!canstocks){
            event.getChannel().sendMessage("Error: you cannot view your stock until the market is done updating").queue();
            return;
        }
        // check if they even have a stock
        if (!Database.dbcheckifstock(uid)){
            event.getChannel().sendMessage("Error: you have no stocks asshole!").queue();
            return;
        }
        StringBuilder builder = new StringBuilder();
        int id = Database.dbloadstockid(uid);
        //with that id, get everything else
        String name = Database.dbgetname(id);
        int diff = Database.dbgetdiff(id);
        int price = Database.dbgetprice(id);
        // then build the message
        builder.append("```md\n#Your stocks\n\n");
        builder.append("name: "+name+"\n");
        builder.append("[price]("+Integer.toString(price)+")\n");
        builder.append("[difference]("+Integer.toString(diff)+")\n```");
        // finally, send the message
        event.getChannel().sendMessage(builder.toString()).queue();
    }
}
