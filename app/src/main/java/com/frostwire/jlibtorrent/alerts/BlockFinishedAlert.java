package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_finished_alert;

public final class BlockFinishedAlert extends PeerAlert<block_finished_alert> {
    BlockFinishedAlert(block_finished_alert block_finished_alert) {
        super(block_finished_alert);
    }

    public int blockIndex() {
        return ((block_finished_alert) this.alert).getBlock_index();
    }

    public int pieceIndex() {
        return ((block_finished_alert) this.alert).getPiece_index();
    }
}
