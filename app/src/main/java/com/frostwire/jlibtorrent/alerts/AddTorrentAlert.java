package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.add_torrent_alert;

public final class AddTorrentAlert extends TorrentAlert<add_torrent_alert> {
    AddTorrentAlert(add_torrent_alert add_torrent_alert) {
        super(add_torrent_alert);
    }

    public AddTorrentParams params() {
        return new AddTorrentParams(((add_torrent_alert) this.alert).getParams());
    }

    public ErrorCode error() {
        return new ErrorCode(((add_torrent_alert) this.alert).getError());
    }
}
