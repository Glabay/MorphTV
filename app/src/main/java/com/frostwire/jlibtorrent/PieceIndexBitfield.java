package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.piece_index_bitfield;
import com.frostwire.jlibtorrent.swig.torrent_status;

public final class PieceIndexBitfield {
    /* renamed from: f */
    private final piece_index_bitfield f29f;
    private final torrent_status ts;

    public PieceIndexBitfield(piece_index_bitfield piece_index_bitfield) {
        this(piece_index_bitfield, null);
    }

    PieceIndexBitfield(piece_index_bitfield piece_index_bitfield, torrent_status torrent_status) {
        this.f29f = piece_index_bitfield;
        this.ts = torrent_status;
    }

    public piece_index_bitfield swig() {
        return this.f29f;
    }

    public torrent_status ts() {
        return this.ts;
    }

    public boolean getBit(int i) {
        return this.f29f.get_bit(i);
    }

    public void clearBit(int i) {
        this.f29f.clear_bit(i);
    }

    public void setBit(int i) {
        this.f29f.set_bit(i);
    }

    public int endIndex() {
        return this.f29f.end_index();
    }

    public boolean isAllSet() {
        return this.f29f.all_set();
    }

    public boolean isNoneSet() {
        return this.f29f.none_set();
    }

    public int size() {
        return this.f29f.size();
    }

    public boolean isEmpty() {
        return this.f29f.empty();
    }

    public int count() {
        return this.f29f.count();
    }

    public int findFirstSet() {
        return this.f29f.find_first_set();
    }

    public int findLastClear() {
        return this.f29f.find_last_clear();
    }

    public void resize(int i, boolean z) {
        this.f29f.resize(i, z);
    }

    public void resize(int i) {
        this.f29f.resize(i);
    }

    public void setAll() {
        this.f29f.set_all();
    }

    public void clearAll() {
        this.f29f.clear_all();
    }

    public void clear() {
        this.f29f.clear();
    }
}
