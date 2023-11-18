package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.partial_piece_info;

public final class PartialPieceInfo {
    /* renamed from: p */
    private final partial_piece_info f26p;

    public PartialPieceInfo(partial_piece_info partial_piece_info) {
        this.f26p = partial_piece_info;
    }

    public partial_piece_info swig() {
        return this.f26p;
    }

    public int pieceIndex() {
        return this.f26p.getPiece_index();
    }

    public int blocksInPiece() {
        return this.f26p.getBlocks_in_piece();
    }

    public int finished() {
        return this.f26p.getFinished();
    }

    public int writing() {
        return this.f26p.getWriting();
    }

    public int requested() {
        return this.f26p.getRequested();
    }
}
