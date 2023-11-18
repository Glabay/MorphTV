package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_snubbed_alert;

public final class PeerSnubbedAlert extends PeerAlert<peer_snubbed_alert> {
    public PeerSnubbedAlert(peer_snubbed_alert peer_snubbed_alert) {
        super(peer_snubbed_alert);
    }
}
