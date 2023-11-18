package com.android.morpheustv.player;

import com.github.se_bastiaan.torrentstream.Torrent;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.TransferListener;

public class TorrentDataSourceFactory implements Factory {
    private long initPosition;
    private final TransferListener<? super TorrentDataSource> listener;
    private final Torrent torrent;

    public TorrentDataSourceFactory(Torrent torrent) {
        this(torrent, 0, null);
    }

    public TorrentDataSourceFactory(Torrent torrent, long j) {
        this(torrent, j, null);
    }

    public TorrentDataSourceFactory(Torrent torrent, long j, TransferListener<? super TorrentDataSource> transferListener) {
        this.initPosition = 0;
        this.listener = transferListener;
        this.torrent = torrent;
        this.initPosition = j;
    }

    public DataSource createDataSource() {
        return new TorrentDataSource(this.torrent, this.initPosition, this.listener);
    }
}
