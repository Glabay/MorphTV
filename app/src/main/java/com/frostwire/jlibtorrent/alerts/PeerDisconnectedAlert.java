package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.peer_disconnected_alert;

public final class PeerDisconnectedAlert extends PeerAlert<peer_disconnected_alert> {
    PeerDisconnectedAlert(peer_disconnected_alert peer_disconnected_alert) {
        super(peer_disconnected_alert);
    }

    public int socketType() {
        return ((peer_disconnected_alert) this.alert).getSocket_type();
    }

    public Operation operation() {
        return Operation.fromSwig(((peer_disconnected_alert) this.alert).getOp());
    }

    public ErrorCode error() {
        return new ErrorCode(((peer_disconnected_alert) this.alert).getError());
    }

    public CloseReason reason() {
        return CloseReason.fromSwig(((peer_disconnected_alert) this.alert).getReason().swigValue());
    }
}
