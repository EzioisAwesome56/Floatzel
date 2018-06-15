package com.eziosoft.floatzel.Music;

import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<Pair<AudioTrack, User>> queue;
    private final Guild guild;
    private final TextChannel channel;
    public User currentUser;
    public int repeat = 0;
    private final List<Pair<AudioTrack, User>> repeatQueue = new ArrayList<>();
    private int currentRepeatTrack = 0;
    private boolean started = false;

    /**
     * @param player The audio player the scheduler manages.
     * @param event The CommandEvent containing guild and channel info.
     */
    public TrackScheduler(AudioPlayer player, CommandEvent event) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.guild = event.getGuild();
        this.channel = event.getTextChannel();
    }

    private Pair<AudioTrack, User> getCurrentRepeatTrack() {
        return repeatQueue.get(currentRepeatTrack);
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track, User user) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            if (repeat != 2) {
                queue.offer(Pair.of(track, user));
            } else {
                repeatQueue.add(Pair.of(track, user));
            }
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    private void nextTrack() {
        if (repeat != 2) {
            channel.sendMessage("Now playing: `" + queue.element().getLeft().getInfo().title
                    + "` (Queued by: " + queue.element().getRight().getName() + ")").queue();
            Pair<AudioTrack, User> pair = queue.poll();
            AudioTrack track = pair.getLeft();
            this.currentUser = pair.getRight();
            player.startTrack(track, false);
        } else {
            if (currentRepeatTrack < repeatQueue.size() - 1) ++currentRepeatTrack;
            else currentRepeatTrack = 0;
            player.startTrack(getCurrentRepeatTrack().getLeft().makeClone(), false);
            channel.sendMessage("Now playing: `" + getCurrentRepeatTrack().getLeft().getInfo().title
                    + "` (Queued by: " + getCurrentRepeatTrack().getRight().getName() + ")").queue();
        }
    }

    public void toggleRepeat() {
        switch (repeat) {
            case 0:
                ++repeat;
                break;
            case 1:
                ++repeat;
                repeatQueue.add(Pair.of(player.getPlayingTrack().makeClone(), currentUser));
                queue.drainTo(repeatQueue);
                break;
            case 2:
                repeat = 0;
                for (int i = currentRepeatTrack + 1; i < repeatQueue.size(); i++) {
                    queue.offer(repeatQueue.get(i));
                }
                repeatQueue.clear();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            if (repeat == 1) player.startTrack(track.makeClone(), false);
            else if (queue.size() > 0 || repeat == 2) nextTrack();
            else closeConnection();
        }
    }

    private void closeConnection() {
        new Thread(() -> Floatzel.musicPlayer.closeConnection(guild)).start();
    }

    public boolean hasStarted() {
        return started;
    }

    public void setStarted() {
        started = true;
    }

    public BlockingQueue<Pair<AudioTrack, User>> getQueue() {
        return queue;
    }

    public int getQueueSize() {
        if (queue.size() == 0) return repeatQueue.size();
        else return queue.size();
    }

    public List<Pair<AudioTrack, User>> getRepeatQueue() {
        return repeatQueue;
    }

    public int getRepeat() {
        return currentRepeatTrack;
    }
}
