package com.github.se_bastiaan.torrentstream.exceptions;

public class TorrentInfoException extends Exception {
    public TorrentInfoException() {
        super("No torrent info could be found or read");
    }
}
