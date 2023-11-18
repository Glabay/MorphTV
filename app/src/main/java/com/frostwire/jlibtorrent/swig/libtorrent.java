package com.frostwire.jlibtorrent.swig;

public class libtorrent implements libtorrentConstants {
    public static boolean op_eq(error_code error_code, error_code error_code2) {
        return libtorrent_jni.op_eq__SWIG_1(error_code.getCPtr(error_code), error_code, error_code.getCPtr(error_code2), error_code2);
    }

    public static boolean op_lt(error_code error_code, error_code error_code2) {
        return libtorrent_jni.op_lt__SWIG_1(error_code.getCPtr(error_code), error_code, error_code.getCPtr(error_code2), error_code2);
    }

    public static boolean op_ne(error_code error_code, error_code error_code2) {
        return libtorrent_jni.op_ne(error_code.getCPtr(error_code), error_code, error_code.getCPtr(error_code2), error_code2);
    }

    public static error_code make_error_code(errc_t errc_t) {
        return new error_code(libtorrent_jni.make_error_code(errc_t.swigValue()), true);
    }

    public static String version() {
        return libtorrent_jni.version();
    }

    public static torrent_flags_t getSeed_mode() {
        long seed_mode_get = libtorrent_jni.seed_mode_get();
        if (seed_mode_get == 0) {
            return null;
        }
        return new torrent_flags_t(seed_mode_get, false);
    }

    public static torrent_flags_t getUpload_mode() {
        long upload_mode_get = libtorrent_jni.upload_mode_get();
        if (upload_mode_get == 0) {
            return null;
        }
        return new torrent_flags_t(upload_mode_get, false);
    }

    public static torrent_flags_t getShare_mode() {
        long share_mode_get = libtorrent_jni.share_mode_get();
        if (share_mode_get == 0) {
            return null;
        }
        return new torrent_flags_t(share_mode_get, false);
    }

    public static torrent_flags_t getApply_ip_filter() {
        long apply_ip_filter_get = libtorrent_jni.apply_ip_filter_get();
        if (apply_ip_filter_get == 0) {
            return null;
        }
        return new torrent_flags_t(apply_ip_filter_get, false);
    }

    public static torrent_flags_t getPaused() {
        long paused_get = libtorrent_jni.paused_get();
        if (paused_get == 0) {
            return null;
        }
        return new torrent_flags_t(paused_get, false);
    }

    public static torrent_flags_t getAuto_managed() {
        long auto_managed_get = libtorrent_jni.auto_managed_get();
        if (auto_managed_get == 0) {
            return null;
        }
        return new torrent_flags_t(auto_managed_get, false);
    }

    public static torrent_flags_t getDuplicate_is_error() {
        long duplicate_is_error_get = libtorrent_jni.duplicate_is_error_get();
        if (duplicate_is_error_get == 0) {
            return null;
        }
        return new torrent_flags_t(duplicate_is_error_get, false);
    }

    public static torrent_flags_t getUpdate_subscribe() {
        long update_subscribe_get = libtorrent_jni.update_subscribe_get();
        if (update_subscribe_get == 0) {
            return null;
        }
        return new torrent_flags_t(update_subscribe_get, false);
    }

    public static torrent_flags_t getSuper_seeding() {
        long super_seeding_get = libtorrent_jni.super_seeding_get();
        if (super_seeding_get == 0) {
            return null;
        }
        return new torrent_flags_t(super_seeding_get, false);
    }

    public static torrent_flags_t getSequential_download() {
        long sequential_download_get = libtorrent_jni.sequential_download_get();
        if (sequential_download_get == 0) {
            return null;
        }
        return new torrent_flags_t(sequential_download_get, false);
    }

    public static torrent_flags_t getStop_when_ready() {
        long stop_when_ready_get = libtorrent_jni.stop_when_ready_get();
        if (stop_when_ready_get == 0) {
            return null;
        }
        return new torrent_flags_t(stop_when_ready_get, false);
    }

    public static torrent_flags_t getOverride_trackers() {
        long override_trackers_get = libtorrent_jni.override_trackers_get();
        if (override_trackers_get == 0) {
            return null;
        }
        return new torrent_flags_t(override_trackers_get, false);
    }

    public static torrent_flags_t getOverride_web_seeds() {
        long override_web_seeds_get = libtorrent_jni.override_web_seeds_get();
        if (override_web_seeds_get == 0) {
            return null;
        }
        return new torrent_flags_t(override_web_seeds_get, false);
    }

    public static torrent_flags_t getNeed_save_resume() {
        long need_save_resume_get = libtorrent_jni.need_save_resume_get();
        if (need_save_resume_get == 0) {
            return null;
        }
        return new torrent_flags_t(need_save_resume_get, false);
    }

    public static torrent_flags_t getAll() {
        long all_get = libtorrent_jni.all_get();
        if (all_get == 0) {
            return null;
        }
        return new torrent_flags_t(all_get, false);
    }

    public static torrent_flags_t getDefault_flags() {
        long default_flags_get = libtorrent_jni.default_flags_get();
        if (default_flags_get == 0) {
            return null;
        }
        return new torrent_flags_t(default_flags_get, false);
    }

    public static String operation_name(operation_t operation_t) {
        return libtorrent_jni.operation_name(operation_t.swigValue());
    }

    public static stats_metric_vector session_stats_metrics() {
        return new stats_metric_vector(libtorrent_jni.session_stats_metrics(), true);
    }

    public static int find_metric_idx(String str) {
        return libtorrent_jni.find_metric_idx(str);
    }

    public static int getNum_alert_types() {
        return libtorrent_jni.num_alert_types_get();
    }

    public static int setting_by_name(String str) {
        return libtorrent_jni.setting_by_name(str);
    }

    public static String name_for_setting(int i) {
        return libtorrent_jni.name_for_setting(i);
    }

    public static settings_pack default_settings() {
        return new settings_pack(libtorrent_jni.default_settings(), true);
    }

    public static address_sha1_hash_pair_vector extract_node_ids(bdecode_node bdecode_node, string_view string_view) {
        return new address_sha1_hash_pair_vector(libtorrent_jni.extract_node_ids(bdecode_node.getCPtr(bdecode_node), bdecode_node, string_view.getCPtr(string_view), string_view), true);
    }

    public static dht_state read_dht_state(bdecode_node bdecode_node) {
        return new dht_state(libtorrent_jni.read_dht_state(bdecode_node.getCPtr(bdecode_node), bdecode_node), true);
    }

    public static entry save_dht_state(dht_state dht_state) {
        return new entry(libtorrent_jni.save_dht_state(dht_state.getCPtr(dht_state), dht_state), true);
    }

    public static settings_pack min_memory_usage() {
        return new settings_pack(libtorrent_jni.min_memory_usage(), true);
    }

    public static settings_pack high_performance_seed() {
        return new settings_pack(libtorrent_jni.high_performance_seed(), true);
    }

    public static session_params read_session_params(bdecode_node bdecode_node, save_state_flags_t save_state_flags_t) {
        return new session_params(libtorrent_jni.read_session_params__SWIG_0(bdecode_node.getCPtr(bdecode_node), bdecode_node, save_state_flags_t.getCPtr(save_state_flags_t), save_state_flags_t), true);
    }

    public static session_params read_session_params(bdecode_node bdecode_node) {
        return new session_params(libtorrent_jni.read_session_params__SWIG_1(bdecode_node.getCPtr(bdecode_node), bdecode_node), true);
    }

    public static boolean op_lte(address address, address address2) {
        return libtorrent_jni.op_lte(address.getCPtr(address), address, address.getCPtr(address2), address2);
    }

    public static String make_magnet_uri(torrent_handle torrent_handle) {
        return libtorrent_jni.make_magnet_uri__SWIG_0(torrent_handle.getCPtr(torrent_handle), torrent_handle);
    }

    public static String make_magnet_uri(torrent_info torrent_info) {
        return libtorrent_jni.make_magnet_uri__SWIG_1(torrent_info.getCPtr(torrent_info), torrent_info);
    }

    public static void add_files(file_storage file_storage, String str, create_flags_t create_flags_t) {
        libtorrent_jni.add_files__SWIG_0(file_storage.getCPtr(file_storage), file_storage, str, create_flags_t.getCPtr(create_flags_t), create_flags_t);
    }

    public static void add_files(file_storage file_storage, String str) {
        libtorrent_jni.add_files__SWIG_1(file_storage.getCPtr(file_storage), file_storage, str);
    }

    public static void set_piece_hashes(create_torrent create_torrent, String str, error_code error_code) {
        libtorrent_jni.set_piece_hashes(create_torrent.getCPtr(create_torrent), create_torrent, str, error_code.getCPtr(error_code), error_code);
    }

    public static String generate_fingerprint(String str, int i, int i2, int i3, int i4) {
        return libtorrent_jni.generate_fingerprint(str, i, i2, i3, i4);
    }

    public static byte_vector ed25519_create_seed() {
        return new byte_vector(libtorrent_jni.ed25519_create_seed(), true);
    }

    public static byte_vectors_pair ed25519_create_keypair(byte_vector byte_vector) {
        return new byte_vectors_pair(libtorrent_jni.ed25519_create_keypair(byte_vector.getCPtr(byte_vector), byte_vector), true);
    }

    public static byte_vector ed25519_sign(byte_vector byte_vector, byte_vector byte_vector2, byte_vector byte_vector3) {
        return new byte_vector(libtorrent_jni.ed25519_sign(byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2, byte_vector.getCPtr(byte_vector3), byte_vector3), true);
    }

    public static boolean ed25519_verify(byte_vector byte_vector, byte_vector byte_vector2, byte_vector byte_vector3) {
        return libtorrent_jni.ed25519_verify(byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2, byte_vector.getCPtr(byte_vector3), byte_vector3);
    }

    public static byte_vector ed25519_add_scalar_public(byte_vector byte_vector, byte_vector byte_vector2) {
        return new byte_vector(libtorrent_jni.ed25519_add_scalar_public(byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2), true);
    }

    public static byte_vector ed25519_add_scalar_secret(byte_vector byte_vector, byte_vector byte_vector2) {
        return new byte_vector(libtorrent_jni.ed25519_add_scalar_secret(byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2), true);
    }

    public static byte_vector ed25519_key_exchange(byte_vector byte_vector, byte_vector byte_vector2) {
        return new byte_vector(libtorrent_jni.ed25519_key_exchange(byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2), true);
    }

    public static void add_files_ex(file_storage file_storage, String str, add_files_listener add_files_listener, create_flags_t create_flags_t) {
        libtorrent_jni.add_files_ex(file_storage.getCPtr(file_storage), file_storage, str, add_files_listener.getCPtr(add_files_listener), add_files_listener, create_flags_t.getCPtr(create_flags_t), create_flags_t);
    }

    public static void set_piece_hashes_ex(create_torrent create_torrent, String str, set_piece_hashes_listener set_piece_hashes_listener, error_code error_code) {
        libtorrent_jni.set_piece_hashes_ex(create_torrent.getCPtr(create_torrent), create_torrent, str, set_piece_hashes_listener.getCPtr(set_piece_hashes_listener), set_piece_hashes_listener, error_code.getCPtr(error_code), error_code);
    }

    public static int boost_version() {
        return libtorrent_jni.boost_version();
    }

    public static String boost_lib_version() {
        return libtorrent_jni.boost_lib_version();
    }

    public static int openssl_version_number() {
        return libtorrent_jni.openssl_version_number();
    }

    public static String openssl_version_text() {
        return libtorrent_jni.openssl_version_text();
    }

    public static void set_posix_wrapper(posix_wrapper posix_wrapper) {
        libtorrent_jni.set_posix_wrapper(posix_wrapper.getCPtr(posix_wrapper), posix_wrapper);
    }
}
