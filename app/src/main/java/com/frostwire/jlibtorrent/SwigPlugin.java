package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.string_view;
import com.frostwire.jlibtorrent.swig.swig_plugin;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

class SwigPlugin extends swig_plugin {
    /* renamed from: p */
    private final Plugin f33p;

    public SwigPlugin(Plugin plugin) {
        this.f33p = plugin;
    }

    public boolean on_dht_request(string_view string_view, udp_endpoint udp_endpoint, bdecode_node bdecode_node, entry entry) {
        return this.f33p.onDhtRequest(Vectors.byte_vector2string(string_view.to_bytes(), "US-ASCII"), new UdpEndpoint(udp_endpoint), new BDecodeNode(bdecode_node), new Entry(entry));
    }
}
