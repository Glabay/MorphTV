package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_warning_alert;

public final class TrackerWarningAlert extends TrackerAlert<tracker_warning_alert> {
    TrackerWarningAlert(tracker_warning_alert tracker_warning_alert) {
        super(tracker_warning_alert);
    }

    public String warningMessage() {
        return ((tracker_warning_alert) this.alert).warning_message();
    }
}
