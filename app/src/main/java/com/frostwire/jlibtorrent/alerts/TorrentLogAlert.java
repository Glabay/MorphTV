package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_log_alert;

public final class TorrentLogAlert extends TorrentAlert<torrent_log_alert> {
    TorrentLogAlert(torrent_log_alert torrent_log_alert) {
        super(torrent_log_alert);
    }

    public String logMessage() {
        return ((torrent_log_alert) this.alert).log_message();
    }
}
