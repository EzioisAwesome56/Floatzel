package com.eziosoft.floatzel.Util.Lib;

import com.jagrosh.jdautilities.command.AnnotatedModuleCompiler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.CommandListener;
import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import com.jagrosh.jdautilities.command.Command.Category;
import com.jagrosh.jdautilities.commons.utils.FixedSizeCache;
import com.jagrosh.jdautilities.commons.utils.SafeIdUtil;
import java.io.IOException;
import java.io.Reader;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.requests.Requester;
import net.dv8tion.jda.core.utils.Checks;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CmdCI implements CommandClient, EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(CommandClient.class);
    private static final int INDEX_LIMIT = 20;
    private static final String DEFAULT_PREFIX = "@mention";
    private final OffsetDateTime start;
    private final Game game;
    private final OnlineStatus status;
    private final String ownerId;
    private final String[] coOwnerIds;
    private final String prefix;
    private final String altprefix;
    private final String serverInvite;
    private final HashMap<String, Integer> commandIndex;
    private final ArrayList<Command> commands;
    private final String success;
    private final String warning;
    private final String error;
    private final String carbonKey;
    private final String botsKey;
    private final String botsOrgKey;
    private final HashMap<String, OffsetDateTime> cooldowns;
    private final HashMap<String, Integer> uses;
    private final FixedSizeCache<Long, Set<Message>> linkMap;
    private final boolean useHelp;
    private final Consumer<CommandEvent> helpConsumer;
    private final String helpWord;
    private final ScheduledExecutorService executor;
    private final AnnotatedModuleCompiler compiler;
    private final GuildSettingsManager manager;
    private String textPrefix;
    private CommandListener listener = null;
    private int totalGuilds;

    public CmdCI(String ownerId, String[] coOwnerIds, String prefix, String altprefix, Game game, OnlineStatus status, String serverInvite, String success, String warning, String error, String carbonKey, String botsKey, String botsOrgKey, ArrayList<Command> commands, boolean useHelp, Consumer<CommandEvent> helpConsumer, String helpWord, ScheduledExecutorService executor, int linkedCacheSize, AnnotatedModuleCompiler compiler, GuildSettingsManager manager) {
        Checks.check(ownerId != null, "Owner ID was set null or not set! Please provide an User ID to register as the owner!");
        if (!SafeIdUtil.checkId(ownerId)) {
            LOG.warn(String.format("The provided Owner ID (%s) was found unsafe! Make sure ID is a non-negative long!", ownerId));
        }

        if (coOwnerIds != null) {
            String[] var22 = coOwnerIds;
            int var23 = coOwnerIds.length;

            for(int var24 = 0; var24 < var23; ++var24) {
                String coOwnerId = var22[var24];
                if (!SafeIdUtil.checkId(coOwnerId)) {
                    LOG.warn(String.format("The provided CoOwner ID (%s) was found unsafe! Make sure ID is a non-negative long!", coOwnerId));
                }
            }
        }

        this.start = OffsetDateTime.now();
        this.ownerId = ownerId;
        this.coOwnerIds = coOwnerIds;
        this.prefix = prefix != null && !prefix.isEmpty() ? prefix : "@mention";
        this.altprefix = altprefix != null && !altprefix.isEmpty() ? altprefix : null;
        this.textPrefix = prefix;
        this.game = game;
        this.status = status;
        this.serverInvite = serverInvite;
        this.success = success == null ? "" : success;
        this.warning = warning == null ? "" : warning;
        this.error = error == null ? "" : error;
        this.carbonKey = carbonKey;
        this.botsKey = botsKey;
        this.botsOrgKey = botsOrgKey;
        this.commandIndex = new HashMap();
        this.commands = new ArrayList();
        this.cooldowns = new HashMap();
        this.uses = new HashMap();
        this.linkMap = linkedCacheSize > 0 ? new FixedSizeCache(linkedCacheSize) : null;
        this.useHelp = useHelp;
        this.helpWord = helpWord == null ? "help" : helpWord;
        this.executor = executor == null ? Executors.newSingleThreadScheduledExecutor() : executor;
        this.compiler = compiler;
        this.manager = manager;
        this.helpConsumer = helpConsumer == null ? (event) -> {
            StringBuilder builder = new StringBuilder("**" + event.getSelfUser().getName() + "** commands:\n");
            Category category = null;
            Iterator var8 = commands.iterator();

            while(true) {
                Command command;
                do {
                    do {
                        if (!var8.hasNext()) {
                            User owner = event.getJDA().getUserById(ownerId);
                            if (owner != null) {
                                builder.append("\n\nFor additional help, contact **").append(owner.getName()).append("**#").append(owner.getDiscriminator());
                                if (serverInvite != null) {
                                    builder.append(" or join ").append(serverInvite);
                                }
                            }

                            if (event.isFromType(ChannelType.TEXT)) {
                                event.reactSuccess();
                            }

                            event.replyInDm(builder.toString(), (unused) -> {
                            }, (t) -> {
                                event.replyWarning("Help cannot be sent because you are blocking Direct Messages.");
                            });
                            return;
                        }

                        command = (Command)var8.next();
                    } while(command.isHidden());
                } while(command.isOwnerCommand() && !event.isOwner());

                if (!Objects.equals(category, command.getCategory())) {
                    category = command.getCategory();
                    builder.append("\n\n  __").append(category == null ? "No Category" : category.getName()).append("__:\n");
                }

                builder.append("\n`").append(this.textPrefix).append(prefix == null ? " " : "").append(command.getName()).append(command.getArguments() == null ? "`" : " " + command.getArguments() + "`").append(" - ").append(command.getHelp());
            }
        } : helpConsumer;
        Iterator var26 = commands.iterator();

        while(var26.hasNext()) {
            Command command = (Command)var26.next();
            this.addCommand(command);
        }

    }

    public void setListener(CommandListener listener) {
        this.listener = listener;
    }

    public CommandListener getListener() {
        return this.listener;
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public OffsetDateTime getStartTime() {
        return this.start;
    }

    public OffsetDateTime getCooldown(String name) {
        return (OffsetDateTime)this.cooldowns.get(name);
    }

    public int getRemainingCooldown(String name) {
        if (this.cooldowns.containsKey(name)) {
            int time = (int)OffsetDateTime.now().until((Temporal)this.cooldowns.get(name), ChronoUnit.SECONDS);
            if (time <= 0) {
                this.cooldowns.remove(name);
                return 0;
            } else {
                return time;
            }
        } else {
            return 0;
        }
    }

    public void applyCooldown(String name, int seconds) {
        this.cooldowns.put(name, OffsetDateTime.now().plusSeconds((long)seconds));
    }

    public void cleanCooldowns() {
        OffsetDateTime now = OffsetDateTime.now();
        List var10000 = (List)this.cooldowns.keySet().stream().filter((str) -> {
            return ((OffsetDateTime)this.cooldowns.get(str)).isBefore(now);
        }).collect(Collectors.toList());
        HashMap var10001 = this.cooldowns;
        this.cooldowns.getClass();
        var10000.forEach(var10001::remove);
    }

    public int getCommandUses(Command command) {
        return this.getCommandUses(command.getName());
    }

    public int getCommandUses(String name) {
        return (Integer)this.uses.getOrDefault(name, 0);
    }

    public void addCommand(Command command) {
        this.addCommand(command, this.commands.size());
    }

    public void addCommand(Command command, int index) {
        if (index <= this.commands.size() && index >= 0) {
            String name = command.getName();
            HashMap var4 = this.commandIndex;
            synchronized(this.commandIndex) {
                if (this.commandIndex.containsKey(name)) {
                    throw new IllegalArgumentException("Command added has a name or alias that has already been indexed: \"" + name + "\"!");
                }

                String[] var5 = command.getAliases();
                int var6 = var5.length;
                int var7 = 0;

                while(true) {
                    if (var7 >= var6) {
                        this.commandIndex.put(name, index);
                        if (index < this.commands.size()) {
                            ((List)this.commandIndex.keySet().stream().filter((key) -> {
                                return (Integer)this.commandIndex.get(key) > index;
                            }).collect(Collectors.toList())).forEach((key) -> {
                                Integer var10000 = (Integer)this.commandIndex.put(key, (Integer)this.commandIndex.get(key) + 1);
                            });
                        }
                        break;
                    }

                    String alias = var5[var7];
                    if (this.commandIndex.containsKey(alias)) {
                        throw new IllegalArgumentException("Command added has a name or alias that has already been indexed: \"" + alias + "\"!");
                    }

                    this.commandIndex.put(alias, index);
                    ++var7;
                }
            }

            this.commands.add(index, command);
        } else {
            throw new ArrayIndexOutOfBoundsException("Index specified is invalid: [" + index + "/" + this.commands.size() + "]");
        }
    }

    public void removeCommand(String name) {
        if (!this.commandIndex.containsKey(name)) {
            throw new IllegalArgumentException("Name provided is not indexed: \"" + name + "\"!");
        } else {
            int targetIndex = (Integer)this.commandIndex.remove(name);
            if (this.commandIndex.containsValue(targetIndex)) {
                List var10000 = (List)this.commandIndex.keySet().stream().filter((key) -> {
                    return (Integer)this.commandIndex.get(key) == targetIndex;
                }).collect(Collectors.toList());
                HashMap var10001 = this.commandIndex;
                this.commandIndex.getClass();
                var10000.forEach(var10001::remove);
            }

            ((List)this.commandIndex.keySet().stream().filter((key) -> {
                return (Integer)this.commandIndex.get(key) > targetIndex;
            }).collect(Collectors.toList())).forEach((key) -> {
                Integer var10000 = (Integer)this.commandIndex.put(key, (Integer)this.commandIndex.get(key) - 1);
            });
            this.commands.remove(targetIndex);
        }
    }

    public void addAnnotatedModule(Object module) {
        this.compiler.compile(module).forEach(this::addCommand);
    }

    public void addAnnotatedModule(Object module, Function<Command, Integer> mapFunction) {
        this.compiler.compile(module).forEach((command) -> {
            this.addCommand(command, (Integer)mapFunction.apply(command));
        });
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public long getOwnerIdLong() {
        return Long.parseLong(this.ownerId);
    }

    public String[] getCoOwnerIds() {
        return this.coOwnerIds;
    }

    public long[] getCoOwnerIdsLong() {
        if (this.coOwnerIds == null) {
            return null;
        } else {
            long[] ids = new long[this.coOwnerIds.length];

            for(int i = 0; i < ids.length; ++i) {
                ids[i] = Long.parseLong(this.coOwnerIds[i]);
            }

            return ids;
        }
    }

    public String getSuccess() {
        return this.success;
    }

    public String getWarning() {
        return this.warning;
    }

    public String getError() {
        return this.error;
    }

    public ScheduledExecutorService getScheduleExecutor() {
        return this.executor;
    }

    public String getServerInvite() {
        return this.serverInvite;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getAltPrefix() {
        return this.altprefix;
    }

    public String getTextualPrefix() {
        return this.textPrefix;
    }

    public int getTotalGuilds() {
        return this.totalGuilds;
    }

    public String getHelpWord() {
        return this.helpWord;
    }

    public boolean usesLinkedDeletion() {
        return this.linkMap != null;
    }

    public <S> S getSettingsFor(Guild guild) {
        return this.manager == null ? null : this.manager.getSettings(guild);
    }

    public <M extends GuildSettingsManager> M getSettingsManager() {
        return this.manager;
    }

    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            this.onMessageReceived((MessageReceivedEvent)event);
        } else if (event instanceof GuildMessageDeleteEvent && this.usesLinkedDeletion()) {
            this.onMessageDelete((GuildMessageDeleteEvent)event);
        } else if (event instanceof GuildJoinEvent) {
            if (((GuildJoinEvent)event).getGuild().getSelfMember().getJoinDate().plusMinutes(10L).isAfter(OffsetDateTime.now())) {
                this.sendStats(event.getJDA());
            }
        } else if (event instanceof GuildLeaveEvent) {
            this.sendStats(event.getJDA());
        } else if (event instanceof ReadyEvent) {
            this.onReady((ReadyEvent)event);
        } else if (event instanceof ShutdownEvent) {
            GuildSettingsManager<?> manager = this.getSettingsManager();
            if (manager != null) {
                manager.shutdown();
            }

            this.executor.shutdown();
        }

    }

    private void onReady(ReadyEvent event) {
        if (!event.getJDA().getSelfUser().isBot()) {
            LOG.error("JDA-Utilities does not support CLIENT accounts.");
            event.getJDA().shutdown();
        } else {
            this.textPrefix = this.prefix.equals("@mention") ? "@" + event.getJDA().getSelfUser().getName() + " " : this.prefix;
            event.getJDA().getPresence().setPresence(this.status == null ? OnlineStatus.ONLINE : this.status, this.game == null ? null : ("default".equals(this.game.getName()) ? Game.playing("Type " + this.textPrefix + this.helpWord) : this.game));
            GuildSettingsManager<?> manager = this.getSettingsManager();
            if (manager != null) {
                manager.init();
            }

            this.sendStats(event.getJDA());
        }
    }

    private void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String[] parts = null;
            String rawContent = event.getMessage().getContentRaw();
            GuildSettingsProvider settings = event.isFromType(ChannelType.TEXT) ? this.provideSettings(event.getGuild()) : null;
            if ((this.prefix.equals("@mention") || this.altprefix != null && this.altprefix.equals("@mention")) && (rawContent.startsWith("<@" + event.getJDA().getSelfUser().getId() + ">") || rawContent.startsWith("<@!" + event.getJDA().getSelfUser().getId() + ">"))) {
                parts = splitOnPrefixLength(rawContent, rawContent.indexOf(">") + 1);
            }

            if (parts == null && rawContent.toLowerCase().startsWith(this.prefix.toLowerCase())) {
                parts = splitOnPrefixLength(rawContent, this.prefix.length());
            }

            if (parts == null && this.altprefix != null && rawContent.toLowerCase().startsWith(this.altprefix.toLowerCase())) {
                parts = splitOnPrefixLength(rawContent, this.altprefix.length());
            }

            if (parts == null && settings != null) {
                Collection<String> prefixes = settings.getPrefixes();
                if (prefixes != null) {
                    Iterator var6 = prefixes.iterator();

                    while(var6.hasNext()) {
                        String prefix = (String)var6.next();
                        if (parts == null && rawContent.toLowerCase().startsWith(prefix.toLowerCase())) {
                            parts = splitOnPrefixLength(rawContent, prefix.length());
                        }
                    }
                }
            }

            if (parts != null) {
                if (this.useHelp && parts[0].equalsIgnoreCase(this.helpWord)) {
                    CommandEvent cevent = new CommandEvent(event, parts[1] == null ? "" : parts[1], this);
                    if (this.listener != null) {
                        this.listener.onCommand(cevent, (Command)null);
                    }

                    this.helpConsumer.accept(cevent);
                    if (this.listener != null) {
                        this.listener.onCompletedCommand(cevent, (Command)null);
                    }

                    return;
                }

                if (event.isFromType(ChannelType.PRIVATE) || event.getTextChannel().canTalk()) {
                    String name = parts[0];
                    String args = parts[1] == null ? "" : parts[1];
                    Command command;
                    if (this.commands.size() < 21) {
                        command = (Command)this.commands.stream().filter((cmd) -> {
                            return cmd.isCommandFor(name);
                        }).findAny().orElse((Object)null);
                    } else {
                        HashMap var8 = this.commandIndex;
                        synchronized(this.commandIndex) {
                            int i = (Integer)this.commandIndex.getOrDefault(name.toLowerCase(), -1);
                            command = i != -1 ? (Command)this.commands.get(i) : null;
                        }
                    }

                    if (command != null) {
                        CommandEvent cevent = new CommandEvent(event, args, this);
                        if (this.listener != null) {
                            this.listener.onCommand(cevent, command);
                        }

                        this.uses.put(command.getName(), (Integer)this.uses.getOrDefault(command.getName(), 0) + 1);
                        command.run(cevent);
                        return;
                    }
                }
            }

            if (this.listener != null) {
                this.listener.onNonCommandMessage(event);
            }

        }
    }

    private void sendStats(JDA jda) {
        OkHttpClient client = ((JDAImpl)jda).getHttpClientBuilder().build();
        Builder builder;
        if (this.carbonKey != null) {
            okhttp3.FormBody.Builder bodyBuilder = (new okhttp3.FormBody.Builder()).add("key", this.carbonKey).add("servercount", Integer.toString(jda.getGuilds().size()));
            if (jda.getShardInfo() != null) {
                bodyBuilder.add("shard_id", Integer.toString(jda.getShardInfo().getShardId())).add("shard_count", Integer.toString(jda.getShardInfo().getShardTotal()));
            }

            builder = (new Builder()).post(bodyBuilder.build()).url("https://www.carbonitex.net/discord/data/botdata.php");
            client.newCall(builder.build()).enqueue(new Callback() {
                public void onResponse(Call call, Response response) {
                    CmdCI.LOG.info("Successfully send information to carbonitex.net");
                    response.close();
                }

                public void onFailure(Call call, IOException e) {
                    CmdCI.LOG.error("Failed to send information to carbonitex.net ", e);
                }
            });
        }

        JSONObject body = (new JSONObject()).put("server_count", jda.getGuilds().size());
        if (jda.getShardInfo() != null) {
            body.put("shard_id", jda.getShardInfo().getShardId()).put("shard_count", jda.getShardInfo().getShardTotal());
        }

        if (this.botsOrgKey != null) {
            builder = (new Builder()).post(RequestBody.create(Requester.MEDIA_TYPE_JSON, body.toString())).url("https://discordbots.org/api/bots/" + jda.getSelfUser().getId() + "/stats").header("Authorization", this.botsOrgKey).header("Content-Type", "application/json");
            client.newCall(builder.build()).enqueue(new Callback() {
                public void onResponse(Call call, Response response) {
                    CmdCI.LOG.info("Successfully send information to discordbots.org");
                    response.close();
                }

                public void onFailure(Call call, IOException e) {
                    CmdCI.LOG.error("Failed to send information to discordbots.org ", e);
                }
            });
        }

        if (this.botsKey != null) {
            builder = (new Builder()).post(RequestBody.create(Requester.MEDIA_TYPE_JSON, body.toString())).url("https://bots.discord.pw/api/bots/" + jda.getSelfUser().getId() + "/stats").header("Authorization", this.botsKey).header("Content-Type", "application/json");
            client.newCall(builder.build()).enqueue(new Callback() {
                public void onResponse(Call call, Response response) {
                    CmdCI.LOG.info("Successfully send information to bots.discord.pw");
                    response.close();
                }

                public void onFailure(Call call, IOException e) {
                    CmdCI.LOG.error("Failed to send information to bots.discord.pw ", e);
                }
            });
            if (jda.getShardInfo() == null) {
                this.totalGuilds = jda.getGuilds().size();
            } else {
                Builder b = (new Builder()).get().url("https://bots.discord.pw/api/bots/" + jda.getSelfUser().getId() + "/stats").header("Authorization", this.botsKey).header("Content-Type", "application/json");
                client.newCall(b.build()).enqueue(new Callback() {
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            Reader reader = response.body().charStream();
                            Throwable var4 = null;

                            try {
                                JSONArray array = (new JSONObject(new JSONTokener(reader))).getJSONArray("stats");
                                int total = 0;

                                for(int i = 0; i < array.length(); ++i) {
                                    total += array.getJSONObject(i).getInt("server_count");
                                }

                                CmdCI.this.totalGuilds = total;
                            } catch (Throwable var22) {
                                var4 = var22;
                                throw var22;
                            } finally {
                                if (reader != null) {
                                    if (var4 != null) {
                                        try {
                                            reader.close();
                                        } catch (Throwable var21) {
                                            var4.addSuppressed(var21);
                                        }
                                    } else {
                                        reader.close();
                                    }
                                }

                            }
                        } finally {
                            response.close();
                        }
                    }

                    public void onFailure(Call call, IOException e) {
                        CmdCI.LOG.error("Failed to retrieve bot shard information from bots.discord.pw ", e);
                    }
                });
            }
        }

    }

    private void onMessageDelete(GuildMessageDeleteEvent event) {
        FixedSizeCache var2 = this.linkMap;
        synchronized(this.linkMap) {
            if (this.linkMap.contains(event.getMessageIdLong())) {
                Set<Message> messages = (Set)this.linkMap.get(event.getMessageIdLong());
                if (messages.size() > 1 && event.getGuild().getSelfMember().hasPermission(event.getChannel(), new Permission[]{Permission.MESSAGE_MANAGE})) {
                    event.getChannel().deleteMessages(messages).queue((unused) -> {
                    }, (ignored) -> {
                    });
                } else if (messages.size() > 0) {
                    messages.forEach((m) -> {
                        m.delete().queue((unused) -> {
                        }, (ignored) -> {
                        });
                    });
                }
            }

        }
    }

    private GuildSettingsProvider provideSettings(Guild guild) {
        Object settings = this.getSettingsFor(guild);
        return settings != null && settings instanceof GuildSettingsProvider ? (GuildSettingsProvider)settings : null;
    }

    private static String[] splitOnPrefixLength(String rawContent, int length) {
        return (String[])Arrays.copyOf(rawContent.substring(length).trim().split("\\s+", 2), 2);
    }

    public void linkIds(long callId, Message message) {
        if (this.usesLinkedDeletion()) {
            FixedSizeCache var4 = this.linkMap;
            synchronized(this.linkMap) {
                Set<Message> stored = (Set)this.linkMap.get(callId);
                if (stored != null) {
                    stored.add(message);
                } else {
                    Set<Message> stored = new HashSet();
                    stored.add(message);
                    this.linkMap.add(callId, stored);
                }

            }
        }
    }
}
