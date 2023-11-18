package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_reply_alert;

public final class TrackerReplyAlert extends TrackerAlert<tracker_reply_alert> {
    public TrackerReplyAlert(tracker_reply_alert tracker_reply_alert) {
        super(tracker_reply_alert);
    }

    public int numPeers() {
        return ((tracker_reply_alert) this.alert).getNum_peers();
    }
}
