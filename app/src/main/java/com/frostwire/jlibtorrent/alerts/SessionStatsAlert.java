package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.session_stats_alert;

public final class SessionStatsAlert extends AbstractAlert<session_stats_alert> {
    SessionStatsAlert(session_stats_alert session_stats_alert) {
        super(session_stats_alert);
    }

    public long value(int i) {
        return ((session_stats_alert) this.alert).get_value(i);
    }
}
