package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.swig.external_ip_alert;

public final class ExternalIpAlert extends AbstractAlert<external_ip_alert> {
    ExternalIpAlert(external_ip_alert external_ip_alert) {
        super(external_ip_alert);
    }

    public Address externalAddress() {
        return new Address(((external_ip_alert) this.alert).get_external_address());
    }
}
