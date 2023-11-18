package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_routing_bucket;

public final class DhtRoutingBucket {
    /* renamed from: t */
    private final dht_routing_bucket f21t;

    public DhtRoutingBucket(dht_routing_bucket dht_routing_bucket) {
        this.f21t = dht_routing_bucket;
    }

    public dht_routing_bucket swig() {
        return this.f21t;
    }

    public int numNodes() {
        return this.f21t.getNum_nodes();
    }

    public int numReplacements() {
        return this.f21t.getNum_replacements();
    }

    public int lastActive() {
        return this.f21t.getLast_active();
    }
}
