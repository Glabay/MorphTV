package com.frostwire.jlibtorrent.swig;

public class session_handle {
    public static final session_flags_t add_default_plugins = new session_flags_t(libtorrent_jni.session_handle_add_default_plugins_get(), false);
    public static final remove_flags_t delete_files = new remove_flags_t(libtorrent_jni.session_handle_delete_files_get(), false);
    public static final remove_flags_t delete_partfile = new remove_flags_t(libtorrent_jni.session_handle_delete_partfile_get(), false);
    public static final int disk_cache_no_pieces = libtorrent_jni.session_handle_disk_cache_no_pieces_get();
    public static final save_state_flags_t save_dht_settings = new save_state_flags_t(libtorrent_jni.session_handle_save_dht_settings_get(), false);
    public static final save_state_flags_t save_dht_state = new save_state_flags_t(libtorrent_jni.session_handle_save_dht_state_get(), false);
    public static final save_state_flags_t save_encryption_settings = new save_state_flags_t(libtorrent_jni.session_handle_save_encryption_settings_get(), false);
    public static final save_state_flags_t save_settings = new save_state_flags_t(libtorrent_jni.session_handle_save_settings_get(), false);
    public static final session_flags_t start_default_features = new session_flags_t(libtorrent_jni.session_handle_start_default_features_get(), false);
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class protocol_type {
        private static int swigNext;
        private static protocol_type[] swigValues = new protocol_type[]{udp, tcp};
        public static final protocol_type tcp = new protocol_type("tcp", libtorrent_jni.session_handle_tcp_get());
        public static final protocol_type udp = new protocol_type("udp", libtorrent_jni.session_handle_udp_get());
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static protocol_type swigToEnum(int i) {
            if (i < swigValues.length && i >= 0 && swigValues[i].swigValue == i) {
                return swigValues[i];
            }
            for (int i2 = 0; i2 < swigValues.length; i2++) {
                if (swigValues[i2].swigValue == i) {
                    return swigValues[i2];
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No enum ");
            stringBuilder.append(protocol_type.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private protocol_type(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private protocol_type(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private protocol_type(String str, protocol_type protocol_type) {
            this.swigName = str;
            this.swigValue = protocol_type.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected session_handle(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(session_handle session_handle) {
        return session_handle == null ? 0 : session_handle.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_session_handle(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public session_handle() {
        this(libtorrent_jni.new_session_handle__SWIG_0(), true);
    }

    public session_handle(session_handle session_handle) {
        this(libtorrent_jni.new_session_handle__SWIG_1(getCPtr(session_handle), session_handle), true);
    }

    public boolean is_valid() {
        return libtorrent_jni.session_handle_is_valid(this.swigCPtr, this);
    }

    public void save_state(entry entry, save_state_flags_t save_state_flags_t) {
        libtorrent_jni.session_handle_save_state__SWIG_0(this.swigCPtr, this, entry.getCPtr(entry), entry, save_state_flags_t.getCPtr(save_state_flags_t), save_state_flags_t);
    }

    public void save_state(entry entry) {
        libtorrent_jni.session_handle_save_state__SWIG_1(this.swigCPtr, this, entry.getCPtr(entry), entry);
    }

    public void load_state(bdecode_node bdecode_node, save_state_flags_t save_state_flags_t) {
        libtorrent_jni.session_handle_load_state__SWIG_0(this.swigCPtr, this, bdecode_node.getCPtr(bdecode_node), bdecode_node, save_state_flags_t.getCPtr(save_state_flags_t), save_state_flags_t);
    }

    public void load_state(bdecode_node bdecode_node) {
        libtorrent_jni.session_handle_load_state__SWIG_1(this.swigCPtr, this, bdecode_node.getCPtr(bdecode_node), bdecode_node);
    }

    public void refresh_torrent_status(torrent_status_vector torrent_status_vector, status_flags_t status_flags_t) {
        libtorrent_jni.session_handle_refresh_torrent_status__SWIG_0(this.swigCPtr, this, torrent_status_vector.getCPtr(torrent_status_vector), torrent_status_vector, status_flags_t.getCPtr(status_flags_t), status_flags_t);
    }

    public void refresh_torrent_status(torrent_status_vector torrent_status_vector) {
        libtorrent_jni.session_handle_refresh_torrent_status__SWIG_1(this.swigCPtr, this, torrent_status_vector.getCPtr(torrent_status_vector), torrent_status_vector);
    }

    public void post_torrent_updates(status_flags_t status_flags_t) {
        libtorrent_jni.session_handle_post_torrent_updates__SWIG_0(this.swigCPtr, this, status_flags_t.getCPtr(status_flags_t), status_flags_t);
    }

    public void post_torrent_updates() {
        libtorrent_jni.session_handle_post_torrent_updates__SWIG_1(this.swigCPtr, this);
    }

    public void post_session_stats() {
        libtorrent_jni.session_handle_post_session_stats(this.swigCPtr, this);
    }

    public void post_dht_stats() {
        libtorrent_jni.session_handle_post_dht_stats(this.swigCPtr, this);
    }

    public torrent_handle find_torrent(sha1_hash sha1_hash) {
        return new torrent_handle(libtorrent_jni.session_handle_find_torrent(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash), true);
    }

    public torrent_handle_vector get_torrents() {
        return new torrent_handle_vector(libtorrent_jni.session_handle_get_torrents(this.swigCPtr, this), true);
    }

    public torrent_handle add_torrent(add_torrent_params add_torrent_params, error_code error_code) {
        return new torrent_handle(libtorrent_jni.session_handle_add_torrent(this.swigCPtr, this, add_torrent_params.getCPtr(add_torrent_params), add_torrent_params, error_code.getCPtr(error_code), error_code), true);
    }

    public void async_add_torrent(add_torrent_params add_torrent_params) {
        libtorrent_jni.session_handle_async_add_torrent(this.swigCPtr, this, add_torrent_params.getCPtr(add_torrent_params), add_torrent_params);
    }

    public void pause() {
        libtorrent_jni.session_handle_pause(this.swigCPtr, this);
    }

    public void resume() {
        libtorrent_jni.session_handle_resume(this.swigCPtr, this);
    }

    public boolean is_paused() {
        return libtorrent_jni.session_handle_is_paused(this.swigCPtr, this);
    }

    public void set_dht_settings(dht_settings dht_settings) {
        libtorrent_jni.session_handle_set_dht_settings(this.swigCPtr, this, dht_settings.getCPtr(dht_settings), dht_settings);
    }

    public boolean is_dht_running() {
        return libtorrent_jni.session_handle_is_dht_running(this.swigCPtr, this);
    }

    public dht_settings get_dht_settings() {
        return new dht_settings(libtorrent_jni.session_handle_get_dht_settings(this.swigCPtr, this), true);
    }

    public void add_dht_node(string_int_pair string_int_pair) {
        libtorrent_jni.session_handle_add_dht_node(this.swigCPtr, this, string_int_pair.getCPtr(string_int_pair), string_int_pair);
    }

    public void dht_get_item(sha1_hash sha1_hash) {
        libtorrent_jni.session_handle_dht_get_item__SWIG_0(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash dht_put_item(entry entry) {
        return new sha1_hash(libtorrent_jni.session_handle_dht_put_item__SWIG_0(this.swigCPtr, this, entry.getCPtr(entry), entry), true);
    }

    public void dht_get_peers(sha1_hash sha1_hash) {
        libtorrent_jni.session_handle_dht_get_peers(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void dht_announce(sha1_hash sha1_hash, int i, int i2) {
        libtorrent_jni.session_handle_dht_announce__SWIG_0(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash, i, i2);
    }

    public void dht_announce(sha1_hash sha1_hash, int i) {
        libtorrent_jni.session_handle_dht_announce__SWIG_1(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash, i);
    }

    public void dht_announce(sha1_hash sha1_hash) {
        libtorrent_jni.session_handle_dht_announce__SWIG_2(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void dht_live_nodes(sha1_hash sha1_hash) {
        libtorrent_jni.session_handle_dht_live_nodes(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void dht_sample_infohashes(udp_endpoint udp_endpoint, sha1_hash sha1_hash) {
        libtorrent_jni.session_handle_dht_sample_infohashes(this.swigCPtr, this, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void dht_direct_request(udp_endpoint udp_endpoint, entry entry) {
        libtorrent_jni.session_handle_dht_direct_request__SWIG_0(this.swigCPtr, this, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint, entry.getCPtr(entry), entry);
    }

    public void set_ip_filter(ip_filter ip_filter) {
        libtorrent_jni.session_handle_set_ip_filter(this.swigCPtr, this, ip_filter.getCPtr(ip_filter), ip_filter);
    }

    public ip_filter get_ip_filter() {
        return new ip_filter(libtorrent_jni.session_handle_get_ip_filter(this.swigCPtr, this), true);
    }

    public void set_port_filter(port_filter port_filter) {
        libtorrent_jni.session_handle_set_port_filter(this.swigCPtr, this, port_filter.getCPtr(port_filter), port_filter);
    }

    public sha1_hash id() {
        return new sha1_hash(libtorrent_jni.session_handle_id(this.swigCPtr, this), true);
    }

    public void set_key(long j) {
        libtorrent_jni.session_handle_set_key(this.swigCPtr, this, j);
    }

    public static int getGlobal_peer_class_id() {
        return libtorrent_jni.session_handle_global_peer_class_id_get();
    }

    public static int getTcp_peer_class_id() {
        return libtorrent_jni.session_handle_tcp_peer_class_id_get();
    }

    public static int getLocal_peer_class_id() {
        return libtorrent_jni.session_handle_local_peer_class_id_get();
    }

    public int listen_port() {
        return libtorrent_jni.session_handle_listen_port(this.swigCPtr, this);
    }

    public int ssl_listen_port() {
        return libtorrent_jni.session_handle_ssl_listen_port(this.swigCPtr, this);
    }

    public boolean is_listening() {
        return libtorrent_jni.session_handle_is_listening(this.swigCPtr, this);
    }

    public void set_peer_class_filter(ip_filter ip_filter) {
        libtorrent_jni.session_handle_set_peer_class_filter(this.swigCPtr, this, ip_filter.getCPtr(ip_filter), ip_filter);
    }

    public void set_peer_class_type_filter(peer_class_type_filter peer_class_type_filter) {
        libtorrent_jni.session_handle_set_peer_class_type_filter(this.swigCPtr, this, peer_class_type_filter.getCPtr(peer_class_type_filter), peer_class_type_filter);
    }

    public int create_peer_class(String str) {
        return libtorrent_jni.session_handle_create_peer_class(this.swigCPtr, this, str);
    }

    public void delete_peer_class(int i) {
        libtorrent_jni.session_handle_delete_peer_class(this.swigCPtr, this, i);
    }

    public peer_class_info get_peer_class(int i) {
        return new peer_class_info(libtorrent_jni.session_handle_get_peer_class(this.swigCPtr, this, i), 1);
    }

    public void set_peer_class(int i, peer_class_info peer_class_info) {
        libtorrent_jni.session_handle_set_peer_class(this.swigCPtr, this, i, peer_class_info.getCPtr(peer_class_info), peer_class_info);
    }

    public void remove_torrent(torrent_handle torrent_handle, remove_flags_t remove_flags_t) {
        libtorrent_jni.session_handle_remove_torrent__SWIG_0(this.swigCPtr, this, torrent_handle.getCPtr(torrent_handle), torrent_handle, remove_flags_t.getCPtr(remove_flags_t), remove_flags_t);
    }

    public void remove_torrent(torrent_handle torrent_handle) {
        libtorrent_jni.session_handle_remove_torrent__SWIG_1(this.swigCPtr, this, torrent_handle.getCPtr(torrent_handle), torrent_handle);
    }

    public void apply_settings(settings_pack settings_pack) {
        libtorrent_jni.session_handle_apply_settings(this.swigCPtr, this, settings_pack.getCPtr(settings_pack), settings_pack);
    }

    public settings_pack get_settings() {
        return new settings_pack(libtorrent_jni.session_handle_get_settings(this.swigCPtr, this), true);
    }

    public void pop_alerts(alert_ptr_vector alert_ptr_vector) {
        libtorrent_jni.session_handle_pop_alerts(this.swigCPtr, this, alert_ptr_vector.getCPtr(alert_ptr_vector), alert_ptr_vector);
    }

    public int add_port_mapping(protocol_type protocol_type, int i, int i2) {
        return libtorrent_jni.session_handle_add_port_mapping(this.swigCPtr, this, protocol_type.swigValue(), i, i2);
    }

    public void delete_port_mapping(int i) {
        libtorrent_jni.session_handle_delete_port_mapping(this.swigCPtr, this, i);
    }

    public void dht_get_item(byte_vector byte_vector, byte_vector byte_vector2) {
        libtorrent_jni.session_handle_dht_get_item__SWIG_1(this.swigCPtr, this, byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2);
    }

    public void dht_put_item(byte_vector byte_vector, byte_vector byte_vector2, entry entry, byte_vector byte_vector3) {
        libtorrent_jni.session_handle_dht_put_item__SWIG_1(this.swigCPtr, this, byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2, entry.getCPtr(entry), entry, byte_vector.getCPtr(byte_vector3), byte_vector3);
    }

    public void dht_direct_request(udp_endpoint udp_endpoint, entry entry, long j) {
        libtorrent_jni.session_handle_dht_direct_request__SWIG_1(this.swigCPtr, this, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint, entry.getCPtr(entry), entry, j);
    }

    public alert wait_for_alert_ms(long j) {
        j = libtorrent_jni.session_handle_wait_for_alert_ms(this.swigCPtr, this, j);
        if (j == 0) {
            return 0;
        }
        return new alert(j, false);
    }

    public void set_alert_notify_callback(alert_notify_callback alert_notify_callback) {
        libtorrent_jni.session_handle_set_alert_notify_callback(this.swigCPtr, this, alert_notify_callback.getCPtr(alert_notify_callback), alert_notify_callback);
    }

    public void add_extension(swig_plugin swig_plugin) {
        libtorrent_jni.session_handle_add_extension(this.swigCPtr, this, swig_plugin.getCPtr(swig_plugin), swig_plugin);
    }
}
