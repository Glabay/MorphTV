package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_timeout_alert;

public final class BlockTimeoutAlert extends PeerAlert<block_timeout_alert> {
    BlockTimeoutAlert(block_timeout_alert block_timeout_alert) {
        super(block_timeout_alert);
    }

    public int blockIndex() {
        return ((block_timeout_alert) this.alert).getBlock_index();
    }

    public int pieceIndex() {
        return ((block_timeout_alert) this.alert).getPiece_index();
    }
}
