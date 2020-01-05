package com.eziosoft.floatzel.Commands.Other;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class SetAss extends FCommand {
    public SetAss(){
        name = "jerkmode";
        help = "change if floatzel is an asshole to everyone or not";
        category = other;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        // if the first arg is "check", display currently set option
        if (argsplit[0].equals("check")){
            event.reply("Jerk mode is currently set to: "+ Database.dbcheckifass(event.getGuild().getId()));
            return;
        }
        // first check to see if the person has atleast manage message perms
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
            event.reply("Error: You do not have the required permission to change this option!\n" +
                    "This command requires the `Manage Messages` permission on your role/user!");
            return;
        }
        // we don't check to make sure they even have an entry because to get here youd have to run past Fcommand's check
        // so we just go right to checking if the input is valid
        if (argsplit[0].isEmpty()){
            event.reply("You didn't provide what option you wish to set this mode too!");
            return;
        }
        // then check to make sure it wouldnt explode a boolean
        boolean dank = Boolean.valueOf(argsplit[0]);
        event.reply("note: setting it to anything besides `true` will set this option to false!");
        // set the option in the database
        Database.dbsetass(event.getGuild().getId(), argsplit[0]);
    }
}
