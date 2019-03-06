package com.eziosoft.floatzel.Commands.Debug;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Database;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Debug extends FCommand {
    public Debug(){
        name = "counttweet";
        help = "Counts total number of tweets in the bot";
        category = owner;
        ownerCommand = true;
    }

    @Override
    protected void cmdrun(CommandEvent event) throws DatabaseException {
        int a = Database.dbcounttweets();
        event.getChannel().sendMessage(Integer.toString(a)).queue();
    }

}
