package com.github.se_bastiaan.torrentstream.exceptions;

public class NotInitializedException extends Exception {
    public NotInitializedException() {
        super("TorrentStreamer is not initialized. Call init() first before getting an instance.");
    }
}
