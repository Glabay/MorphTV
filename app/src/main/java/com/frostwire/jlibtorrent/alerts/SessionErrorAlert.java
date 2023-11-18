package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.session_error_alert;

public final class SessionErrorAlert extends AbstractAlert<session_error_alert> {
    SessionErrorAlert(session_error_alert session_error_alert) {
        super(session_error_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((session_error_alert) this.alert).getError());
    }
}
