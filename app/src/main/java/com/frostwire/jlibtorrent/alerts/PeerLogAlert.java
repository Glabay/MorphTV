package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_log_alert;
import com.frostwire.jlibtorrent.swig.peer_log_alert.direction_t;

public final class PeerLogAlert extends PeerAlert<peer_log_alert> {

    public enum Direction {
        INCOMING_MESSAGE(direction_t.incoming_message.swigValue()),
        OUTGOING_MESSAGE(direction_t.outgoing_message.swigValue()),
        INCOMING(direction_t.incoming.swigValue()),
        OUTGOING(direction_t.outgoing.swigValue()),
        INFO(direction_t.info.swigValue()),
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

    PeerLogAlert(peer_log_alert peer_log_alert) {
        super(peer_log_alert);
    }

    public String eventType() {
        return ((peer_log_alert) this.alert).get_event_type();
    }

    public Direction direction() {
        return Direction.fromSwig(((peer_log_alert) this.alert).getDirection().swigValue());
    }

    public String logMessage() {
        return ((peer_log_alert) this.alert).log_message();
    }
}
