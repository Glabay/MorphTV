package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.storage_moved_alert;

public final class StorageMovedAlert extends TorrentAlert<storage_moved_alert> {
    public StorageMovedAlert(storage_moved_alert storage_moved_alert) {
        super(storage_moved_alert);
    }

    public String storagePath() {
        return ((storage_moved_alert) this.alert).storage_path();
    }
}
