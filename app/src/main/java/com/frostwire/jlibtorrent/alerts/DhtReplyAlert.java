package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.dht_reply_alert;

public final class DhtReplyAlert extends TrackerAlert<dht_reply_alert> {
    DhtReplyAlert(dht_reply_alert dht_reply_alert) {
        super(dht_reply_alert);
    }

    public int numPeers() {
        return ((dht_reply_alert) this.alert).getNum_peers();
    }
}
