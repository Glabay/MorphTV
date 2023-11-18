package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;

public final class Address implements Comparable<Address>, Cloneable {
    private final address addr;

    public Address(address address) {
        this.addr = address;
    }

    public Address(String str) {
        error_code error_code = new error_code();
        this.addr = address.from_string(str, error_code);
        if (error_code.value() != null) {
            throw new IllegalArgumentException(error_code.message());
        }
    }

    public Address() {
        this(new address());
    }

    public address swig() {
        return this.addr;
    }

    public boolean isV4() {
        return this.addr.is_v4();
    }

    public boolean isV6() {
        return this.addr.is_v6();
    }

    public boolean isLoopback() {
        return this.addr.is_loopback();
    }

    public boolean isUnspecified() {
        return this.addr.is_unspecified();
    }

    public boolean isMulticast() {
        return this.addr.is_multicast();
    }

    public int compareTo(Address address) {
        return address.compare(this.addr, address.addr);
    }

    public String toString() {
        return toString(this.addr);
    }

    public Address clone() {
        return new Address(new address(this.addr));
    }

    static String toString(address address) {
        error_code error_code = new error_code();
        return error_code.value() != 0 ? "<invalid address>" : address.to_string(error_code);
    }
}
