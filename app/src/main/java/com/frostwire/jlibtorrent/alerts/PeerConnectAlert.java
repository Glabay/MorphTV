package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_connect_alert;

public final class PeerConnectAlert extends PeerAlert<peer_connect_alert> {
    PeerConnectAlert(peer_connect_alert peer_connect_alert) {
        super(peer_connect_alert);
    }

    public int socketType() {
        return ((peer_connect_alert) this.alert).getSocket_type();
    }
}
