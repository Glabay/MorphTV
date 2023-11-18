package com.google.android.exoplayer2.source;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.HashMap;

public abstract class CompositeMediaSource<T> implements MediaSource {
    private final HashMap<T, MediaSource> childSources = new HashMap();
    private ExoPlayer player;

    protected abstract void onChildSourceInfoRefreshed(@Nullable T t, MediaSource mediaSource, Timeline timeline, @Nullable Object obj);

    protected CompositeMediaSource() {
    }

    @CallSuper
    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        this.player = exoPlayer;
    }

    @CallSuper
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        for (MediaSource maybeThrowSourceInfoRefreshError : this.childSources.values()) {
            maybeThrowSourceInfoRefreshError.maybeThrowSourceInfoRefreshError();
        }
    }

    @CallSuper
    public void releaseSource() {
        for (MediaSource releaseSource : this.childSources.values()) {
            releaseSource.releaseSource();
        }
        this.childSources.clear();
        this.player = null;
    }

    protected void prepareChildSource(@Nullable final T t, final MediaSource mediaSource) {
        Assertions.checkArgument(this.childSources.containsKey(t) ^ 1);
        this.childSources.put(t, mediaSource);
        mediaSource.prepareSource(this.player, false, new Listener() {
            public void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline, @Nullable Object obj) {
                CompositeMediaSource.this.onChildSourceInfoRefreshed(t, mediaSource, timeline, obj);
            }
        });
    }

    protected void releaseChildSource(@Nullable T t) {
        ((MediaSource) this.childSources.remove(t)).releaseSource();
    }
}
