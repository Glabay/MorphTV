package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.tracker_error_alert;

public final class TrackerErrorAlert extends TrackerAlert<tracker_error_alert> {
    public TrackerErrorAlert(tracker_error_alert tracker_error_alert) {
        super(tracker_error_alert);
    }

    public int getTimesInRow() {
        return ((tracker_error_alert) this.alert).getTimes_in_row();
    }

    public int getStatusCode() {
        return ((tracker_error_alert) this.alert).getStatus_code();
    }

    public ErrorCode getError() {
        return new ErrorCode(((tracker_error_alert) this.alert).getError());
    }

    public String errorMessage() {
        return ((tracker_error_alert) this.alert).error_message();
    }
}
