package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.dht_immutable_item_alert;

public final class DhtImmutableItemAlert extends AbstractAlert<dht_immutable_item_alert> {
    DhtImmutableItemAlert(dht_immutable_item_alert dht_immutable_item_alert) {
        super(dht_immutable_item_alert);
    }

    public Sha1Hash target() {
        return new Sha1Hash(((dht_immutable_item_alert) this.alert).getTarget());
    }

    public Entry item() {
        return new Entry(((dht_immutable_item_alert) this.alert).getItem());
    }
}
