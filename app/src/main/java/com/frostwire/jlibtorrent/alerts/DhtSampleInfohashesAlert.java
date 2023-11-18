package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_sample_infohashes_alert;
import com.frostwire.jlibtorrent.swig.sha1_hash_udp_endpoint_pair;
import com.frostwire.jlibtorrent.swig.sha1_hash_udp_endpoint_pair_vector;
import com.frostwire.jlibtorrent.swig.sha1_hash_vector;
import java.util.ArrayList;
import java.util.List;

public final class DhtSampleInfohashesAlert extends AbstractAlert<dht_sample_infohashes_alert> {
    DhtSampleInfohashesAlert(dht_sample_infohashes_alert dht_sample_infohashes_alert) {
        super(dht_sample_infohashes_alert);
    }

    public UdpEndpoint endpoint() {
        return new UdpEndpoint(((dht_sample_infohashes_alert) this.alert).get_endpoint());
    }

    public long interval() {
        return ((dht_sample_infohashes_alert) this.alert).get_interval();
    }

    public int numInfohashes() {
        return ((dht_sample_infohashes_alert) this.alert).getNum_infohashes();
    }

    public int numSamples() {
        return ((dht_sample_infohashes_alert) this.alert).num_samples();
    }

    public List<Sha1Hash> samples() {
        sha1_hash_vector samples = ((dht_sample_infohashes_alert) this.alert).samples();
        int size = (int) samples.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new Sha1Hash(samples.get(i)).clone());
        }
        return arrayList;
    }

    public int numNodes() {
        return ((dht_sample_infohashes_alert) this.alert).num_nodes();
    }

    public List<Pair<Sha1Hash, UdpEndpoint>> nodes() {
        sha1_hash_udp_endpoint_pair_vector nodes = ((dht_sample_infohashes_alert) this.alert).nodes();
        int size = (int) nodes.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            sha1_hash_udp_endpoint_pair sha1_hash_udp_endpoint_pair = nodes.get(i);
            arrayList.add(new Pair(new Sha1Hash(sha1_hash_udp_endpoint_pair.getFirst()).clone(), new UdpEndpoint(sha1_hash_udp_endpoint_pair.getSecond()).clone()));
        }
        return arrayList;
    }
}
