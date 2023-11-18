package com.frostwire.jlibtorrent.swig;

import org.mozilla.universalchardet.prober.CharsetProber;

public class settings_pack {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class bandwidth_mixed_algo_t {
        public static final bandwidth_mixed_algo_t peer_proportional = new bandwidth_mixed_algo_t("peer_proportional", libtorrent_jni.settings_pack_peer_proportional_get());
        public static final bandwidth_mixed_algo_t prefer_tcp = new bandwidth_mixed_algo_t("prefer_tcp", libtorrent_jni.settings_pack_prefer_tcp_get());
        private static int swigNext;
        private static bandwidth_mixed_algo_t[] swigValues = new bandwidth_mixed_algo_t[]{prefer_tcp, peer_proportional};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static bandwidth_mixed_algo_t swigToEnum(int i) {
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
            stringBuilder.append(bandwidth_mixed_algo_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private bandwidth_mixed_algo_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private bandwidth_mixed_algo_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private bandwidth_mixed_algo_t(String str, bandwidth_mixed_algo_t bandwidth_mixed_algo_t) {
            this.swigName = str;
            this.swigValue = bandwidth_mixed_algo_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class bool_types {
        public static final bool_types allow_i2p_mixed = new bool_types("allow_i2p_mixed");
        public static final bool_types allow_multiple_connections_per_ip = new bool_types("allow_multiple_connections_per_ip", libtorrent_jni.settings_pack_allow_multiple_connections_per_ip_get());
        public static final bool_types allow_partial_disk_writes = new bool_types("allow_partial_disk_writes");
        public static final bool_types always_send_user_agent = new bool_types("always_send_user_agent");
        public static final bool_types announce_crypto_support = new bool_types("announce_crypto_support", libtorrent_jni.settings_pack_announce_crypto_support_get());
        public static final bool_types announce_to_all_tiers = new bool_types("announce_to_all_tiers");
        public static final bool_types announce_to_all_trackers = new bool_types("announce_to_all_trackers");
        public static final bool_types anonymous_mode = new bool_types("anonymous_mode");
        public static final bool_types apply_ip_filter_to_trackers = new bool_types("apply_ip_filter_to_trackers");
        public static final bool_types auto_manage_prefer_seeds = new bool_types("auto_manage_prefer_seeds");
        public static final bool_types auto_sequential = new bool_types("auto_sequential");
        public static final bool_types ban_web_seeds = new bool_types("ban_web_seeds", libtorrent_jni.settings_pack_ban_web_seeds_get());
        public static final bool_types broadcast_lsd = new bool_types("broadcast_lsd");
        public static final bool_types close_redundant_connections = new bool_types("close_redundant_connections");
        public static final bool_types coalesce_reads = new bool_types("coalesce_reads", libtorrent_jni.settings_pack_coalesce_reads_get());
        public static final bool_types coalesce_writes = new bool_types("coalesce_writes");
        public static final bool_types disable_hash_checks = new bool_types("disable_hash_checks", libtorrent_jni.settings_pack_disable_hash_checks_get());
        public static final bool_types dont_count_slow_torrents = new bool_types("dont_count_slow_torrents");
        public static final bool_types enable_dht = new bool_types("enable_dht");
        public static final bool_types enable_incoming_tcp = new bool_types("enable_incoming_tcp");
        public static final bool_types enable_incoming_utp = new bool_types("enable_incoming_utp");
        public static final bool_types enable_lsd = new bool_types("enable_lsd");
        public static final bool_types enable_natpmp = new bool_types("enable_natpmp");
        public static final bool_types enable_outgoing_tcp = new bool_types("enable_outgoing_tcp");
        public static final bool_types enable_outgoing_utp = new bool_types("enable_outgoing_utp");
        public static final bool_types enable_upnp = new bool_types("enable_upnp");
        public static final bool_types force_proxy = new bool_types("force_proxy");
        public static final bool_types incoming_starts_queued_torrents = new bool_types("incoming_starts_queued_torrents");
        public static final bool_types listen_system_port_fallback = new bool_types("listen_system_port_fallback");
        public static final bool_types lock_files = new bool_types("lock_files", libtorrent_jni.settings_pack_lock_files_get());
        public static final bool_types max_bool_setting_internal = new bool_types("max_bool_setting_internal");
        public static final bool_types no_atime_storage = new bool_types("no_atime_storage", libtorrent_jni.settings_pack_no_atime_storage_get());
        public static final bool_types no_connect_privileged_ports = new bool_types("no_connect_privileged_ports");
        public static final bool_types no_recheck_incomplete_resume = new bool_types("no_recheck_incomplete_resume", libtorrent_jni.settings_pack_no_recheck_incomplete_resume_get());
        public static final bool_types prefer_rc4 = new bool_types("prefer_rc4");
        public static final bool_types prefer_udp_trackers = new bool_types("prefer_udp_trackers");
        public static final bool_types prioritize_partial_pieces = new bool_types("prioritize_partial_pieces");
        public static final bool_types proxy_hostnames = new bool_types("proxy_hostnames");
        public static final bool_types proxy_peer_connections = new bool_types("proxy_peer_connections");
        public static final bool_types proxy_tracker_connections = new bool_types("proxy_tracker_connections");
        public static final bool_types rate_limit_ip_overhead = new bool_types("rate_limit_ip_overhead");
        public static final bool_types report_redundant_bytes = new bool_types("report_redundant_bytes");
        public static final bool_types report_true_downloaded = new bool_types("report_true_downloaded");
        public static final bool_types report_web_seed_downloads = new bool_types("report_web_seed_downloads");
        public static final bool_types seeding_outgoing_connections = new bool_types("seeding_outgoing_connections", libtorrent_jni.settings_pack_seeding_outgoing_connections_get());
        public static final bool_types send_redundant_have = new bool_types("send_redundant_have", libtorrent_jni.settings_pack_send_redundant_have_get());
        public static final bool_types smooth_connects = new bool_types("smooth_connects");
        public static final bool_types strict_end_game_mode = new bool_types("strict_end_game_mode");
        public static final bool_types strict_super_seeding = new bool_types("strict_super_seeding");
        public static final bool_types support_merkle_torrents = new bool_types("support_merkle_torrents");
        public static final bool_types support_share_mode = new bool_types("support_share_mode");
        private static int swigNext;
        private static bool_types[] swigValues = new bool_types[]{allow_multiple_connections_per_ip, send_redundant_have, use_dht_as_fallback, upnp_ignore_nonrouters, use_parole_mode, use_read_cache, coalesce_reads, coalesce_writes, auto_manage_prefer_seeds, dont_count_slow_torrents, close_redundant_connections, prioritize_partial_pieces, rate_limit_ip_overhead, announce_to_all_tiers, announce_to_all_trackers, prefer_udp_trackers, strict_super_seeding, disable_hash_checks, allow_i2p_mixed, volatile_read_cache, no_atime_storage, incoming_starts_queued_torrents, report_true_downloaded, strict_end_game_mode, broadcast_lsd, enable_outgoing_utp, enable_incoming_utp, enable_outgoing_tcp, enable_incoming_tcp, no_recheck_incomplete_resume, anonymous_mode, report_web_seed_downloads, seeding_outgoing_connections, no_connect_privileged_ports, smooth_connects, always_send_user_agent, apply_ip_filter_to_trackers, lock_files, ban_web_seeds, allow_partial_disk_writes, force_proxy, support_share_mode, support_merkle_torrents, report_redundant_bytes, listen_system_port_fallback, announce_crypto_support, enable_upnp, enable_natpmp, enable_lsd, enable_dht, prefer_rc4, proxy_hostnames, proxy_peer_connections, auto_sequential, proxy_tracker_connections, max_bool_setting_internal};
        public static final bool_types upnp_ignore_nonrouters = new bool_types("upnp_ignore_nonrouters");
        public static final bool_types use_dht_as_fallback = new bool_types("use_dht_as_fallback", libtorrent_jni.settings_pack_use_dht_as_fallback_get());
        public static final bool_types use_parole_mode = new bool_types("use_parole_mode");
        public static final bool_types use_read_cache = new bool_types("use_read_cache");
        public static final bool_types volatile_read_cache = new bool_types("volatile_read_cache", libtorrent_jni.settings_pack_volatile_read_cache_get());
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static bool_types swigToEnum(int i) {
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
            stringBuilder.append(bool_types.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private bool_types(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private bool_types(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private bool_types(String str, bool_types bool_types) {
            this.swigName = str;
            this.swigValue = bool_types.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class choking_algorithm_t {
        public static final choking_algorithm_t bittyrant_choker = new choking_algorithm_t("bittyrant_choker", libtorrent_jni.settings_pack_bittyrant_choker_get());
        public static final choking_algorithm_t fixed_slots_choker = new choking_algorithm_t("fixed_slots_choker", libtorrent_jni.settings_pack_fixed_slots_choker_get());
        public static final choking_algorithm_t rate_based_choker = new choking_algorithm_t("rate_based_choker", libtorrent_jni.settings_pack_rate_based_choker_get());
        private static int swigNext;
        private static choking_algorithm_t[] swigValues = new choking_algorithm_t[]{fixed_slots_choker, rate_based_choker, bittyrant_choker};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static choking_algorithm_t swigToEnum(int i) {
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
            stringBuilder.append(choking_algorithm_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private choking_algorithm_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private choking_algorithm_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private choking_algorithm_t(String str, choking_algorithm_t choking_algorithm_t) {
            this.swigName = str;
            this.swigValue = choking_algorithm_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class enc_level {
        public static final enc_level pe_both = new enc_level("pe_both", libtorrent_jni.settings_pack_pe_both_get());
        public static final enc_level pe_plaintext = new enc_level("pe_plaintext", libtorrent_jni.settings_pack_pe_plaintext_get());
        public static final enc_level pe_rc4 = new enc_level("pe_rc4", libtorrent_jni.settings_pack_pe_rc4_get());
        private static int swigNext;
        private static enc_level[] swigValues = new enc_level[]{pe_plaintext, pe_rc4, pe_both};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static enc_level swigToEnum(int i) {
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
            stringBuilder.append(enc_level.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private enc_level(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private enc_level(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private enc_level(String str, enc_level enc_level) {
            this.swigName = str;
            this.swigValue = enc_level.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class enc_policy {
        public static final enc_policy pe_disabled = new enc_policy("pe_disabled");
        public static final enc_policy pe_enabled = new enc_policy("pe_enabled");
        public static final enc_policy pe_forced = new enc_policy("pe_forced");
        private static int swigNext;
        private static enc_policy[] swigValues = new enc_policy[]{pe_forced, pe_enabled, pe_disabled};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static enc_policy swigToEnum(int i) {
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
            stringBuilder.append(enc_policy.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private enc_policy(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private enc_policy(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private enc_policy(String str, enc_policy enc_policy) {
            this.swigName = str;
            this.swigValue = enc_policy.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class int_types {
        public static final int_types active_checking = new int_types("active_checking");
        public static final int_types active_dht_limit = new int_types("active_dht_limit");
        public static final int_types active_downloads = new int_types("active_downloads");
        public static final int_types active_limit = new int_types("active_limit");
        public static final int_types active_lsd_limit = new int_types("active_lsd_limit");
        public static final int_types active_seeds = new int_types("active_seeds");
        public static final int_types active_tracker_limit = new int_types("active_tracker_limit");
        public static final int_types aio_max = new int_types("aio_max");
        public static final int_types aio_threads = new int_types("aio_threads");
        public static final int_types alert_mask = new int_types("alert_mask");
        public static final int_types alert_queue_size = new int_types("alert_queue_size");
        public static final int_types allowed_enc_level = new int_types("allowed_enc_level");
        public static final int_types allowed_fast_set_size = new int_types("allowed_fast_set_size");
        public static final int_types auto_manage_interval = new int_types("auto_manage_interval", libtorrent_jni.settings_pack_auto_manage_interval_get());
        public static final int_types auto_manage_startup = new int_types("auto_manage_startup");
        public static final int_types auto_scrape_interval = new int_types("auto_scrape_interval");
        public static final int_types auto_scrape_min_interval = new int_types("auto_scrape_min_interval");
        public static final int_types cache_expiry = new int_types("cache_expiry", libtorrent_jni.settings_pack_cache_expiry_get());
        public static final int_types cache_size = new int_types("cache_size");
        public static final int_types cache_size_volatile = new int_types("cache_size_volatile");
        public static final int_types checking_mem_usage = new int_types("checking_mem_usage", libtorrent_jni.settings_pack_checking_mem_usage_get());
        public static final int_types choking_algorithm = new int_types("choking_algorithm");
        public static final int_types close_file_interval = new int_types("close_file_interval");
        public static final int_types connect_seed_every_n_download = new int_types("connect_seed_every_n_download");
        public static final int_types connection_speed = new int_types("connection_speed");
        public static final int_types connections_limit = new int_types("connections_limit", libtorrent_jni.settings_pack_connections_limit_get());
        public static final int_types connections_slack = new int_types("connections_slack");
        public static final int_types decrease_est_reciprocation_rate = new int_types("decrease_est_reciprocation_rate");
        public static final int_types default_est_reciprocation_rate = new int_types("default_est_reciprocation_rate");
        public static final int_types dht_announce_interval = new int_types("dht_announce_interval");
        public static final int_types disk_io_read_mode = new int_types("disk_io_read_mode");
        public static final int_types disk_io_write_mode = new int_types("disk_io_write_mode");
        public static final int_types download_rate_limit = new int_types("download_rate_limit");
        public static final int_types file_pool_size = new int_types("file_pool_size");
        public static final int_types handshake_timeout = new int_types("handshake_timeout");
        public static final int_types i2p_port = new int_types("i2p_port");
        public static final int_types in_enc_policy = new int_types("in_enc_policy");
        public static final int_types inactive_down_rate = new int_types("inactive_down_rate");
        public static final int_types inactive_up_rate = new int_types("inactive_up_rate");
        public static final int_types inactivity_timeout = new int_types("inactivity_timeout");
        public static final int_types increase_est_reciprocation_rate = new int_types("increase_est_reciprocation_rate");
        public static final int_types initial_picker_threshold = new int_types("initial_picker_threshold");
        public static final int_types listen_queue_size = new int_types("listen_queue_size");
        public static final int_types local_service_announce_interval = new int_types("local_service_announce_interval");
        public static final int_types max_allowed_in_request_queue = new int_types("max_allowed_in_request_queue");
        public static final int_types max_failcount = new int_types("max_failcount");
        public static final int_types max_http_recv_buffer_size = new int_types("max_http_recv_buffer_size");
        public static final int_types max_int_setting_internal = new int_types("max_int_setting_internal");
        public static final int_types max_metadata_size = new int_types("max_metadata_size");
        public static final int_types max_out_request_queue = new int_types("max_out_request_queue");
        public static final int_types max_paused_peerlist_size = new int_types("max_paused_peerlist_size");
        public static final int_types max_peer_recv_buffer_size = new int_types("max_peer_recv_buffer_size");
        public static final int_types max_peerlist_size = new int_types("max_peerlist_size");
        public static final int_types max_pex_peers = new int_types("max_pex_peers");
        public static final int_types max_queued_disk_bytes = new int_types("max_queued_disk_bytes");
        public static final int_types max_rejects = new int_types("max_rejects");
        public static final int_types max_retry_port_bind = new int_types("max_retry_port_bind");
        public static final int_types max_suggest_pieces = new int_types("max_suggest_pieces");
        public static final int_types max_web_seed_connections = new int_types("max_web_seed_connections");
        public static final int_types min_announce_interval = new int_types("min_announce_interval");
        public static final int_types min_reconnect_time = new int_types("min_reconnect_time");
        public static final int_types mixed_mode_algorithm = new int_types("mixed_mode_algorithm");
        public static final int_types network_threads = new int_types("network_threads");
        public static final int_types num_optimistic_unchoke_slots = new int_types("num_optimistic_unchoke_slots", libtorrent_jni.settings_pack_num_optimistic_unchoke_slots_get());
        public static final int_types num_outgoing_ports = new int_types("num_outgoing_ports");
        public static final int_types num_want = new int_types("num_want");
        public static final int_types optimistic_disk_retry = new int_types("optimistic_disk_retry");
        public static final int_types optimistic_unchoke_interval = new int_types("optimistic_unchoke_interval");
        public static final int_types out_enc_policy = new int_types("out_enc_policy");
        public static final int_types outgoing_port = new int_types("outgoing_port");
        public static final int_types peer_connect_timeout = new int_types("peer_connect_timeout");
        public static final int_types peer_timeout = new int_types("peer_timeout");
        public static final int_types peer_tos = new int_types("peer_tos");
        public static final int_types peer_turnover = new int_types("peer_turnover");
        public static final int_types peer_turnover_cutoff = new int_types("peer_turnover_cutoff");
        public static final int_types peer_turnover_interval = new int_types("peer_turnover_interval");
        public static final int_types piece_timeout = new int_types("piece_timeout");
        public static final int_types predictive_piece_announce = new int_types("predictive_piece_announce");
        public static final int_types proxy_port = new int_types("proxy_port");
        public static final int_types proxy_type = new int_types("proxy_type");
        public static final int_types read_cache_line_size = new int_types("read_cache_line_size", libtorrent_jni.settings_pack_read_cache_line_size_get());
        public static final int_types recv_socket_buffer_size = new int_types("recv_socket_buffer_size");
        public static final int_types request_queue_time = new int_types("request_queue_time");
        public static final int_types request_timeout = new int_types("request_timeout");
        public static final int_types resolver_cache_timeout = new int_types("resolver_cache_timeout");
        public static final int_types seed_choking_algorithm = new int_types("seed_choking_algorithm");
        public static final int_types seed_time_limit = new int_types("seed_time_limit");
        public static final int_types seed_time_ratio_limit = new int_types("seed_time_ratio_limit");
        public static final int_types seeding_piece_quota = new int_types("seeding_piece_quota");
        public static final int_types send_buffer_low_watermark = new int_types("send_buffer_low_watermark");
        public static final int_types send_buffer_watermark = new int_types("send_buffer_watermark");
        public static final int_types send_buffer_watermark_factor = new int_types("send_buffer_watermark_factor");
        public static final int_types send_socket_buffer_size = new int_types("send_socket_buffer_size");
        public static final int_types share_mode_target = new int_types("share_mode_target");
        public static final int_types share_ratio_limit = new int_types("share_ratio_limit");
        public static final int_types stop_tracker_timeout = new int_types("stop_tracker_timeout");
        public static final int_types suggest_mode = new int_types("suggest_mode");
        private static int swigNext;
        private static int_types[] swigValues;
        public static final int_types tick_interval = new int_types("tick_interval");
        public static final int_types torrent_connect_boost = new int_types("torrent_connect_boost");
        public static final int_types tracker_backoff = new int_types("tracker_backoff", libtorrent_jni.settings_pack_tracker_backoff_get());
        public static final int_types tracker_completion_timeout = new int_types("tracker_completion_timeout", libtorrent_jni.settings_pack_tracker_completion_timeout_get());
        public static final int_types tracker_maximum_response_length = new int_types("tracker_maximum_response_length");
        public static final int_types tracker_receive_timeout = new int_types("tracker_receive_timeout");
        public static final int_types udp_tracker_token_expiry = new int_types("udp_tracker_token_expiry");
        public static final int_types unchoke_interval = new int_types("unchoke_interval");
        public static final int_types unchoke_slots_limit = new int_types("unchoke_slots_limit", libtorrent_jni.settings_pack_unchoke_slots_limit_get());
        public static final int_types upload_rate_limit = new int_types("upload_rate_limit");
        public static final int_types urlseed_max_request_bytes = new int_types("urlseed_max_request_bytes");
        public static final int_types urlseed_pipeline_size = new int_types("urlseed_pipeline_size");
        public static final int_types urlseed_timeout = new int_types("urlseed_timeout");
        public static final int_types urlseed_wait_retry = new int_types("urlseed_wait_retry");
        public static final int_types utp_connect_timeout = new int_types("utp_connect_timeout");
        public static final int_types utp_fin_resends = new int_types("utp_fin_resends");
        public static final int_types utp_gain_factor = new int_types("utp_gain_factor");
        public static final int_types utp_loss_multiplier = new int_types("utp_loss_multiplier", libtorrent_jni.settings_pack_utp_loss_multiplier_get());
        public static final int_types utp_min_timeout = new int_types("utp_min_timeout");
        public static final int_types utp_num_resends = new int_types("utp_num_resends");
        public static final int_types utp_syn_resends = new int_types("utp_syn_resends");
        public static final int_types utp_target_delay = new int_types("utp_target_delay");
        public static final int_types web_seed_name_lookup_retry = new int_types("web_seed_name_lookup_retry");
        public static final int_types whole_pieces_threshold = new int_types("whole_pieces_threshold");
        public static final int_types write_cache_line_size = new int_types("write_cache_line_size");
        private final String swigName;
        private final int swigValue;

        static {
            int_types[] int_typesArr = new int_types[CharsetProber.ASCII_Z];
            int_typesArr[0] = tracker_completion_timeout;
            int_typesArr[1] = tracker_receive_timeout;
            int_typesArr[2] = stop_tracker_timeout;
            int_typesArr[3] = tracker_maximum_response_length;
            int_typesArr[4] = piece_timeout;
            int_typesArr[5] = request_timeout;
            int_typesArr[6] = request_queue_time;
            int_typesArr[7] = max_allowed_in_request_queue;
            int_typesArr[8] = max_out_request_queue;
            int_typesArr[9] = whole_pieces_threshold;
            int_typesArr[10] = peer_timeout;
            int_typesArr[11] = urlseed_timeout;
            int_typesArr[12] = urlseed_pipeline_size;
            int_typesArr[13] = urlseed_wait_retry;
            int_typesArr[14] = file_pool_size;
            int_typesArr[15] = max_failcount;
            int_typesArr[16] = min_reconnect_time;
            int_typesArr[17] = peer_connect_timeout;
            int_typesArr[18] = connection_speed;
            int_typesArr[19] = inactivity_timeout;
            int_typesArr[20] = unchoke_interval;
            int_typesArr[21] = optimistic_unchoke_interval;
            int_typesArr[22] = num_want;
            int_typesArr[23] = initial_picker_threshold;
            int_typesArr[24] = allowed_fast_set_size;
            int_typesArr[25] = suggest_mode;
            int_typesArr[26] = max_queued_disk_bytes;
            int_typesArr[27] = handshake_timeout;
            int_typesArr[28] = send_buffer_low_watermark;
            int_typesArr[29] = send_buffer_watermark;
            int_typesArr[30] = send_buffer_watermark_factor;
            int_typesArr[31] = choking_algorithm;
            int_typesArr[32] = seed_choking_algorithm;
            int_typesArr[33] = cache_size;
            int_typesArr[34] = cache_expiry;
            int_typesArr[35] = disk_io_write_mode;
            int_typesArr[36] = disk_io_read_mode;
            int_typesArr[37] = outgoing_port;
            int_typesArr[38] = num_outgoing_ports;
            int_typesArr[39] = peer_tos;
            int_typesArr[40] = active_downloads;
            int_typesArr[41] = active_seeds;
            int_typesArr[42] = active_checking;
            int_typesArr[43] = active_dht_limit;
            int_typesArr[44] = active_tracker_limit;
            int_typesArr[45] = active_lsd_limit;
            int_typesArr[46] = active_limit;
            int_typesArr[47] = auto_manage_interval;
            int_typesArr[48] = seed_time_limit;
            int_typesArr[49] = auto_scrape_interval;
            int_typesArr[50] = auto_scrape_min_interval;
            int_typesArr[51] = max_peerlist_size;
            int_typesArr[52] = max_paused_peerlist_size;
            int_typesArr[53] = min_announce_interval;
            int_typesArr[54] = auto_manage_startup;
            int_typesArr[55] = seeding_piece_quota;
            int_typesArr[56] = max_rejects;
            int_typesArr[57] = recv_socket_buffer_size;
            int_typesArr[58] = send_socket_buffer_size;
            int_typesArr[59] = max_peer_recv_buffer_size;
            int_typesArr[60] = read_cache_line_size;
            int_typesArr[61] = write_cache_line_size;
            int_typesArr[62] = optimistic_disk_retry;
            int_typesArr[63] = max_suggest_pieces;
            int_typesArr[64] = local_service_announce_interval;
            int_typesArr[65] = dht_announce_interval;
            int_typesArr[66] = udp_tracker_token_expiry;
            int_typesArr[67] = num_optimistic_unchoke_slots;
            int_typesArr[68] = default_est_reciprocation_rate;
            int_typesArr[69] = increase_est_reciprocation_rate;
            int_typesArr[70] = decrease_est_reciprocation_rate;
            int_typesArr[71] = max_pex_peers;
            int_typesArr[72] = tick_interval;
            int_typesArr[73] = share_mode_target;
            int_typesArr[74] = upload_rate_limit;
            int_typesArr[75] = download_rate_limit;
            int_typesArr[76] = unchoke_slots_limit;
            int_typesArr[77] = connections_limit;
            int_typesArr[78] = connections_slack;
            int_typesArr[79] = utp_target_delay;
            int_typesArr[80] = utp_gain_factor;
            int_typesArr[81] = utp_min_timeout;
            int_typesArr[82] = utp_syn_resends;
            int_typesArr[83] = utp_fin_resends;
            int_typesArr[84] = utp_num_resends;
            int_typesArr[85] = utp_connect_timeout;
            int_typesArr[86] = utp_loss_multiplier;
            int_typesArr[87] = mixed_mode_algorithm;
            int_typesArr[88] = listen_queue_size;
            int_typesArr[89] = torrent_connect_boost;
            int_typesArr[90] = alert_queue_size;
            int_typesArr[91] = max_metadata_size;
            int_typesArr[92] = checking_mem_usage;
            int_typesArr[93] = predictive_piece_announce;
            int_typesArr[94] = aio_threads;
            int_typesArr[95] = aio_max;
            int_typesArr[96] = network_threads;
            int_typesArr[97] = tracker_backoff;
            int_typesArr[98] = share_ratio_limit;
            int_typesArr[99] = seed_time_ratio_limit;
            int_typesArr[100] = peer_turnover;
            int_typesArr[101] = peer_turnover_cutoff;
            int_typesArr[102] = peer_turnover_interval;
            int_typesArr[103] = connect_seed_every_n_download;
            int_typesArr[104] = max_http_recv_buffer_size;
            int_typesArr[105] = max_retry_port_bind;
            int_typesArr[106] = alert_mask;
            int_typesArr[107] = out_enc_policy;
            int_typesArr[108] = in_enc_policy;
            int_typesArr[109] = allowed_enc_level;
            int_typesArr[110] = inactive_down_rate;
            int_typesArr[111] = inactive_up_rate;
            int_typesArr[112] = proxy_type;
            int_typesArr[113] = proxy_port;
            int_typesArr[114] = i2p_port;
            int_typesArr[115] = cache_size_volatile;
            int_typesArr[116] = urlseed_max_request_bytes;
            int_typesArr[117] = web_seed_name_lookup_retry;
            int_typesArr[118] = close_file_interval;
            int_typesArr[119] = max_web_seed_connections;
            int_typesArr[120] = resolver_cache_timeout;
            int_typesArr[121] = max_int_setting_internal;
            swigValues = int_typesArr;
        }

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static int_types swigToEnum(int i) {
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
            stringBuilder.append(int_types.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private int_types(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private int_types(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private int_types(String str, int_types int_types) {
            this.swigName = str;
            this.swigValue = int_types.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class io_buffer_mode_t {
        public static final io_buffer_mode_t disable_os_cache = new io_buffer_mode_t("disable_os_cache", libtorrent_jni.settings_pack_disable_os_cache_get());
        public static final io_buffer_mode_t enable_os_cache = new io_buffer_mode_t("enable_os_cache", libtorrent_jni.settings_pack_enable_os_cache_get());
        private static int swigNext;
        private static io_buffer_mode_t[] swigValues = new io_buffer_mode_t[]{enable_os_cache, disable_os_cache};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static io_buffer_mode_t swigToEnum(int i) {
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
            stringBuilder.append(io_buffer_mode_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private io_buffer_mode_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private io_buffer_mode_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private io_buffer_mode_t(String str, io_buffer_mode_t io_buffer_mode_t) {
            this.swigName = str;
            this.swigValue = io_buffer_mode_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class proxy_type_t {
        public static final proxy_type_t http = new proxy_type_t("http");
        public static final proxy_type_t http_pw = new proxy_type_t("http_pw");
        public static final proxy_type_t i2p_proxy = new proxy_type_t("i2p_proxy");
        public static final proxy_type_t none = new proxy_type_t("none");
        public static final proxy_type_t socks4 = new proxy_type_t("socks4");
        public static final proxy_type_t socks5 = new proxy_type_t("socks5");
        public static final proxy_type_t socks5_pw = new proxy_type_t("socks5_pw");
        private static int swigNext;
        private static proxy_type_t[] swigValues = new proxy_type_t[]{none, socks4, socks5, socks5_pw, http, http_pw, i2p_proxy};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static proxy_type_t swigToEnum(int i) {
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
            stringBuilder.append(proxy_type_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private proxy_type_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private proxy_type_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private proxy_type_t(String str, proxy_type_t proxy_type_t) {
            this.swigName = str;
            this.swigValue = proxy_type_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class seed_choking_algorithm_t {
        public static final seed_choking_algorithm_t anti_leech = new seed_choking_algorithm_t("anti_leech");
        public static final seed_choking_algorithm_t fastest_upload = new seed_choking_algorithm_t("fastest_upload");
        public static final seed_choking_algorithm_t round_robin = new seed_choking_algorithm_t("round_robin");
        private static int swigNext;
        private static seed_choking_algorithm_t[] swigValues = new seed_choking_algorithm_t[]{round_robin, fastest_upload, anti_leech};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static seed_choking_algorithm_t swigToEnum(int i) {
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
            stringBuilder.append(seed_choking_algorithm_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private seed_choking_algorithm_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private seed_choking_algorithm_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private seed_choking_algorithm_t(String str, seed_choking_algorithm_t seed_choking_algorithm_t) {
            this.swigName = str;
            this.swigValue = seed_choking_algorithm_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class settings_counts_t {
        public static final settings_counts_t num_bool_settings = new settings_counts_t("num_bool_settings", libtorrent_jni.settings_pack_num_bool_settings_get());
        public static final settings_counts_t num_int_settings = new settings_counts_t("num_int_settings", libtorrent_jni.settings_pack_num_int_settings_get());
        public static final settings_counts_t num_string_settings = new settings_counts_t("num_string_settings", libtorrent_jni.settings_pack_num_string_settings_get());
        private static int swigNext;
        private static settings_counts_t[] swigValues = new settings_counts_t[]{num_string_settings, num_bool_settings, num_int_settings};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static settings_counts_t swigToEnum(int i) {
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
            stringBuilder.append(settings_counts_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private settings_counts_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private settings_counts_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private settings_counts_t(String str, settings_counts_t settings_counts_t) {
            this.swigName = str;
            this.swigValue = settings_counts_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class string_types {
        public static final string_types announce_ip = new string_types("announce_ip");
        public static final string_types dht_bootstrap_nodes = new string_types("dht_bootstrap_nodes");
        public static final string_types handshake_client_version = new string_types("handshake_client_version", libtorrent_jni.settings_pack_handshake_client_version_get());
        public static final string_types i2p_hostname = new string_types("i2p_hostname");
        public static final string_types listen_interfaces = new string_types("listen_interfaces");
        public static final string_types max_string_setting_internal = new string_types("max_string_setting_internal");
        public static final string_types outgoing_interfaces = new string_types("outgoing_interfaces");
        public static final string_types peer_fingerprint = new string_types("peer_fingerprint");
        public static final string_types proxy_hostname = new string_types("proxy_hostname");
        public static final string_types proxy_password = new string_types("proxy_password");
        public static final string_types proxy_username = new string_types("proxy_username");
        private static int swigNext;
        private static string_types[] swigValues = new string_types[]{user_agent, announce_ip, handshake_client_version, outgoing_interfaces, listen_interfaces, proxy_hostname, proxy_username, proxy_password, i2p_hostname, peer_fingerprint, dht_bootstrap_nodes, max_string_setting_internal};
        public static final string_types user_agent = new string_types("user_agent", libtorrent_jni.settings_pack_user_agent_get());
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static string_types swigToEnum(int i) {
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
            stringBuilder.append(string_types.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private string_types(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private string_types(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private string_types(String str, string_types string_types) {
            this.swigName = str;
            this.swigValue = string_types.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class suggest_mode_t {
        public static final suggest_mode_t no_piece_suggestions = new suggest_mode_t("no_piece_suggestions", libtorrent_jni.settings_pack_no_piece_suggestions_get());
        public static final suggest_mode_t suggest_read_cache = new suggest_mode_t("suggest_read_cache", libtorrent_jni.settings_pack_suggest_read_cache_get());
        private static int swigNext;
        private static suggest_mode_t[] swigValues = new suggest_mode_t[]{no_piece_suggestions, suggest_read_cache};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static suggest_mode_t swigToEnum(int i) {
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
            stringBuilder.append(suggest_mode_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private suggest_mode_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private suggest_mode_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private suggest_mode_t(String str, suggest_mode_t suggest_mode_t) {
            this.swigName = str;
            this.swigValue = suggest_mode_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    public static final class type_bases {
        public static final type_bases bool_type_base = new type_bases("bool_type_base", libtorrent_jni.settings_pack_bool_type_base_get());
        public static final type_bases index_mask = new type_bases("index_mask", libtorrent_jni.settings_pack_index_mask_get());
        public static final type_bases int_type_base = new type_bases("int_type_base", libtorrent_jni.settings_pack_int_type_base_get());
        public static final type_bases string_type_base = new type_bases("string_type_base", libtorrent_jni.settings_pack_string_type_base_get());
        private static int swigNext;
        private static type_bases[] swigValues = new type_bases[]{string_type_base, int_type_base, bool_type_base, type_mask, index_mask};
        public static final type_bases type_mask = new type_bases("type_mask", libtorrent_jni.settings_pack_type_mask_get());
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static type_bases swigToEnum(int i) {
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
            stringBuilder.append(type_bases.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private type_bases(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private type_bases(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private type_bases(String str, type_bases type_bases) {
            this.swigName = str;
            this.swigValue = type_bases.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected settings_pack(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(settings_pack settings_pack) {
        return settings_pack == null ? 0 : settings_pack.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_settings_pack(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public settings_pack() {
        this(libtorrent_jni.new_settings_pack__SWIG_0(), true);
    }

    public settings_pack(settings_pack settings_pack) {
        this(libtorrent_jni.new_settings_pack__SWIG_1(getCPtr(settings_pack), settings_pack), true);
    }

    public void set_str(int i, String str) {
        libtorrent_jni.settings_pack_set_str(this.swigCPtr, this, i, str);
    }

    public void set_int(int i, int i2) {
        libtorrent_jni.settings_pack_set_int(this.swigCPtr, this, i, i2);
    }

    public void set_bool(int i, boolean z) {
        libtorrent_jni.settings_pack_set_bool(this.swigCPtr, this, i, z);
    }

    public boolean has_val(int i) {
        return libtorrent_jni.settings_pack_has_val(this.swigCPtr, this, i);
    }

    public void clear() {
        libtorrent_jni.settings_pack_clear__SWIG_0(this.swigCPtr, this);
    }

    public void clear(int i) {
        libtorrent_jni.settings_pack_clear__SWIG_1(this.swigCPtr, this, i);
    }

    public String get_str(int i) {
        return libtorrent_jni.settings_pack_get_str(this.swigCPtr, this, i);
    }

    public int get_int(int i) {
        return libtorrent_jni.settings_pack_get_int(this.swigCPtr, this, i);
    }

    public boolean get_bool(int i) {
        return libtorrent_jni.settings_pack_get_bool(this.swigCPtr, this, i);
    }
}
