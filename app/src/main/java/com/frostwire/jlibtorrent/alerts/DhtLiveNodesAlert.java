package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_live_nodes_alert;
import com.frostwire.jlibtorrent.swig.sha1_hash_udp_endpoint_pair;
import com.frostwire.jlibtorrent.swig.sha1_hash_udp_endpoint_pair_vector;
import java.util.ArrayList;
import java.util.List;

public final class DhtLiveNodesAlert extends AbstractAlert<dht_live_nodes_alert> {
    DhtLiveNodesAlert(dht_live_nodes_alert dht_live_nodes_alert) {
        super(dht_live_nodes_alert);
    }

    public Sha1Hash nodeId() {
        return new Sha1Hash(((dht_live_nodes_alert) this.alert).getNode_id());
    }

    public int numNodes() {
        return ((dht_live_nodes_alert) this.alert).num_nodes();
    }

    public List<Pair<Sha1Hash, UdpEndpoint>> nodes() {
        sha1_hash_udp_endpoint_pair_vector nodes = ((dht_live_nodes_alert) this.alert).nodes();
        int size = (int) nodes.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            sha1_hash_udp_endpoint_pair sha1_hash_udp_endpoint_pair = nodes.get(i);
            arrayList.add(new Pair(new Sha1Hash(sha1_hash_udp_endpoint_pair.getFirst()).clone(), new UdpEndpoint(sha1_hash_udp_endpoint_pair.getSecond()).clone()));
        }
        return arrayList;
    }
}
