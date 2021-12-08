package com.eziosoft.floatzel;

import com.eziosoft.floatzel.Commands.Currency.*;
import com.eziosoft.floatzel.Commands.Debug.Debug;
import com.eziosoft.floatzel.Commands.Entertainment.*;
import com.eziosoft.floatzel.Commands.Image.*;
import com.eziosoft.floatzel.Commands.Other.*;
import com.eziosoft.floatzel.Commands.Owner.MakeTable;
import com.eziosoft.floatzel.Commands.Owner.TweetUtils;
import com.eziosoft.floatzel.Commands.PayForCommands.BetterLoan;
import com.eziosoft.floatzel.Commands.Sound.*;
import com.eziosoft.floatzel.Commands.Spam.Spam;
import com.eziosoft.floatzel.Commands.Spam.Think;
import com.eziosoft.floatzel.Commands.Stock.Stock;
import com.eziosoft.floatzel.Commands.Stock.StockBuy;
import com.eziosoft.floatzel.Commands.Stock.StockSell;
import com.eziosoft.floatzel.Commands.admin.*;
import com.eziosoft.floatzel.Listeners.MiscListener;
import com.eziosoft.floatzel.Music.Player;
import com.eziosoft.floatzel.Objects.EmojiManager;
import com.eziosoft.floatzel.Objects.GuildSettingsManager;
import com.eziosoft.floatzel.Objects.ModLoader;
import com.eziosoft.floatzel.Objects.gameTexts;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableImageCommand;
import com.eziosoft.floatzel.SlashCommands.Globals.GManage;
import com.eziosoft.floatzel.SlashCommands.Local.*;
import com.eziosoft.floatzel.SlashCommands.SlashCommandManager;
import com.eziosoft.floatzel.Util.TwitterManager;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.floatzel.kekbot.Commands.GameCommand;
import com.eziosoft.floatzel.kekbot.KekGlue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;


public class Floatzel {
    // json configuration file
    public static JsonConfig conf = new JsonConfig();
    public static gameTexts gameTexts = new gameTexts();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static EmojiManager emojiManager = new EmojiManager();
    public static GuildSettingsManager guildSettingsManager = new GuildSettingsManager();

    public static boolean joke = false;
    public static String jokename = "Cirno";
    public static String normalname = "Floatzel";


    public static boolean isdev = false;
    public static boolean fallback = false;
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

    // slash commands
    public static SlashCommandManager scm = new SlashCommandManager();

    // new in 3.0: mod loader
    public static ModLoader loader;

    public static void main(String[] args) throws LoginException {
        System.out.println("Floatzel is now starting up...");
        //Checking if shit is dev (which is set by adding --dev to java args, if so, do extra shit.
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--dev")) {
                isdev = true;
                System.out.println("RUNNING IN DEVELOPMENT MODE!");
            }
            if (arg.equalsIgnoreCase("--fallback")){
                fallback = true;
                System.out.println("Running with fallback database");
            }
        }

        // does the configuration file exist??!!?!??!?!!
        if (!Utils.configExist()){
            System.err.println("Floatzel was unable to find configuration data!");
            // we assume it doesnt exist and generate fresh config data
            Utils.makeConfig(gson);
            // then just exit the software!
            System.exit(0);
        } else {
            // it exists, load it!
            boolean a = Utils.loadConfig(gson);
            if (!a){
                System.err.println("there was a error loading config. Floatzel shutting down...");
                System.exit(1);
            } else {
                System.out.println("Configuration data loaded!");
            }

        }

        // load the game texts
        if (!Utils.gameTextExist()){
            System.err.println("Error: games.json does not exist! Creating a blank one and saving it");
            Utils.makeGames(gson);
            System.err.println("Done! bot will operate normally but without any cool games. consider editing games.json to add your own!");
        } else {
            if (!Utils.loadGames(gson)){
                System.err.println("Error loading the games.json file!");
                System.exit(-1);
            } else {
                System.out.println("Games loaded!");
            }
        }

        // init the emoji manager
        emojiManager.initilize();

        // start up the mod loader
        try {
            loader = new ModLoader("mods");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Floatzel has encountered an error trying to startup! now shutting down...");
            System.exit(-1);
        }


        // will this work?
        twitterManager = new TwitterManager();

        // resume everything else
        version = !isdev ? "2.7" : "2.x Development";
         commandClient = new CommandClientBuilder().setPrefixFunction(event -> guildSettingsManager.getPrefix(event.getGuild().getId())).setOwnerId(conf.getOwnerid()).useHelpBuilder(false).build();


        // change this depending on what token you wanna use
        commandClient.addCommand(new Shutdown());
        dualRegister(new Pi());
        dualRegister(new Boyfriend());
        dualRegister(new Girlfriend());
        commandClient.addCommand(new Think());
        commandClient.addCommand(new Spam());
        dualRegister(new Eat());
        commandClient.addCommand(new ImageTest());
        dualRegister(new Invite());
        commandClient.addCommand(new SoundTest());
        dualRegister(new Shit8ball());
        dualRegister(new Stats());
        dualRegister(new Reverse());
        commandClient.addCommand(new Servers());
        commandClient.addCommand(new ServerInfo());
        dualRegister(new Stop());
        dualRegister(new Play());
        dualRegister(new Repeat());
        commandClient.addCommand(new Sax());
        commandClient.addCommand(new Starman());
        commandClient.addCommand(new Bal());
        commandClient.addCommand(new Inflate());
        commandClient.addCommand(new Gamble());
        commandClient.addCommand(new Pay());
        commandClient.addCommand(new Loan());
        commandClient.addCommand(new BetterLoan());
        commandClient.addCommand(new BuyCmd());
        commandClient.addCommand(new Tweet());
        commandClient.addCommand(new Check());
        commandClient.addCommand(new ViewStocks());
        commandClient.addCommand(new Force());
        commandClient.addCommand(new Debug());
        commandClient.addCommand(new AddTweet());
        commandClient.addCommand(new StockBuy());
        commandClient.addCommand(new Stock());
        commandClient.addCommand(new StockSell());
        dualRegisterImage(new Jpeg());
        dualRegister(new Cow());
        dualRegisterImage(new Explode());
        dualRegisterImage(new Implode());
        dualRegisterImage(new Wall());
        commandClient.addCommand(new RunPlugin());
        dualRegister(new Random());
        dualRegister(new Ping());
        dualRegisterImage(new Shrink());
        dualRegisterImage(new Pixel());
        commandClient.addCommand(new LootBox());
        commandClient.addCommand(new Help());
        dualRegisterImage(new Expand());
        dualRegisterImage(new Small());
        dualRegisterImage(new Swirl());
        commandClient.addCommand(new MakeTable());
        dualRegister(new Avatar());
        dualRegister(new DiceRoll());
        commandClient.addCommand(new TweetUtils());
        commandClient.addCommand(new GameCommand());

        // load registerable slash commands
        scm.addRegisterable("other", new OtherPorts());
        scm.addRegisterable("fun", new FunPorts());
        scm.addRegisterable("prefix", new prefix());
        scm.addRegisterable("debug", new debug());
        scm.addRegisterable("image", new ImagePorts());
        scm.addRegisterable("audio", new Audio());

        // load rest of mods here
        try{
            loader.loadAll();
        } catch (Exception e){
            System.err.println("Floatzel modloader has encountered an error during mod loading!");
            e.printStackTrace();
            System.exit(-1);
        }

        // prepare words for kekglue
        KekGlue.initWords();

        // add the one global cmd we have to the global list
        if (!isdev) scm.addGlobalCmd("gmanage", new GManage());


        jda = DefaultShardManagerBuilder.createDefault(!isdev ? conf.getToken() : conf.getDevtoken())
                .addEventListeners(listener, commandClient, musicPlayer, waiter, scm, KekGlue.KekBot.gamesManager)
                .setShardsTotal(2)
                .build();


        //TwitterManager is now a listener too, which'll do all the work onReady by itself instead of relying on MiscListener
        if (!isdev && Floatzel.conf.getTwitterTog()) Floatzel.jda.addEventListener(twitterManager);
    }

    public static void dualRegister(FSlashableCommand c){
        commandClient.addCommand(c);
        scm.addSlashableAction(c.getName(), c);
    }

    public static void dualRegisterImage(FSlashableImageCommand c){
        commandClient.addCommand(c);
        scm.addSlashableImageAction(c.getName(), c);
    }
}
