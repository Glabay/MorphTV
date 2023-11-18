package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.lsd_peer_alert;

public final class LsdPeerAlert extends PeerAlert<lsd_peer_alert> {
    LsdPeerAlert(lsd_peer_alert lsd_peer_alert) {
        super(lsd_peer_alert);
    }
}
