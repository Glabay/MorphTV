package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_get_peers_reply_alert;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;
import com.frostwire.jlibtorrent.swig.tcp_endpoint_vector;
import java.util.ArrayList;

public final class DhtGetPeersReplyAlert extends AbstractAlert<dht_get_peers_reply_alert> {
    DhtGetPeersReplyAlert(dht_get_peers_reply_alert dht_get_peers_reply_alert) {
        super(dht_get_peers_reply_alert);
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(((dht_get_peers_reply_alert) this.alert).getInfo_hash());
    }

    public int numPeers() {
        return ((dht_get_peers_reply_alert) this.alert).num_peers();
    }

    public ArrayList<TcpEndpoint> peers() {
        tcp_endpoint_vector peers = ((dht_get_peers_reply_alert) this.alert).peers();
        int size = (int) peers.size();
        ArrayList<TcpEndpoint> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            tcp_endpoint tcp_endpoint = peers.get(i);
            arrayList.add(new TcpEndpoint(new Address(tcp_endpoint.address()).toString(), tcp_endpoint.port()));
        }
        return arrayList;
    }
}
