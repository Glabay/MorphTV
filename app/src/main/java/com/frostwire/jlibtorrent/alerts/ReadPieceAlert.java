package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.read_piece_alert;

public final class ReadPieceAlert extends TorrentAlert<read_piece_alert> {
    ReadPieceAlert(read_piece_alert read_piece_alert) {
        super(read_piece_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((read_piece_alert) this.alert).getError());
    }

    public long bufferPtr() {
        return ((read_piece_alert) this.alert).buffer_ptr();
    }

    public int piece() {
        return ((read_piece_alert) this.alert).getPiece();
    }

    public int size() {
        return ((read_piece_alert) this.alert).getSize();
    }
}
