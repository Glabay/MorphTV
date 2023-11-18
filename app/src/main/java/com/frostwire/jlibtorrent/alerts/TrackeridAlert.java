package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.trackerid_alert;

public final class TrackeridAlert extends TorrentAlert<trackerid_alert> {
    TrackeridAlert(trackerid_alert trackerid_alert) {
        super(trackerid_alert);
    }

    public String trackerId() {
        return ((trackerid_alert) this.alert).tracker_id();
    }
}
