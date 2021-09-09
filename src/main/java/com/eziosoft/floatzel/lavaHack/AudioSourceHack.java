package com.eziosoft.floatzel.lavaHack;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class AudioSourceHack extends AudioSourceManagers {

    public static void registerRemoteSources(AudioPlayerManager playerManager) {
        registerRemoteSources(playerManager, ContainerRegHack.DEFAULT_REGISTRY);
    }

    public static void registerLocalSource(AudioPlayerManager playerManager) {
        registerLocalSource(playerManager, ContainerRegHack.DEFAULT_REGISTRY);
    }
}
