package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.dht_pkt_alert;
import com.frostwire.jlibtorrent.swig.dht_pkt_alert.direction_t;

public final class DhtPktAlert extends AbstractAlert<dht_pkt_alert> {

    public enum Direction {
        INCOMING(direction_t.incoming.swigValue()),
        OUTGOING(direction_t.outgoing.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private Direction(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static Direction fromSwig(int i) {
            for (Direction direction : (Direction[]) Direction.class.getEnumConstants()) {
                if (direction.swig() == i) {
                    return direction;
                }
            }
            return UNKNOWN;
        }
    }

    DhtPktAlert(dht_pkt_alert dht_pkt_alert) {
        super(dht_pkt_alert);
    }

    public byte[] pktBuf() {
        return Vectors.byte_span2bytes(((dht_pkt_alert) this.alert).pkt_buf());
    }

    public Direction direction() {
        return Direction.fromSwig(((dht_pkt_alert) this.alert).getDirection().swigValue());
    }

    public UdpEndpoint node() {
        return new UdpEndpoint(((dht_pkt_alert) this.alert).get_node());
    }
}
