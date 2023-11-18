package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.lsd_error_alert;

public final class LsdErrorAlert extends AbstractAlert<lsd_error_alert> {
    LsdErrorAlert(lsd_error_alert lsd_error_alert) {
        super(lsd_error_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((lsd_error_alert) this.alert).getError());
    }
}
