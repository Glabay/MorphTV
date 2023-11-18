package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.dht_get_peers_alert;

public final class DhtGetPeersAlert extends AbstractAlert<dht_get_peers_alert> {
    DhtGetPeersAlert(dht_get_peers_alert dht_get_peers_alert) {
        super(dht_get_peers_alert);
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(((dht_get_peers_alert) this.alert).getInfo_hash());
    }
}
