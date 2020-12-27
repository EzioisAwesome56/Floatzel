package com.eziosoft.floatzel.Commands.Owner;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

public class MakeTable extends FCommand {
    public MakeTable(){
        name = "maketable";
        description = "makes a new table in the database";
        ownerCommand = true;
        category = owner;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        if (argsplit[0].isEmpty()){
            event.reply("You did not provide a name for the new table you FOOL");
            return;
        }
        // otherwise just make the table
        boolean result = Database.dbmaketable(argsplit[0], argsplit[1]);
        event.reply("Did the table making complete? "+ Boolean.toString(result));
    }
}
