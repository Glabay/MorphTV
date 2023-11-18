package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.storage_moved_failed_alert;

public final class StorageMovedFailedAlert extends TorrentAlert<storage_moved_failed_alert> {
    StorageMovedFailedAlert(storage_moved_failed_alert storage_moved_failed_alert) {
        super(storage_moved_failed_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((storage_moved_failed_alert) this.alert).getError());
    }

    public String filePath() {
        return ((storage_moved_failed_alert) this.alert).file_path();
    }

    public Operation operation() {
        return Operation.fromSwig(((storage_moved_failed_alert) this.alert).getOp());
    }
}
