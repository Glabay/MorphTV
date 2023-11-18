package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.file_completed_alert;

public final class FileCompletedAlert extends TorrentAlert<file_completed_alert> {
    FileCompletedAlert(file_completed_alert file_completed_alert) {
        super(file_completed_alert);
    }

    public int index() {
        return ((file_completed_alert) this.alert).getIndex();
    }
}
