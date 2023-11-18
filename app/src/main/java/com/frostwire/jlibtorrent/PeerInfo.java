package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_info;

public final class PeerInfo {
    /* renamed from: p */
    private final peer_info f27p;

    PeerInfo(peer_info peer_info) {
        this.f27p = peer_info;
    }

    public peer_info swig() {
        return this.f27p;
    }

    public byte[] client() {
        return Vectors.byte_vector2bytes(this.f27p.get_client());
    }

    public PieceIndexBitfield pieces() {
        return new PieceIndexBitfield(this.f27p.getPieces());
    }

    public long totalDownload() {
        return this.f27p.getTotal_download();
    }

    public long totalUpload() {
        return this.f27p.getTotal_upload();
    }
}
