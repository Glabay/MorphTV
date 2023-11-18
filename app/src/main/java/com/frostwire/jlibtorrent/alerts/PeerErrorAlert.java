package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.peer_error_alert;

public final class PeerErrorAlert extends PeerAlert<peer_error_alert> {
    PeerErrorAlert(peer_error_alert peer_error_alert) {
        super(peer_error_alert);
    }

    public Operation operation() {
        return Operation.fromSwig(((peer_error_alert) this.alert).getOp());
    }

    public ErrorCode error() {
        return new ErrorCode(((peer_error_alert) this.alert).getError());
    }
}
