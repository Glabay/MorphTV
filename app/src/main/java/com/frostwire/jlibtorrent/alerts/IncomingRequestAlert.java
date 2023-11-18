package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.PeerRequest;
import com.frostwire.jlibtorrent.swig.incoming_request_alert;

public final class IncomingRequestAlert extends PeerAlert<incoming_request_alert> {
    public IncomingRequestAlert(incoming_request_alert incoming_request_alert) {
        super(incoming_request_alert);
    }

    public PeerRequest request() {
        return new PeerRequest(((incoming_request_alert) this.alert).getReq());
    }
}
