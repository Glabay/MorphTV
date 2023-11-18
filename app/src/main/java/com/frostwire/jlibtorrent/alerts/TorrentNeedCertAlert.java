package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.torrent_need_cert_alert;

public final class TorrentNeedCertAlert extends TorrentAlert<torrent_need_cert_alert> {
    public TorrentNeedCertAlert(torrent_need_cert_alert torrent_need_cert_alert) {
        super(torrent_need_cert_alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(((torrent_need_cert_alert) this.alert).getError());
    }
}
