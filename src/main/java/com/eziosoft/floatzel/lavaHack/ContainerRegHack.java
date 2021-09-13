package com.eziosoft.floatzel.lavaHack;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.MediaContainerRegistry;

import java.util.List;

public class ContainerRegHack extends MediaContainerRegistry {
    public static final MediaContainerRegistry DEFAULT_REGISTRY = new MediaContainerRegistry(MediaContainerHax.asList());

    private final List<MediaContainerProbe> probes;

    public ContainerRegHack(List<MediaContainerProbe> probes) {
        super(probes);
        this.probes = probes;
    }
}
