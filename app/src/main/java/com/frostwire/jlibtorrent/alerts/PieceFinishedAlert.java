package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.piece_finished_alert;

public final class PieceFinishedAlert extends TorrentAlert<piece_finished_alert> {
    PieceFinishedAlert(piece_finished_alert piece_finished_alert) {
        super(piece_finished_alert);
    }

    public int pieceIndex() {
        return ((piece_finished_alert) this.alert).getPiece_index();
    }
}
