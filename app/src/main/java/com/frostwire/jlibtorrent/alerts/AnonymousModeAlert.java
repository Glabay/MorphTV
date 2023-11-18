package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.anonymous_mode_alert;
import com.frostwire.jlibtorrent.swig.anonymous_mode_alert.kind_t;

public final class AnonymousModeAlert extends TorrentAlert<anonymous_mode_alert> {

    public enum Kind {
        TRACKER_NOT_ANONYMOUS(kind_t.tracker_not_anonymous.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private Kind(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static Kind fromSwig(int i) {
            for (Kind kind : (Kind[]) Kind.class.getEnumConstants()) {
                if (kind.swig() == i) {
                    return kind;
                }
            }
            return UNKNOWN;
        }
    }

    AnonymousModeAlert(anonymous_mode_alert anonymous_mode_alert) {
        super(anonymous_mode_alert);
    }

    public Kind kind() {
        return Kind.fromSwig(((anonymous_mode_alert) this.alert).getKind());
    }

    public String str() {
        return ((anonymous_mode_alert) this.alert).getStr();
    }
}
