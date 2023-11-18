package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.hash_failed_alert;

public final class HashFailedAlert extends TorrentAlert<hash_failed_alert> {
    HashFailedAlert(hash_failed_alert hash_failed_alert) {
        super(hash_failed_alert);
    }

    public int pieceIndex() {
        return ((hash_failed_alert) this.alert).getPiece_index();
    }
}
