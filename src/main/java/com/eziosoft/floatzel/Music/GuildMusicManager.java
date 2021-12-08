package com.eziosoft.floatzel.Music;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Objects.MusicPlayerIDs;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
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
    public final String msgchannel;
    public final int status;
    public String hostid;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     * @param mpid object with all the ids in it
     */
    public GuildMusicManager(AudioPlayerManager manager, MusicPlayerIDs mpid, int status) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, mpid);
        memeScheduler = new MemeScheduler(player, Long.toString(mpid.getGuildid()));
        msgchannel = mpid.getMessagechannelid();
        this.status = status;
        switch (status) {
            case 0: player.addListener(scheduler);
                break;
            case 1: player.addListener(memeScheduler);
                break;
        }
    }

    public GuildMusicManager setHost(String host) {
        this.hostid = host;
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

    public TextChannel getChannel(){
        return Floatzel.jda.getTextChannelById(msgchannel);
    }
}
