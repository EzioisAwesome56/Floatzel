package com.eziosoft.floatzel;
import com.eziosoft.floatzel.Commands.Asshole.Hello;
import com.eziosoft.floatzel.Commands.Asshole.SwearCombine;
import com.eziosoft.floatzel.Commands.Currency.*;
import com.eziosoft.floatzel.Commands.Debug.Debug;
import com.eziosoft.floatzel.Commands.Entertainment.*;
import com.eziosoft.floatzel.Commands.Image.*;
import com.eziosoft.floatzel.Commands.Other.*;
import com.eziosoft.floatzel.Commands.Owner.Eval;
import com.eziosoft.floatzel.Commands.PayForCommands.BetterLoan;
import com.eziosoft.floatzel.Commands.PayForCommands.Tag;
import com.eziosoft.floatzel.Commands.Secret.Gay;
import com.eziosoft.floatzel.Commands.Smm.Course;
import com.eziosoft.floatzel.Commands.Smm.RandCourse;
import com.eziosoft.floatzel.Commands.Sound.*;
import com.eziosoft.floatzel.Commands.Spam.Spam;
import com.eziosoft.floatzel.Commands.Spam.Think;
import com.eziosoft.floatzel.Commands.Stock.Stock;
import com.eziosoft.floatzel.Commands.Stock.StockBuy;
import com.eziosoft.floatzel.Commands.Stock.StockSell;
import com.eziosoft.floatzel.Commands.Test.TestCommand;
import com.eziosoft.floatzel.Commands.admin.*;
import com.eziosoft.floatzel.Listeners.MiscListener;
import com.eziosoft.floatzel.Music.Player;
import com.eziosoft.floatzel.Slack.Slack;
import com.eziosoft.floatzel.Util.TwitterManager;
import com.eziosoft.floatzel.Util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;


public class Floatzel {
    // json configuration file
    public static JsonConfig conf = new JsonConfig();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static boolean isdev = false;
    // version shit
    public static String version;

    // stolen from kekbot: some form of event waiter?
    public static final EventWaiter waiter = new EventWaiter(Executors.newSingleThreadScheduledExecutor(), false);

    public static ShardManager jda;
    public static CommandClient commandClient;
    public static MiscListener listener = new MiscListener();
    public static TwitterManager twitterManager;
    public static Player musicPlayer = new Player();

    //thing for the tweet bot
    public static boolean tweeton = false;
    public static boolean fail = false;

    public static void main(String[] args) throws LoginException {
        System.out.println("Floatzel is now starting up...");
        //Checking if shit is dev (which is set by adding --dev to java args, if so, do extra shit.
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--dev")) {
                isdev = true;
                System.out.println("RUNNING IN DEVELOPMENT MODE!");
            }
        }

        // does the configuration file exist??!!?!??!?!!
        if (!Utils.configExist()){
            System.out.println("Floatzel was unable to find configuration data!");
            // we assume it doesnt exist and generate fresh config data
            Utils.makeConfig(gson);
            // then just exit the software!
            System.exit(0);
        } else {
            // it exists, load it!
            boolean a = Utils.loadConfig(gson);
            if (!a){
                System.out.println("there was a error loading config. Floatzel shutting down...");
                System.exit(1);
            } else {
                System.out.println("Configuration data loaded!");
            }

        }
        // will this work?
        twitterManager = new TwitterManager();

        // resume everything else
        version = !isdev ? "2.5.1 HOTFIX 1" : "2.x Developement";
         commandClient = new CommandClientBuilder().setOwnerId(conf.getOwnerid()).useHelpBuilder(false).setPrefix(!isdev ? conf.getPrefix() : conf.getDevprefix()).build();


        // change this depending on what token you wanna use
        commandClient.addCommand(new TestCommand());
        commandClient.addCommand(new KYS());
        commandClient.addCommand(new Pi());
        commandClient.addCommand(new Boyfriend());
        commandClient.addCommand(new Girlfriend());
        commandClient.addCommand(new Think());
        commandClient.addCommand(new Spam());
        commandClient.addCommand(new Eat());
        commandClient.addCommand(new ImageTest());
        commandClient.addCommand(new Invite());
        commandClient.addCommand(new SoundTest());
        commandClient.addCommand(new Shit8ball());
        commandClient.addCommand(new Hello());
        commandClient.addCommand(new Stats());
        commandClient.addCommand(new Reverse());
        commandClient.addCommand(new Servers());
        commandClient.addCommand(new ServerInfo());
        commandClient.addCommand(new SwearCombine());
        commandClient.addCommand(new Stop());
        commandClient.addCommand(new Play());
        commandClient.addCommand(new Repeat());
        commandClient.addCommand(new Sax());
        commandClient.addCommand(new Starman());
        commandClient.addCommand(new Bal());
        commandClient.addCommand(new Inflate());
        commandClient.addCommand(new Gamble());
        commandClient.addCommand(new Pay());
        commandClient.addCommand(new Loan());
        commandClient.addCommand(new BetterLoan());
        commandClient.addCommand(new BuyCmd());
        commandClient.addCommand(new Mitsuru());
        commandClient.addCommand(new Tweet());
        commandClient.addCommand(new Check());
        commandClient.addCommand(new ViewStocks());
        commandClient.addCommand(new Force());
        commandClient.addCommand(new Debug());
        commandClient.addCommand(new AddTweet());
        commandClient.addCommand(new Eval());
        commandClient.addCommand(new Tag());
        commandClient.addCommand(new StockBuy());
        commandClient.addCommand(new Stock());
        commandClient.addCommand(new StockSell());
        commandClient.addCommand(new Floof());
        commandClient.addCommand(new Course());
        commandClient.addCommand(new RandCourse());
        commandClient.addCommand(new Resize());
        commandClient.addCommand(new Cow());
        commandClient.addCommand(new Explode());
        commandClient.addCommand(new Implode());
        commandClient.addCommand(new Wall());
        commandClient.addCommand(new RunPlugin());
        commandClient.addCommand(new Random());
        //commandClient.addCommand(new LootBox());
        commandClient.addCommand(new Yukari());
        // PUT THIS LAST OR ELSE HELP BREAKS
        commandClient.addCommand(new Help());
        // secret commands that are not in help for a reason
        commandClient.addCommand(new Gay());


        
        jda = new DefaultShardManagerBuilder().setToken(!isdev ? conf.getToken() : conf.getDevtoken())
                .addEventListeners(listener, commandClient, musicPlayer, waiter)
                .setShardsTotal(2).build();

        //TwitterManager is now a listener too, which'll do all the work onReady by itself instead of relying on MiscListener
        if (!isdev && Floatzel.conf.getTwitterTog()) Floatzel.jda.addEventListener(twitterManager);
    }
}
