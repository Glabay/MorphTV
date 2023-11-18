package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.portmap_log_alert;

public final class PortmapLogAlert extends AbstractAlert<portmap_log_alert> {
    public PortmapLogAlert(portmap_log_alert portmap_log_alert) {
        super(portmap_log_alert);
    }

    public int mapType() {
        return ((portmap_log_alert) this.alert).getMap_type();
    }

    public String logMessage() {
        return ((portmap_log_alert) this.alert).log_message();
    }
}
