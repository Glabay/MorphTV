package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_request;

public final class PeerRequest {
    /* renamed from: r */
    private final peer_request f28r;

    public PeerRequest(peer_request peer_request) {
        this.f28r = peer_request;
    }

    public peer_request swig() {
        return this.f28r;
    }

    public int piece() {
        return this.f28r.getPiece();
    }

    public int start() {
        return this.f28r.getStart();
    }

    public int length() {
        return this.f28r.getLength();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PeerRequest(piece: ");
        stringBuilder.append(piece());
        stringBuilder.append(", start: ");
        stringBuilder.append(start());
        stringBuilder.append(", length: ");
        stringBuilder.append(length());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
