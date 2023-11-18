package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_downloading_alert;

public final class BlockDownloadingAlert extends PeerAlert<block_downloading_alert> {
    BlockDownloadingAlert(block_downloading_alert block_downloading_alert) {
        super(block_downloading_alert);
    }

    public int blockIndex() {
        return ((block_downloading_alert) this.alert).getBlock_index();
    }

    public int pieceIndex() {
        return ((block_downloading_alert) this.alert).getPiece_index();
    }
}
