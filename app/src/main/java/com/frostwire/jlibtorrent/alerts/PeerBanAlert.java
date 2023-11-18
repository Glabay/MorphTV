package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_ban_alert;

public final class PeerBanAlert extends PeerAlert<peer_ban_alert> {
    public PeerBanAlert(peer_ban_alert peer_ban_alert) {
        super(peer_ban_alert);
    }
}
