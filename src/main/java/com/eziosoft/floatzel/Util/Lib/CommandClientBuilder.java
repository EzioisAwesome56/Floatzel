package com.eziosoft.floatzel.Util.Lib;

import com.eziosoft.floatzel.Util.Lib.CmdCI;
import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.command.impl.AnnotatedModuleCompilerImpl;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;


public class CommandClientBuilder {
    private Game game = Game.playing("default");
    private OnlineStatus status;
    private String ownerId;
    private String[] coOwnerIds;
    private String prefix;
    private String altprefix;
    private String serverInvite;
    private String success;
    private String warning;
    private String error;
    private String carbonKey;
    private String botsKey;
    private String botsOrgKey;
    private final LinkedList<Command> commands;
    private CommandListener listener;
    private boolean useHelp;
    private Consumer<CommandEvent> helpConsumer;
    private String helpWord;
    private ScheduledExecutorService executor;
    private int linkedCacheSize;
    private AnnotatedModuleCompiler compiler;
    private GuildSettingsManager manager;

    public CommandClientBuilder() {
        this.status = OnlineStatus.ONLINE;
        this.commands = new LinkedList();
        this.useHelp = true;
        this.linkedCacheSize = 0;
        this.compiler = new AnnotatedModuleCompilerImpl();
        this.manager = null;
    }

    public CommandClient build() {
        CommandClient client = new CmdCI(this.ownerId, this.coOwnerIds, this.prefix, this.altprefix, this.game, this.status, this.serverInvite, this.success, this.warning, this.error, this.carbonKey, this.botsKey, this.botsOrgKey, new ArrayList(this.commands), this.useHelp, this.helpConsumer, this.helpWord, this.executor, this.linkedCacheSize, this.compiler, this.manager);
        if (this.listener != null) {
            client.setListener(this.listener);
        }

        return client;
    }

    public CommandClientBuilder setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public CommandClientBuilder setCoOwnerIds(String... coOwnerIds) {
        this.coOwnerIds = coOwnerIds;
        return this;
    }

    public CommandClientBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandClientBuilder setAlternativePrefix(String prefix) {
        this.altprefix = prefix;
        return this;
    }

    public CommandClientBuilder useHelpBuilder(boolean useHelp) {
        this.useHelp = useHelp;
        return this;
    }

    public CommandClientBuilder setHelpConsumer(Consumer<CommandEvent> helpConsumer) {
        this.helpConsumer = helpConsumer;
        return this;
    }

    public CommandClientBuilder setHelpWord(String helpWord) {
        this.helpWord = helpWord;
        return this;
    }

    public CommandClientBuilder setServerInvite(String serverInvite) {
        this.serverInvite = serverInvite;
        return this;
    }

    public CommandClientBuilder setEmojis(String success, String warning, String error) {
        this.success = success;
        this.warning = warning;
        this.error = error;
        return this;
    }

    public CommandClientBuilder setGame(Game game) {
        this.game = game;
        return this;
    }

    public CommandClientBuilder useDefaultGame() {
        this.game = Game.playing("default");
        return this;
    }

    public CommandClientBuilder setStatus(OnlineStatus status) {
        this.status = status;
        return this;
    }

    public CommandClientBuilder addCommand(Command command) {
        this.commands.add(command);
        return this;
    }

    public CommandClientBuilder addCommands(Command... commands) {
        Command[] var2 = commands;
        int var3 = commands.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Command command = var2[var4];
            this.addCommand(command);
        }

        return this;
    }

    public CommandClientBuilder addAnnotatedModule(Object module) {
        this.commands.addAll(this.compiler.compile(module));
        return this;
    }

    public CommandClientBuilder addAnnotatedModules(Object... modules) {
        Object[] var2 = modules;
        int var3 = modules.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object command = var2[var4];
            this.addAnnotatedModule(command);
        }

        return this;
    }

    public CommandClientBuilder setAnnotatedCompiler(AnnotatedModuleCompiler compiler) {
        this.compiler = compiler;
        return this;
    }

    public CommandClientBuilder setCarbonitexKey(String key) {
        this.carbonKey = key;
        return this;
    }

    public CommandClientBuilder setDiscordBotsKey(String key) {
        this.botsKey = key;
        return this;
    }

    public CommandClientBuilder setDiscordBotListKey(String key) {
        this.botsOrgKey = key;
        return this;
    }

    public CommandClientBuilder setListener(CommandListener listener) {
        this.listener = listener;
        return this;
    }

    public CommandClientBuilder setScheduleExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public CommandClientBuilder setLinkedCacheSize(int linkedCacheSize) {
        this.linkedCacheSize = linkedCacheSize;
        return this;
    }

    public CommandClientBuilder setGuildSettingsManager(GuildSettingsManager manager) {
        this.manager = manager;
        return this;
    }
}
