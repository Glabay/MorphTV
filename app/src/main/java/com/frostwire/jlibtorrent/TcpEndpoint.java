package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;

public final class TcpEndpoint implements Cloneable {
    private final tcp_endpoint endp;

    public TcpEndpoint(tcp_endpoint tcp_endpoint) {
        this.endp = tcp_endpoint;
    }

    public TcpEndpoint() {
        this(new tcp_endpoint());
    }

    public TcpEndpoint(Address address, int i) {
        this(new tcp_endpoint(address.swig(), i));
    }

    public TcpEndpoint(String str, int i) {
        error_code error_code = new error_code();
        address from_string = address.from_string(str, error_code);
        if (error_code.value() != 0) {
            throw new IllegalArgumentException(error_code.message());
        }
        this.endp = new tcp_endpoint(from_string, i);
    }

    public tcp_endpoint swig() {
        return this.endp;
    }

    public Address address() {
        return new Address(this.endp.address());
    }

    public int port() {
        return this.endp.port();
    }

    public String toString() {
        address address = this.endp.address();
        String address2 = Address.toString(address);
        StringBuilder stringBuilder = new StringBuilder();
        if (!address.is_v4()) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("[");
            stringBuilder2.append(address2);
            stringBuilder2.append("]");
            address2 = stringBuilder2.toString();
        }
        stringBuilder.append(address2);
        stringBuilder.append(":");
        stringBuilder.append(this.endp.port());
        return stringBuilder.toString();
    }

    public TcpEndpoint clone() {
        return new TcpEndpoint(new tcp_endpoint(this.endp));
    }
}
