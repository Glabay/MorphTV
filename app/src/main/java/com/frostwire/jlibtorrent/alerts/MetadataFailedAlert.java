package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.metadata_failed_alert;

public final class MetadataFailedAlert extends TorrentAlert<metadata_failed_alert> {
    MetadataFailedAlert(metadata_failed_alert metadata_failed_alert) {
        super(metadata_failed_alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(((metadata_failed_alert) this.alert).getError());
    }
}
