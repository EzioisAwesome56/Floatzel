package com.eziosoft.floatzel.Music;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Objects.MusicPlayerIDs;
import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.floatzel.LibraryHacks.LavaPlayer.AudioSourceHack;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Player extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public Player() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceHack.registerRemoteSources(playerManager);
        AudioSourceHack.registerLocalSource(playerManager);
    }

    public User getHost(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        return Floatzel.jda.getUserById(musicManagers.get(guildId).hostid);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(MusicPlayerIDs mpid, int status) {
        GuildMusicManager musicManager = musicManagers.get(mpid.getGuildid());

        if (musicManager == null || (musicManager.getStatus() == 2 && status < 2)) {
            musicManager = new GuildMusicManager(playerManager, mpid, status).setHost(mpid.getUserid());
            mpid.getGuild().getAudioManager().setSendingHandler(musicManager.getSendHandler());
            musicManagers.put(mpid.getGuildid(), musicManager);
        }

        return musicManager;
    }

    public int getActivePlayerCount() {
        return musicManagers.size();
    }

    public void changeHost(Guild guild, User user) {
        long guildId = Long.parseLong(guild.getId());
        musicManagers.get(guildId).setHost(user.getId());
    }

    /** meme logic and shim functions **/
    public void loadAndMeme(final CommandEvent event, final String trackUrl) {
        memeLogic(new MusicPlayerIDs(event), trackUrl);
    }
    // TODO: write slash command meme player.
    private void memeLogic(MusicPlayerIDs mpid, String trackUrl){
        GuildMusicManager musicManager = getGuildAudioPlayer(mpid, 1);
        if (musicManager.getStatus() < 1) {
            mpid.getChannel().sendMessage("I can't meme while music's playing...").queue();
            return;
        }
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                playMeme(mpid, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                playMeme(mpid, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    /** Regular load functions; for loading urls, main logic and shim functions **/
    public void loadAndPlaySlash(final SlashCommandEvent event, final String trackUrl){
        loadAndPlayLogic(new MusicPlayerIDs(event), trackUrl);
    }
    public void loadAndPlay(final CommandEvent event, final String trackUrl) {
        loadAndPlayLogic(new MusicPlayerIDs(event), trackUrl);
    }
    private void loadAndPlayLogic(MusicPlayerIDs mpid, String trackUrl){
        GuildMusicManager musicManager = getGuildAudioPlayer(mpid, 0);
        if (musicManager.getStatus() == 1) {
            mpid.getTextChannel().sendMessage("Cant play music while playing memes. sorry!").queue();
            return;
        }
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                queueTrack(mpid, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                int failed = 0;

                for (AudioTrack track : playlist.getTracks()) {
                    if (track != null) {
                        play(mpid, musicManager, track);
                    } else failed++;
                }

                mpid.getChannel().sendMessage(mpid.getUser().getName() + " added " + (playlist.getTracks().size() - failed) + " tracks to the queue." + (failed > 0 ? " (" + failed + " track(s) could not be added.)" : "")).queue();
            }

            @Override
            public void noMatches() {
                mpid.getChannel().sendMessage("`" + trackUrl + "` isn't a URL, sadly.").queue();
                if (musicManager.player.getPlayingTrack() == null) killConnection(mpid.getGuild());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                mpid.getChannel().sendMessage("WHOA AN ERROR: " + exception.getMessage()).queue();
                if (musicManager.player.getPlayingTrack() == null) killConnection(mpid.getGuild());
            }
        });
    }

    private void queueTrack(MusicPlayerIDs mpid, GuildMusicManager musicManager, AudioTrack track) {
        play(mpid, musicManager, track);
        if (musicManager.scheduler.getQueueSize() < 1) return;
        String timeBefore;
        if (mpid.getGuild().getAudioManager().isConnected()) {
            if (musicManager.scheduler.repeat != 2) {
                final long[] totalLength = {0};
                musicManager.scheduler.getQueue().forEach(list -> totalLength[0] += list.getLeft().getDuration());
                timeBefore = " (Time before it plays: " +
                        Utils.convertMillisToTime(
                                (musicManager.player.getPlayingTrack().getDuration() - musicManager.player.getPlayingTrack().getPosition() + (totalLength[0] - track.getDuration()))) + " **Queue Position: " + musicManager.scheduler.getQueue().size() + "**)";
            } else {
                long totalLength = 0;
                List<Pair<AudioTrack, User>> playlist = musicManager.scheduler.getRepeatQueue();
                for (int i = musicManager.scheduler.getRepeat() + 1; i < playlist.size(); i++) {
                    totalLength += playlist.get(i).getLeft().getDuration();
                }
                timeBefore = " (Time before it plays: " +
                        Utils.convertMillisToTime(
                                (musicManager.player.getPlayingTrack().getDuration() - musicManager.player.getPlayingTrack().getPosition() + (totalLength - track.getDuration()))) + " **Queue Position: " + musicManager.scheduler.getRepeatQueue().size() + "**)";

            }
            mpid.getChannel().sendMessage("Added \"" + track.getInfo().title + "\" to the queue." + timeBefore).queue();
        }
    }

    private void play(MusicPlayerIDs mpid, GuildMusicManager musicManager, AudioTrack track) {
        connectToUsersVoiceChannel(mpid);

        musicManager.scheduler.queue(track, mpid.getUser());
    }

    private void playMeme(MusicPlayerIDs event, GuildMusicManager musicManager, AudioTrack track) {
        connectToUsersVoiceChannel(event);

        musicManager.memeScheduler.queue(track);
    }

    /*** Repeat methods & logic ***/
    public void repeat(CommandEvent event) {
        DoRepeatLogic(new MusicPlayerIDs(event));
    }
    public void repeat(SlashCommandEvent event){
        DoRepeatLogic(new MusicPlayerIDs(event));
    }
    private void DoRepeatLogic(MusicPlayerIDs mpid){
        GuildMusicManager musicManager = getGuildAudioPlayer(mpid, 0);
        if (!musicManager.isMusic()) {
            return;
        }
        musicManager.scheduler.toggleRepeat();
        if (musicManager.scheduler.repeat == 0) mpid.getChannel().sendMessage("Repeat is now set to: **OFF**.").queue();
        else if (musicManager.scheduler.repeat == 1) mpid.getChannel().sendMessage("Repeat is now set to **SINGLE**.").queue();
        else if (musicManager.scheduler.repeat == 2) mpid.getChannel().sendMessage("Repeat is now set to **MULTI**.").queue();
    }

    /** support functions for joining voice channels and other shit **/
    private void connectToUsersVoiceChannel(MusicPlayerIDs mpid) {
        AudioManager audioManager = mpid.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            Optional<VoiceChannel> voiceChannel = mpid.getGuild().getVoiceChannels().stream().filter(c -> c.getMembers().contains(mpid.getMember())).findFirst();
            if (!voiceChannel.isPresent()) {
                mpid.getTextChannel().sendMessage("You're not in a VC you absolute muffin").queue();
            } else {
                audioManager.openAudioConnection(voiceChannel.get());
                announceStart(mpid, voiceChannel.get());
            }
        } else {
            announceStart(mpid, audioManager.getConnectedChannel());
        }
    }

    private void announceStart(MusicPlayerIDs event, VoiceChannel channel) {
        GuildMusicManager musicManager = musicManagers.get(Long.parseLong(event.getGuild().getId()));
        if (musicManager.isMusic() && !musicManager.scheduler.hasStarted()) {
            musicManagers.get(Long.parseLong(event.getGuild().getId())).scheduler.setStarted();
            event.getChannel().sendMessage(event.getUser().getAsMention() + " is playing fire music in: `" + channel.getName() + "`. ").queue();
            musicManagers.get(Long.parseLong(event.getGuild().getId())).scheduler.currentUser = event.getUser();
        }
    }

    public void announceToMusicSession(Guild guild, String message) {
        musicManagers.get(Long.parseLong(guild.getId())).getChannel().sendMessage(message).queue();
    }

    public void shutdown() {
        shutdown("shut down");
    }

    public void shutdown(String reason) {
        Set<Long> sessions = new HashSet<>(musicManagers.keySet());
        sessions.forEach(id -> closeConnection(Floatzel.jda.getGuildById(id), "This music session was ended due to an internal bot error: `" + reason + "`"));
    }

    public void closeConnection(Guild guild) {
        closeConnection(guild, "Now that the music's over, Ima go take a nap");
    }

    public void closeConnection(Guild guild, String reason) {
        long guildId = Long.parseLong(guild.getId());
        if (this.musicManagers.get(guildId).isMusic()) announceToMusicSession(guild, reason);
        this.musicManagers.remove(guildId);
        guild.getAudioManager().closeAudioConnection();
    }

    public boolean containsConnection(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        return musicManagers.containsKey(guildId);
    }

    public void killConnection(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        this.musicManagers.remove(guildId);
    }

    public boolean isMusic(Guild guild) {
        return musicManagers.get(Long.parseLong(guild.getId())).isMusic();
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (isMusic(event.getGuild())) {
                if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                    List<User> potentialHosts = event.getChannelLeft().getMembers().stream().map(Member::getUser).filter(user -> !user.isBot()).collect(Collectors.toList());
                    if (potentialHosts.size() >= 1) {
                        if (getHost(event.getGuild()).equals(event.getMember().getUser())) {
                            Random random = new Random();
                            int user = random.nextInt(potentialHosts.size());
                            User newHost = potentialHosts.get(user);
                            changeHost(event.getGuild(), newHost);
                            announceToMusicSession(event.getGuild(), newHost.getName() + " is now the  epic DJ!.");
                        }
                    } else {
                        announceToMusicSession(event.getGuild(), "I get it, you guys wanna let me sleep. Thats fine by me! cya around");
                        closeConnection(event.getGuild());
                    }
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (isMusic(event.getGuild())) {
                if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                    List<User> potentialHosts = event.getChannelLeft().getMembers().stream().map(Member::getUser).filter(user -> !user.isBot()).collect(Collectors.toList());
                    if (potentialHosts.size() >= 1) {
                        if (getHost(event.getGuild()).equals(event.getMember().getUser())) {
                            Random random = new Random();
                            int user = random.nextInt(potentialHosts.size());
                            User newHost = potentialHosts.get(user);
                            changeHost(event.getGuild(), newHost);
                            announceToMusicSession(event.getGuild(), newHost.getName() + " is now the epic DJ!");
                        }
                    } else {
                        announceToMusicSession(event.getGuild(), "I get it, you guys wanna let me sleep. Thats fine by me! cya around");
                        closeConnection(event.getGuild());
                    }
                }
            }
        }
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        if (containsConnection(event.getGuild()) && !event.getGuild().getAudioManager().isConnected()) {
            closeConnection(event.getGuild());
        }
    }

}
