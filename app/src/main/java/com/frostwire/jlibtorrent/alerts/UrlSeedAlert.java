package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.url_seed_alert;

public final class UrlSeedAlert extends TorrentAlert<url_seed_alert> {
    UrlSeedAlert(url_seed_alert url_seed_alert) {
        super(url_seed_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((url_seed_alert) this.alert).getError());
    }

    public String serverUrl() {
        return ((url_seed_alert) this.alert).server_url();
    }

    public String errorMessage() {
        return ((url_seed_alert) this.alert).error_message();
    }
}
