package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.portmap_error_alert;

public final class PortmapErrorAlert extends AbstractAlert<portmap_error_alert> {
    public PortmapErrorAlert(portmap_error_alert portmap_error_alert) {
        super(portmap_error_alert);
    }

    public int getMapping() {
        return ((portmap_error_alert) this.alert).getMapping();
    }

    public PortmapType getMapType() {
        return PortmapType.fromSwig(((portmap_error_alert) this.alert).getMap_type());
    }

    public ErrorCode getError() {
        return new ErrorCode(((portmap_error_alert) this.alert).getError());
    }
}
