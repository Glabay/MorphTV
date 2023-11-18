package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.request_dropped_alert;

public final class RequestDroppedAlert extends PeerAlert<request_dropped_alert> {
    public RequestDroppedAlert(request_dropped_alert request_dropped_alert) {
        super(request_dropped_alert);
    }

    public int getBlockIndex() {
        return ((request_dropped_alert) this.alert).getBlock_index();
    }

    public int getPieceIndex() {
        return ((request_dropped_alert) this.alert).getPiece_index();
    }
}
