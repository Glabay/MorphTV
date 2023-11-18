package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.settings_pack.bool_types;
import com.frostwire.jlibtorrent.swig.settings_pack.int_types;
import com.frostwire.jlibtorrent.swig.settings_pack.string_types;

public final class SettingsPack {
    private final settings_pack sp;

    public SettingsPack(settings_pack settings_pack) {
        this.sp = settings_pack;
    }

    public SettingsPack() {
        this(new settings_pack());
    }

    public settings_pack swig() {
        return this.sp;
    }

    public boolean getBoolean(int i) {
        return this.sp.get_bool(i);
    }

    public SettingsPack setBoolean(int i, boolean z) {
        this.sp.set_bool(i, z);
        return this;
    }

    public int getInteger(int i) {
        return this.sp.get_int(i);
    }

    public SettingsPack setInteger(int i, int i2) {
        this.sp.set_int(i, i2);
        return this;
    }

    public String getString(int i) {
        return this.sp.get_str(i);
    }

    public SettingsPack setString(int i, String str) {
        this.sp.set_str(i, str);
        return this;
    }

    public void clear() {
        this.sp.clear();
    }

    public void clear(int i) {
        this.sp.clear(i);
    }

    public int downloadRateLimit() {
        return this.sp.get_int(int_types.download_rate_limit.swigValue());
    }

    public SettingsPack downloadRateLimit(int i) {
        this.sp.set_int(int_types.download_rate_limit.swigValue(), i);
        return this;
    }

    public int uploadRateLimit() {
        return this.sp.get_int(int_types.upload_rate_limit.swigValue());
    }

    public SettingsPack uploadRateLimit(int i) {
        this.sp.set_int(int_types.upload_rate_limit.swigValue(), i);
        return this;
    }

    public int activeDownloads() {
        return this.sp.get_int(int_types.active_downloads.swigValue());
    }

    public SettingsPack activeDownloads(int i) {
        this.sp.set_int(int_types.active_downloads.swigValue(), i);
        return this;
    }

    public int activeSeeds() {
        return this.sp.get_int(int_types.active_seeds.swigValue());
    }

    public SettingsPack activeSeeds(int i) {
        this.sp.set_int(int_types.active_seeds.swigValue(), i);
        return this;
    }

    public int activeChecking() {
        return this.sp.get_int(int_types.active_checking.swigValue());
    }

    public SettingsPack activeChecking(int i) {
        this.sp.set_int(int_types.active_checking.swigValue(), i);
        return this;
    }

    public int activeDhtLimit() {
        return this.sp.get_int(int_types.active_dht_limit.swigValue());
    }

    public SettingsPack activeDhtLimit(int i) {
        this.sp.set_int(int_types.active_checking.swigValue(), i);
        return this;
    }

    public int activeTrackerLimit() {
        return this.sp.get_int(int_types.active_tracker_limit.swigValue());
    }

    public SettingsPack activeTrackerLimit(int i) {
        this.sp.set_int(int_types.active_tracker_limit.swigValue(), i);
        return this;
    }

    public int activeLsdLimit() {
        return this.sp.get_int(int_types.active_lsd_limit.swigValue());
    }

    public SettingsPack activeLsdLimit(int i) {
        this.sp.set_int(int_types.active_lsd_limit.swigValue(), i);
        return this;
    }

    public int activeLimit() {
        return this.sp.get_int(int_types.active_limit.swigValue());
    }

    public SettingsPack activeLimit(int i) {
        this.sp.set_int(int_types.active_limit.swigValue(), i);
        return this;
    }

    public int connectionsLimit() {
        return this.sp.get_int(int_types.connections_limit.swigValue());
    }

    public SettingsPack connectionsLimit(int i) {
        this.sp.set_int(int_types.connections_limit.swigValue(), i);
        return this;
    }

    public int maxPeerlistSize() {
        return this.sp.get_int(int_types.max_peerlist_size.swigValue());
    }

    public SettingsPack maxPeerlistSize(int i) {
        this.sp.set_int(int_types.max_peerlist_size.swigValue(), i);
        return this;
    }

    public int maxQueuedDiskBytes() {
        return this.sp.get_int(int_types.max_queued_disk_bytes.swigValue());
    }

    public SettingsPack maxQueuedDiskBytes(int i) {
        this.sp.set_int(int_types.max_queued_disk_bytes.swigValue(), i);
        return this;
    }

    public int sendBufferWatermark() {
        return this.sp.get_int(int_types.send_buffer_watermark.swigValue());
    }

    public SettingsPack sendBufferWatermark(int i) {
        this.sp.set_int(int_types.send_buffer_watermark.swigValue(), i);
        return this;
    }

    public int cacheSize() {
        return this.sp.get_int(int_types.cache_size.swigValue());
    }

    public SettingsPack cacheSize(int i) {
        this.sp.set_int(int_types.cache_size.swigValue(), i);
        return this;
    }

    public int tickInterval() {
        return this.sp.get_int(int_types.tick_interval.swigValue());
    }

    public SettingsPack tickInterval(int i) {
        this.sp.set_int(int_types.tick_interval.swigValue(), i);
        return this;
    }

    public int inactivityTimeout() {
        return this.sp.get_int(int_types.inactivity_timeout.swigValue());
    }

    public SettingsPack inactivityTimeout(int i) {
        this.sp.set_int(int_types.inactivity_timeout.swigValue(), i);
        return this;
    }

    public boolean seedingOutgoingConnections() {
        return this.sp.get_bool(bool_types.seeding_outgoing_connections.swigValue());
    }

    public SettingsPack seedingOutgoingConnections(boolean z) {
        this.sp.set_bool(bool_types.seeding_outgoing_connections.swigValue(), z);
        return this;
    }

    public boolean anonymousMode() {
        return this.sp.get_bool(bool_types.anonymous_mode.swigValue());
    }

    public SettingsPack anonymousMode(boolean z) {
        this.sp.set_bool(bool_types.anonymous_mode.swigValue(), z);
        return this;
    }

    public boolean broadcastLSD() {
        return this.sp.get_bool(bool_types.broadcast_lsd.swigValue());
    }

    public SettingsPack broadcastLSD(boolean z) {
        this.sp.set_bool(bool_types.broadcast_lsd.swigValue(), z);
        return this;
    }

    public boolean enableDht() {
        return this.sp.get_bool(bool_types.enable_dht.swigValue());
    }

    public SettingsPack enableDht(boolean z) {
        this.sp.set_bool(bool_types.enable_dht.swigValue(), z);
        return this;
    }

    public String listenInterfaces() {
        return this.sp.get_str(string_types.listen_interfaces.swigValue());
    }

    public SettingsPack listenInterfaces(String str) {
        this.sp.set_str(string_types.listen_interfaces.swigValue(), str);
        return this;
    }

    public int stopTrackerTimeout() {
        return this.sp.get_int(int_types.stop_tracker_timeout.swigValue());
    }

    public SettingsPack stopTrackerTimeout(int i) {
        this.sp.set_int(int_types.stop_tracker_timeout.swigValue(), i);
        return this;
    }

    public int alertQueueSize() {
        return this.sp.get_int(int_types.alert_queue_size.swigValue());
    }

    public SettingsPack alertQueueSize(int i) {
        this.sp.set_int(int_types.alert_queue_size.swigValue(), i);
        return this;
    }
}
