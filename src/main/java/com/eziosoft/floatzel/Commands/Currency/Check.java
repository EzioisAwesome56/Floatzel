package com.eziosoft.floatzel.Commands.Currency;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Check extends FCommand {
    public Check(){
        name = "check";
        description = "check's the bank account of another user";
        category = money;
    }



    @Override
    protected void execute(CommandEvent event) {

    }
}
