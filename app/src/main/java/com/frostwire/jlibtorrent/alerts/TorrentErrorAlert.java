package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.torrent_error_alert;

public final class TorrentErrorAlert extends TorrentAlert<torrent_error_alert> {
    TorrentErrorAlert(torrent_error_alert torrent_error_alert) {
        super(torrent_error_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((torrent_error_alert) this.alert).getError());
    }

    public String filename() {
        return ((torrent_error_alert) this.alert).filename();
    }
}
