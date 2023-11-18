package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

public final class UdpEndpoint implements Cloneable {
    private final udp_endpoint endp;

    public UdpEndpoint(udp_endpoint udp_endpoint) {
        this.endp = udp_endpoint;
    }

    public UdpEndpoint() {
        this(new udp_endpoint());
    }

    public UdpEndpoint(Address address, int i) {
        this(new udp_endpoint(address.swig(), i));
    }

    public UdpEndpoint(String str, int i) {
        error_code error_code = new error_code();
        address from_string = address.from_string(str, error_code);
        if (error_code.value() != 0) {
            throw new IllegalArgumentException(error_code.message());
        }
        this.endp = new udp_endpoint(from_string, i);
    }

    public udp_endpoint swig() {
        return this.endp;
    }

    public Address address() {
        return new Address(this.endp.address());
    }

    public int port() {
        return this.endp.port();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("udp:");
        stringBuilder.append(Address.toString(this.endp.address()));
        stringBuilder.append(":");
        stringBuilder.append(this.endp.port());
        return stringBuilder.toString();
    }

    public UdpEndpoint clone() {
        return new UdpEndpoint(new udp_endpoint(this.endp));
    }
}
