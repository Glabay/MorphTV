package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.portmap_alert;
import com.frostwire.jlibtorrent.swig.portmap_alert.protocol_t;

public final class PortmapAlert extends AbstractAlert<portmap_alert> {

    public enum Protocol {
        TCP(protocol_t.tcp.swigValue()),
        UDP(protocol_t.udp.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private Protocol(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static Protocol fromSwig(int i) {
            for (Protocol protocol : (Protocol[]) Protocol.class.getEnumConstants()) {
                if (protocol.swig() == i) {
                    return protocol;
                }
            }
            return UNKNOWN;
        }
    }

    public PortmapAlert(portmap_alert portmap_alert) {
        super(portmap_alert);
    }

    public int mapping() {
        return ((portmap_alert) this.alert).getMapping();
    }

    public int externalPort() {
        return ((portmap_alert) this.alert).getExternal_port();
    }

    public PortmapType mapType() {
        return PortmapType.fromSwig(((portmap_alert) this.alert).getMap_type());
    }

    public Protocol protocol() {
        return Protocol.fromSwig(((portmap_alert) this.alert).getProtocol());
    }
}
