package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_lookup;

public final class DhtLookup {
    /* renamed from: l */
    private final dht_lookup f20l;

    public DhtLookup(dht_lookup dht_lookup) {
        this.f20l = dht_lookup;
    }

    public dht_lookup swig() {
        return this.f20l;
    }

    public String type() {
        return this.f20l.get_type();
    }

    public int outstandingRequests() {
        return this.f20l.getOutstanding_requests();
    }

    public int timeouts() {
        return this.f20l.getTimeouts();
    }

    public int responses() {
        return this.f20l.getResponses();
    }

    public int branchFactor() {
        return this.f20l.getBranch_factor();
    }

    public int nodesLeft() {
        return this.f20l.getNodes_left();
    }

    public int lastSent() {
        return this.f20l.getLast_sent();
    }

    public int firstTimeout() {
        return this.f20l.getFirst_timeout();
    }

    public Sha1Hash target() {
        return new Sha1Hash(this.f20l.getTarget());
    }
}
