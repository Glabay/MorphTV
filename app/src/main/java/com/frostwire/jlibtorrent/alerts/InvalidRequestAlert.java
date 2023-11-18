package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.PeerRequest;
import com.frostwire.jlibtorrent.swig.invalid_request_alert;

public final class InvalidRequestAlert extends PeerAlert<invalid_request_alert> {
    public InvalidRequestAlert(invalid_request_alert invalid_request_alert) {
        super(invalid_request_alert);
    }

    public PeerRequest getRequest() {
        return new PeerRequest(((invalid_request_alert) this.alert).getRequest());
    }
}
