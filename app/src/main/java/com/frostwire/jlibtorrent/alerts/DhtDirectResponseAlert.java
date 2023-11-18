package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.BDecodeNode;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_direct_response_alert;

public final class DhtDirectResponseAlert extends AbstractAlert<dht_direct_response_alert> {
    DhtDirectResponseAlert(dht_direct_response_alert dht_direct_response_alert) {
        super(dht_direct_response_alert);
    }

    public long userdata() {
        return ((dht_direct_response_alert) this.alert).get_userdata();
    }

    public UdpEndpoint endpoint() {
        return new UdpEndpoint(((dht_direct_response_alert) this.alert).get_endpoint());
    }

    public BDecodeNode response() {
        return new BDecodeNode(((dht_direct_response_alert) this.alert).response());
    }
}
