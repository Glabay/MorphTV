package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_uploaded_alert;

public final class BlockUploadedAlert extends PeerAlert<block_uploaded_alert> {
    BlockUploadedAlert(block_uploaded_alert block_uploaded_alert) {
        super(block_uploaded_alert);
    }

    public int blockIndex() {
        return ((block_uploaded_alert) this.alert).getBlock_index();
    }

    public int pieceIndex() {
        return ((block_uploaded_alert) this.alert).getPiece_index();
    }
}
