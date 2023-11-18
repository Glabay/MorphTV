package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_alert;

public class TrackerAlert<T extends tracker_alert> extends TorrentAlert<T> {
    TrackerAlert(T t) {
        super(t);
    }

    public String trackerUrl() {
        return ((tracker_alert) this.alert).tracker_url();
    }
}
