package com.eziosoft.floatzel.kekbot.Commands;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.kekbot.Games.BaseGame;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

// glue imports
import com.eziosoft.floatzel.kekbot.KekGlue.KekBot;
import com.eziosoft.floatzel.kekbot.KekGlue.LocaleUtils;
import com.eziosoft.floatzel.kekbot.KekGlue.Profile;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

public class GameCommand extends FCommand {

    public GameCommand() {
        name = "game";
        description = "Central command for all the game related commands";
        category = fun;
    }

    private static String getGameStatus(BaseGame game, String locale) {
        final String ready = LocaleUtils.getString("command.fun.game.readystatus", locale);
        final String morePlayers = LocaleUtils.getString("command.fun.game.awaitingstatus", locale);
        if (game.hasMinimum()) {
            if (game.hasMinimumPlayers()) return ready;
            else return morePlayers;
        } else {
            if (game.hasRoomForPlayers()) {
                if (game.hasAI()) return ready;
                else return morePlayers;
            } else return ready;
        }
    }

    @Override
    protected void cmdrun(CommandEvent event) {
        TextChannel channel = event.getTextChannel();
        if (argsplit.length >= 1) {
            switch (argsplit[0].toLowerCase()) {
                case "create":
                    if (argsplit.length >= 2) {
                        String game = argsplit[1];
                        KekBot.gamesManager.addGame(channel, game.toLowerCase(), event.getAuthor());
                    }
                    break;
                case "start":
                case "ready":
                    if (KekBot.gamesManager.doesUserHaveGame(channel, event.getAuthor())) {
                        BaseGame game = KekBot.gamesManager.getGame(channel);
                        if (!game.players.get(0).equals(event.getAuthor())) {
                            channel.sendMessage(LocaleUtils.getString("command.fun.game.start.error", game.players.get(0).getName())).queue();
                            return;
                        }
                        if (!game.isReady()) game.ready();
                        else channel.sendMessage(LocaleUtils.getString("command.fun.game.start.alreadystarted")).queue();
                    }
                    break;
                case "rules":
                    if (!KekBot.gamesManager.isChannelFree(channel)) {
                        BaseGame game = KekBot.gamesManager.getGame(channel);
                        channel.sendMessage(game.getRules()).queue();
                    } else channel.sendMessage(LocaleUtils.getString("command.fun.game.nolobby")).queue();
                    break;
                case "lobby":
                    if (!KekBot.gamesManager.isChannelFree(channel)) {
                        BaseGame game = KekBot.gamesManager.getGame(channel);
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("Current Game:")
                                .addBlankField(false)
                                .addField(LocaleUtils.getString("command.fun.game.lobby.currentgame"), game.getGameName(), false);
                        if (game.hasMinimum()) embed.addField(LocaleUtils.getString("command.fun.game.lobby.minplayers"), String.valueOf(game.getMinNumberOfPlayers()), true);
                        embed.addField(LocaleUtils.getString("command.fun.game.lobby.maxplayers"), String.valueOf(game.getMaxNumberOfPlayers()), true)
                                .addField(LocaleUtils.getString("command.fun.game.lobby.numplayers"), String.valueOf(game.players.size()), true)
                                .addField(LocaleUtils.getString("command.fun.game.lobby.status"), getGameStatus(game, "e"), false)
                                .addField(LocaleUtils.getString("command.fun.game.lobby.players"), StringUtils.join((Iterable<?>) game.players.stream().map(user -> game.getPlayerNumber(user) + ". " + user.getName()).collect(Collectors.toList()), "\n"), false);

                        channel.sendMessage(embed.build()).queue();
                    }
                    break;
                case "join":
                    if (!KekBot.gamesManager.isChannelFree(channel)) {
                        BaseGame game = KekBot.gamesManager.getGame(channel);
                        if (!game.isReady()) {
                            if (game.players.contains(event.getAuthor())){
                                channel.sendMessage(LocaleUtils.getString("command.fun.game.join.existing")).queue();
                            } else {
                                KekBot.gamesManager.joinGame(channel, event.getAuthor());
                            }
                        } else channel.sendMessage(LocaleUtils.getString("command.fun.game.join.started")).queue();
                    } else channel.sendMessage(LocaleUtils.getString("command.fun.game.nolobby")).queue();
                    break;
                case "quit":
                    if (!KekBot.gamesManager.isChannelFree(channel)) {
                        BaseGame game = KekBot.gamesManager.getGame(channel);
                        if (game.isReady()) {
                            if (game.players.contains(event.getAuthor())) {
                                if (!game.canQuit()) {
                                    event.getChannel().sendMessage("You're not allowed to quit this game!").queue();
                                    return;
                                }

                                Profile profile = Profile.getProfile(event.getAuthor());
                                // TODO: impliment this stuff
                                /*profile.spendTopKeks(ThreadLocalRandom.current().nextInt(1, 15));
                                profile.takeKXP(ThreadLocalRandom.current().nextInt(5, 20));
                                profile.save();*/
                                KekBot.gamesManager.killGame(channel);
                                channel.sendMessage(LocaleUtils.getString("command.fun.game.quit.existing", event.getAuthor().getAsMention())).queue();
                            } else channel.sendMessage(LocaleUtils.getString("command.fun.game.quit.existingerror")).queue();
                        } else {
                            if (game.players.contains(event.getAuthor())) {
                                game.removePlayer(event.getAuthor());
                                if (game.players.size() == 0) {
                                    KekBot.gamesManager.closeGame(channel);
                                    channel.sendMessage("**" + LocaleUtils.getString("command.fun.game.quit.cancelled", game.getGameName()) + "**").queue();
                                } else channel.sendMessage("**" + LocaleUtils.getString("command.fun.game.quit.lobby", event.getAuthor().getName(), "(" + game.players.size() + "/" + game.getMaxNumberOfPlayers() + ")") + "**").queue();
                            }
                        }
                    } else channel.sendMessage(LocaleUtils.getString("command.fun.game.nolobby")).queue();
                    break;
                case "cancel":
                    if (!KekBot.gamesManager.isChannelFree(channel)) {
                        BaseGame game = KekBot.gamesManager.getGame(channel);
                        boolean admin = event.getMember().hasPermission(Permission.ADMINISTRATOR);
                        if (game.players.contains(event.getAuthor()) || admin) {
                            if (!game.isReady()) {
                                if (game.players.get(0).equals(event.getAuthor()) || admin) {
                                    KekBot.gamesManager.closeGame(channel);
                                    channel.sendMessage("**" + LocaleUtils.getString("command.fun.game.cancel", game.getGameName()) + "**").queue();
                                } else channel.sendMessage(LocaleUtils.getString("command.fun.game.cancel.error", "`Administrator`")).queue();
                            } else channel.sendMessage(LocaleUtils.getString("command.fun.game.cancel.started", "`" + (Floatzel.isdev ? Floatzel.conf.getDevprefix() : Floatzel.conf.getPrefix()) + "game quit`")).queue();
                        }
                    } else channel.sendMessage(LocaleUtils.getString("command.fun.game.nolobby")).queue();
                    break;
            }
        } else {
            channel.sendMessage(LocaleUtils.getString("command.noargs", "`" + (Floatzel.isdev ? Floatzel.conf.getDevprefix() : Floatzel.conf.getPrefix()) + "help game`")).queue();
        }
    }
}
