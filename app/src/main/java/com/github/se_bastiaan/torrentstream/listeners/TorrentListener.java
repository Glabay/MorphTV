package com.github.se_bastiaan.torrentstream.listeners;

import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;

public interface TorrentListener {
    void onStreamError(Torrent torrent, Exception exception);

    void onStreamPrepared(Torrent torrent);

    void onStreamProgress(Torrent torrent, StreamStatus streamStatus);

    void onStreamReady(Torrent torrent);

    void onStreamStarted(Torrent torrent);

    void onStreamStopped();
}
