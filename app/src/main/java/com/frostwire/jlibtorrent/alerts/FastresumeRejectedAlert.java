package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.fastresume_rejected_alert;

public final class FastresumeRejectedAlert extends TorrentAlert<fastresume_rejected_alert> {
    FastresumeRejectedAlert(fastresume_rejected_alert fastresume_rejected_alert) {
        super(fastresume_rejected_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((fastresume_rejected_alert) this.alert).getError());
    }

    public String filePath() {
        return ((fastresume_rejected_alert) this.alert).file_path();
    }

    public Operation operation() {
        return Operation.fromSwig(((fastresume_rejected_alert) this.alert).getOp());
    }
}
