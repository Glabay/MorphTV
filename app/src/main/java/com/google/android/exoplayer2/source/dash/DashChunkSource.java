package com.google.android.exoplayer2.source.dash;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.source.dash.PlayerEmsgHandler.PlayerTrackEmsgHandler;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;

public interface DashChunkSource extends ChunkSource {

    public interface Factory {
        DashChunkSource createDashChunkSource(LoaderErrorThrower loaderErrorThrower, DashManifest dashManifest, int i, int[] iArr, TrackSelection trackSelection, int i2, long j, boolean z, boolean z2, @Nullable PlayerTrackEmsgHandler playerTrackEmsgHandler);
    }

    void updateManifest(DashManifest dashManifest, int i);
}
