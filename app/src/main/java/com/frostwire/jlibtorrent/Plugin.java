package com.frostwire.jlibtorrent;

public interface Plugin {
    boolean onDhtRequest(String str, UdpEndpoint udpEndpoint, BDecodeNode bDecodeNode, Entry entry);
}
