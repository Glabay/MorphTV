package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.log_alert;

public final class LogAlert extends AbstractAlert<log_alert> {
    LogAlert(log_alert log_alert) {
        super(log_alert);
    }

    public String logMessage() {
        return ((log_alert) this.alert).log_message();
    }
}
