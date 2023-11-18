package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_blocked_alert;
import com.frostwire.jlibtorrent.swig.peer_blocked_alert.reason_t;

public final class PeerBlockedAlert extends TorrentAlert<peer_blocked_alert> {

    public enum Reason {
        IP_FILTER(reason_t.ip_filter.swigValue()),
        PORT_FILTER(reason_t.port_filter.swigValue()),
        I2P_MIXED(reason_t.i2p_mixed.swigValue()),
        PRIVILEGED_PORTS(reason_t.privileged_ports.swigValue()),
        UTP_DISABLED(reason_t.utp_disabled.swigValue()),
        TCP_DISABLED(reason_t.tcp_disabled.swigValue()),
        INVALID_LOCAL_INTERFACE(reason_t.invalid_local_interface.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private Reason(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static Reason fromSwig(int i) {
            for (Reason reason : (Reason[]) Reason.class.getEnumConstants()) {
                if (reason.swig() == i) {
                    return reason;
                }
            }
            return UNKNOWN;
        }
    }

    PeerBlockedAlert(peer_blocked_alert peer_blocked_alert) {
        super(peer_blocked_alert);
    }

    public Reason reason() {
        return Reason.fromSwig(((peer_blocked_alert) this.alert).getReason());
    }
}
