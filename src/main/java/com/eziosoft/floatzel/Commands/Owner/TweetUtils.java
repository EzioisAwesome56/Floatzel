package com.eziosoft.floatzel.Commands.Owner;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Objects.Tweet;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Utils;
import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class TweetUtils extends FCommand {

    Gson gson = new Gson();

    public TweetUtils(){
        name = "tweetutil";
        description = "Utilities for dealing with floatzel's tweets";
        ownerCommand = true;
        category = owner;
        aliases = Utils.makeAlias("tu");
    }

    @Override
    protected void cmdrun(CommandEvent event) throws Exception {
        if (argsplit[0].equals("dump")){
            ArrayList<Tweet> list = new ArrayList<>();
            int total = Database.dbdriver.totalTweets();
            for (int x = 1; x <= total; x++){
                // put every tweet into an array
                list.add(new Tweet(Database.dbdriver.loadTweet(x).getText(), x));
            }
            event.getChannel().sendMessage("tweets dumped!").addFile(gson.toJson(list).getBytes("UTF-8"), "tweets.json").queue();
        } else if (argsplit[0].equals("import")){
            event.reply("do things here");
        } else {
            event.reply("This command requires either `dump` or `import` to be supplied with the command\nimport requires a json file with previously dumped tweets to be attached");
        }
    }
}
