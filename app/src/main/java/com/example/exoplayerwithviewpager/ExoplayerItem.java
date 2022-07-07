package com.example.exoplayerwithviewpager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;


public class ExoplayerItem {
    ExoPlayer exoPlayer;
    int position;
    MediaSource mediaSource;

    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public void setExoPlayer(ExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MediaSource getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(MediaSource mediaSource) {
        this.mediaSource = mediaSource;
    }

    public ExoplayerItem(ExoPlayer exoPlayer, int position, MediaSource mediaSource) {
        this.exoPlayer = exoPlayer;
        this.position = position;
        this.mediaSource = mediaSource;
    }
}
