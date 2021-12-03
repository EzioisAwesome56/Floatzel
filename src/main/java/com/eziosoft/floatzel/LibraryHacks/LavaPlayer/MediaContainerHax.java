package com.eziosoft.floatzel.LibraryHacks.LavaPlayer;

/* Unfortunately I can't extend enums, so this code is entirely borrowed from lavaplayer itself.
I Believe in order to be license compatible, I have to provide links to the original source code and the license
Source: https://github.com/sedmelluq/lavaplayer/blob/master/main/src/main/java/com/sedmelluq/discord/lavaplayer/container/MediaContainer.java
lavaplayer is licensed under the Apache 2.0 license, which can be found & read here:
https://raw.githubusercontent.com/sedmelluq/lavaplayer/master/LICENSE
if this is not in compliance with the license, or I did something wrong, please inform me. Thank you
 */

import com.sedmelluq.discord.lavaplayer.container.MediaContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.adts.AdtsContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.flac.FlacContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.matroska.MatroskaContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.mp3.Mp3ContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.mpeg.MpegContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.mpegts.MpegAdtsContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.ogg.OggContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.playlists.M3uPlaylistContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.playlists.PlainPlaylistContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.playlists.PlsPlaylistContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.wav.WavContainerProbe;
import com.sedmelluq.lavaplayer.extensions.format.xm.XmContainerProbe;

import java.util.ArrayList;
import java.util.List;

public enum MediaContainerHax {
    WAV(new WavContainerProbe()),
    MKV(new MatroskaContainerProbe()),
    MP4(new MpegContainerProbe()),
    FLAC(new FlacContainerProbe()),
    OGG(new OggContainerProbe()),
    M3U(new M3uPlaylistContainerProbe()),
    PLS(new PlsPlaylistContainerProbe()),
    PLAIN(new PlainPlaylistContainerProbe()),
    MP3(new Mp3ContainerProbe()),
    ADTS(new AdtsContainerProbe()),
    MPEGADTS(new MpegAdtsContainerProbe()),
    // the reason why i needed to replace this enum: add XM support from the extension
    XM(new XmContainerProbe());

    /**
     * The probe used to detect files using this container and create the audio tracks for them.
     */
    public final MediaContainerProbe probe;

    MediaContainerHax(MediaContainerProbe probe) {
        this.probe = probe;
    }

    public static List<MediaContainerProbe> asList() {
        List<MediaContainerProbe> probes = new ArrayList<>();

        for (MediaContainerHax container : MediaContainerHax.class.getEnumConstants()) {
            probes.add(container.probe);
        }

        return probes;
    }
}
