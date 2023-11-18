package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.torrent_removed_alert;

public final class TorrentRemovedAlert extends TorrentAlert<torrent_removed_alert> {
    TorrentRemovedAlert(torrent_removed_alert torrent_removed_alert) {
        super(torrent_removed_alert);
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(((torrent_removed_alert) this.alert).getInfo_hash());
    }
}
