package com.eziosoft.floatzel.Music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;
    public final MemeScheduler memeScheduler;
    public final MessageChannel channel;
    public final int status;
    public User host;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager, CommandEvent event, int status) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, event);
        memeScheduler = new MemeScheduler(player, event.getGuild());
        channel = event.getChannel();
        this.status = status;
        switch (status) {
            case 0: player.addListener(scheduler);
                break;
            case 1: player.addListener(memeScheduler);
                break;
        }
    }

    public GuildMusicManager setHost(User host) {
        this.host = host;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public boolean isMusic() {
        return status == 0;
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}
