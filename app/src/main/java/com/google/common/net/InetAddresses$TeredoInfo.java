package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.net.Inet4Address;
import javax.annotation.Nullable;

@Beta
public final class InetAddresses$TeredoInfo {
    private final Inet4Address client;
    private final int flags;
    private final int port;
    private final Inet4Address server;

    public InetAddresses$TeredoInfo(@Nullable Inet4Address inet4Address, @Nullable Inet4Address inet4Address2, int i, int i2) {
        boolean z = i >= 0 && i <= 65535;
        Preconditions.checkArgument(z, "port '%s' is out of range (0 <= port <= 0xffff)", Integer.valueOf(i));
        boolean z2 = i2 >= 0 && i2 <= 65535;
        Preconditions.checkArgument(z2, "flags '%s' is out of range (0 <= flags <= 0xffff)", Integer.valueOf(i2));
        this.server = (Inet4Address) MoreObjects.firstNonNull(inet4Address, InetAddresses.access$000());
        this.client = (Inet4Address) MoreObjects.firstNonNull(inet4Address2, InetAddresses.access$000());
        this.port = i;
        this.flags = i2;
    }

    public Inet4Address getServer() {
        return this.server;
    }

    public Inet4Address getClient() {
        return this.client;
    }

    public int getPort() {
        return this.port;
    }

    public int getFlags() {
        return this.flags;
    }
}
