package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.torrent_deleted_alert;

public final class TorrentDeletedAlert extends TorrentAlert<torrent_deleted_alert> {
    public TorrentDeletedAlert(torrent_deleted_alert torrent_deleted_alert) {
        super(torrent_deleted_alert);
    }

    public Sha1Hash getInfoHash() {
        return new Sha1Hash(((torrent_deleted_alert) this.alert).getInfo_hash());
    }
}
