package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_entry;

public final class AnnounceEntry {
    /* renamed from: e */
    private final announce_entry f17e;

    public AnnounceEntry(announce_entry announce_entry) {
        this.f17e = announce_entry;
    }

    public AnnounceEntry(String str) {
        this(new announce_entry(str));
    }

    public announce_entry swig() {
        return this.f17e;
    }

    public String url() {
        return this.f17e.getUrl();
    }

    public String trackerId() {
        return this.f17e.getTrackerid();
    }

    public int tier() {
        return this.f17e.getTier();
    }
}
