package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.torrent_alert;

public class TorrentAlert<T extends torrent_alert> extends AbstractAlert<T> {
    TorrentAlert(T t) {
        super(t);
    }

    public TorrentHandle handle() {
        return new TorrentHandle(((torrent_alert) this.alert).getHandle());
    }

    public String torrentName() {
        return ((torrent_alert) this.alert).torrent_name();
    }
}
