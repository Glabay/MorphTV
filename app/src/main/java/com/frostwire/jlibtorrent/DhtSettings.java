package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_settings;

public final class DhtSettings {
    /* renamed from: s */
    private final dht_settings f22s;

    public DhtSettings(dht_settings dht_settings) {
        this.f22s = dht_settings;
    }

    public DhtSettings() {
        this(new dht_settings());
    }

    public dht_settings swig() {
        return this.f22s;
    }

    public int maxPeersReply() {
        return this.f22s.getMax_peers_reply();
    }

    public void maxPeersReply(int i) {
        this.f22s.setMax_peers_reply(i);
    }

    public int getSearchBranching() {
        return this.f22s.getSearch_branching();
    }

    public void setSearchBranching(int i) {
        this.f22s.setSearch_branching(i);
    }

    public int getMaxFailCount() {
        return this.f22s.getMax_fail_count();
    }

    public void setMaxFailCount(int i) {
        this.f22s.setMax_fail_count(i);
    }

    public int maxTorrents() {
        return this.f22s.getMax_torrents();
    }

    public void maxTorrents(int i) {
        this.f22s.setMax_torrents(i);
    }

    public int maxDhtItems() {
        return this.f22s.getMax_dht_items();
    }

    public void maxDhtItems(int i) {
        this.f22s.setMax_dht_items(i);
    }

    public int maxPeers() {
        return this.f22s.getMax_peers();
    }

    public void maxPeers(int i) {
        this.f22s.setMax_peers(i);
    }

    public int getMaxTorrentSearchReply() {
        return this.f22s.getMax_torrent_search_reply();
    }

    public void setMaxTorrentSearchReply(int i) {
        this.f22s.setMax_torrent_search_reply(i);
    }

    public boolean isRestrictRoutingIPs() {
        return this.f22s.getRestrict_routing_ips();
    }

    public void setRestrictRoutingIPs(boolean z) {
        this.f22s.setRestrict_routing_ips(z);
    }

    public boolean isRestrictSearchIPs() {
        return this.f22s.getRestrict_search_ips();
    }

    public void setRestrictSearchIPs(boolean z) {
        this.f22s.setRestrict_search_ips(z);
    }

    public boolean isExtendedRoutingTable() {
        return this.f22s.getExtended_routing_table();
    }

    public void setExtendedRoutingTable(boolean z) {
        this.f22s.setExtended_routing_table(z);
    }

    public boolean isAggressiveLookups() {
        return this.f22s.getAggressive_lookups();
    }

    public void getAggressiveLookups(boolean z) {
        this.f22s.setAggressive_lookups(z);
    }

    public boolean isPrivacyLookups() {
        return this.f22s.getPrivacy_lookups();
    }

    public void setPrivacyLookups(boolean z) {
        this.f22s.setPrivacy_lookups(z);
    }

    public boolean isEnforceNodeId() {
        return this.f22s.getEnforce_node_id();
    }

    public void setEnforceNodeId(boolean z) {
        this.f22s.setEnforce_node_id(z);
    }

    public boolean isIgnoreDarkInternet() {
        return this.f22s.getIgnore_dark_internet();
    }

    public void setIgnoreDarkInternet(boolean z) {
        this.f22s.setIgnore_dark_internet(z);
    }

    public int blockTimeout() {
        return this.f22s.getBlock_timeout();
    }

    public void blockTimeout(int i) {
        this.f22s.setBlock_timeout(i);
    }

    public int blockRatelimit() {
        return this.f22s.getBlock_ratelimit();
    }

    public void blockRatelimit(int i) {
        this.f22s.setBlock_ratelimit(i);
    }

    public boolean readOnly() {
        return this.f22s.getRead_only();
    }

    public void readOnly(boolean z) {
        this.f22s.setRead_only(z);
    }

    public int itemLifetime() {
        return this.f22s.getItem_lifetime();
    }

    public void itemLifetime(int i) {
        this.f22s.setItem_lifetime(i);
    }

    public int uploadRateLimit() {
        return this.f22s.getUpload_rate_limit();
    }

    public void uploadRateLimit(int i) {
        this.f22s.setUpload_rate_limit(i);
    }
}
