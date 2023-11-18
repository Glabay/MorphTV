package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.udp_error_alert;

public final class UdpErrorAlert extends AbstractAlert<udp_error_alert> {
    UdpErrorAlert(udp_error_alert udp_error_alert) {
        super(udp_error_alert);
    }

    public UdpEndpoint endpoint() {
        return new UdpEndpoint(((udp_error_alert) this.alert).get_endpoint());
    }

    public ErrorCode error() {
        return new ErrorCode(((udp_error_alert) this.alert).getError());
    }
}
