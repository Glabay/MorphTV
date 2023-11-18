package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.i2p_alert;

public final class I2pAlert extends AbstractAlert<i2p_alert> {
    public I2pAlert(i2p_alert i2p_alert) {
        super(i2p_alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(((i2p_alert) this.alert).getError());
    }
}
