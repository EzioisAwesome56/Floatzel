package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BuyCmd extends FCommand {
    public BuyCmd(){
        name = "buycmd";
        description = "purchase access to commands";
        category = money;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        String args = event.getArgs();
        String uid = event.getAuthor().getId();
        int bal = Database.dbloadint(uid);
        if (args.length() < 1){
            event.getChannel().sendMessage("You didnt tell me what command you wanna buy!").queue();
            return;
        }
        if (args.equals("list")){
            // make this list better later dumbfuck
            // no, i dont think i will past-self
            event.getChannel().sendMessage("```md\n#Commands for sale\n[betterloan](gives you better loans)\n#Price: 850 ```").queue();
            return;
        }
        if (args.equals("betterloan")){
            // check if they have the money to afford it
            if (bal < 850){
                event.getChannel().sendMessage("You dont have enough money to buy this command!").queue();
                return;
            }
            if (Database.dbcheckbloan(uid)){
                event.reply("You already bought this command, no need to buy it again");
                return;
            }
            // subtract the amount of money from the user's bank account and then save it
            bal = bal - 850;
            Database.dbsave(uid, Integer.toString(bal));
            // write te fact the user has the command now
            Database.dbbuycmd(1, uid);
            event.getChannel().sendMessage("You have purchased betterloan!").queue();
            return;
        }
    }
}
