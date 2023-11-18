package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.dht_announce_alert;

public final class DhtAnnounceAlert extends AbstractAlert<dht_announce_alert> {
    DhtAnnounceAlert(dht_announce_alert dht_announce_alert) {
        super(dht_announce_alert);
    }

    public Address ip() {
        return new Address(((dht_announce_alert) this.alert).get_ip());
    }

    public int port() {
        return ((dht_announce_alert) this.alert).getPort();
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(((dht_announce_alert) this.alert).getInfo_hash());
    }
}
