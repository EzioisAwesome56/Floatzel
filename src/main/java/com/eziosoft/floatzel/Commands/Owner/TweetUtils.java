package com.eziosoft.floatzel.Commands.Owner;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Objects.Tweet;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Utils;
import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
            wrapper wrap = new wrapper();
            int total = Database.dbdriver.totalTweets();
            for (int x = 1; x < total; x++){
                // put every tweet into an array
                wrap.getContent().add(Database.dbdriver.loadTweet(x).getText());
            }
            event.getChannel().sendMessage("tweets dumped!").addFile(gson.toJson(wrap).getBytes("UTF-8"), "tweets.json").queue();
        } else if (argsplit[0].equals("import")){
            if (event.getMessage().getAttachments().size() < 1){
                event.reply("Error: please provide json to import!");
                return;
            }
            // get stream
            InputStream stream = event.getMessage().getAttachments().get(0).retrieveInputStream().join();
            String json = IOUtils.toString(stream, StandardCharsets.UTF_8);
            stream.close();
            event.reply("Now importing data...");
            wrapper wrap = gson.fromJson(json, wrapper.class);
            System.out.println("Importing tweets...");
            int current = Database.dbdriver.totalTweets();
            int total = 0;
            for (String s : wrap.getContent()){
                Database.dbdriver.saveTweet(new Tweet(s, current));
                current++;
                total++;
            }
            event.reply("Imported "+ total + " tweets");
        } else {
            event.reply("This command requires either `dump` or `import` to be supplied with the command\nimport requires a json file with previously dumped tweets to be attached");
        }
    }


    static class wrapper{
        private List<String> content;

        public List<String> getContent() {
            return content;
        }

        public wrapper(){
            this.content = new ArrayList<>();
        }

        public void AddTweet(String s){
            this.content.add(s);
        }
    }
}
