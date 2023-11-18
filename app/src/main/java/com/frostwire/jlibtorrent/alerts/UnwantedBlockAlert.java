package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.unwanted_block_alert;

public final class UnwantedBlockAlert extends PeerAlert<unwanted_block_alert> {
    UnwantedBlockAlert(unwanted_block_alert unwanted_block_alert) {
        super(unwanted_block_alert);
    }

    public int blockIndex() {
        return ((unwanted_block_alert) this.alert).getBlock_index();
    }

    public int pieceIndex() {
        return ((unwanted_block_alert) this.alert).getPiece_index();
    }
}
