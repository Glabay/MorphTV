package com.frostwire.jlibtorrent.swig;

public class alert {
    public static final alert_category_t all_categories = new alert_category_t(libtorrent_jni.alert_all_categories_get(), false);
    public static final alert_category_t debug_notification = new alert_category_t(libtorrent_jni.alert_debug_notification_get(), false);
    public static final alert_category_t dht_log_notification = new alert_category_t(libtorrent_jni.alert_dht_log_notification_get(), false);
    public static final alert_category_t dht_notification = new alert_category_t(libtorrent_jni.alert_dht_notification_get(), false);
    public static final alert_category_t dht_operation_notification = new alert_category_t(libtorrent_jni.alert_dht_operation_notification_get(), false);
    public static final alert_category_t error_notification = new alert_category_t(libtorrent_jni.alert_error_notification_get(), false);
    public static final alert_category_t incoming_request_notification = new alert_category_t(libtorrent_jni.alert_incoming_request_notification_get(), false);
    public static final alert_category_t ip_block_notification = new alert_category_t(libtorrent_jni.alert_ip_block_notification_get(), false);
    public static final alert_category_t peer_log_notification = new alert_category_t(libtorrent_jni.alert_peer_log_notification_get(), false);
    public static final alert_category_t peer_notification = new alert_category_t(libtorrent_jni.alert_peer_notification_get(), false);
    public static final alert_category_t performance_warning = new alert_category_t(libtorrent_jni.alert_performance_warning_get(), false);
    public static final alert_category_t picker_log_notification = new alert_category_t(libtorrent_jni.alert_picker_log_notification_get(), false);
    public static final alert_category_t port_mapping_log_notification = new alert_category_t(libtorrent_jni.alert_port_mapping_log_notification_get(), false);
    public static final alert_category_t port_mapping_notification = new alert_category_t(libtorrent_jni.alert_port_mapping_notification_get(), false);
    public static final alert_category_t progress_notification = new alert_category_t(libtorrent_jni.alert_progress_notification_get(), false);
    public static final alert_category_t session_log_notification = new alert_category_t(libtorrent_jni.alert_session_log_notification_get(), false);
    public static final alert_category_t stats_notification = new alert_category_t(libtorrent_jni.alert_stats_notification_get(), false);
    public static final alert_category_t status_notification = new alert_category_t(libtorrent_jni.alert_status_notification_get(), false);
    public static final alert_category_t storage_notification = new alert_category_t(libtorrent_jni.alert_storage_notification_get(), false);
    public static final alert_category_t torrent_log_notification = new alert_category_t(libtorrent_jni.alert_torrent_log_notification_get(), false);
    public static final alert_category_t tracker_notification = new alert_category_t(libtorrent_jni.alert_tracker_notification_get(), false);
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected alert(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(alert alert) {
        return alert == null ? 0 : alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public int type() {
        return libtorrent_jni.alert_type(this.swigCPtr, this);
    }

    public String what() {
        return libtorrent_jni.alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.alert_message(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.alert_category(this.swigCPtr, this), true);
    }

    public static torrent_alert cast_to_torrent_alert(alert alert) {
        long alert_cast_to_torrent_alert = libtorrent_jni.alert_cast_to_torrent_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_alert == 0) {
            return null;
        }
        return new torrent_alert(alert_cast_to_torrent_alert, false);
    }

    public static peer_alert cast_to_peer_alert(alert alert) {
        long alert_cast_to_peer_alert = libtorrent_jni.alert_cast_to_peer_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_alert == 0) {
            return null;
        }
        return new peer_alert(alert_cast_to_peer_alert, false);
    }

    public static tracker_alert cast_to_tracker_alert(alert alert) {
        long alert_cast_to_tracker_alert = libtorrent_jni.alert_cast_to_tracker_alert(getCPtr(alert), alert);
        if (alert_cast_to_tracker_alert == 0) {
            return null;
        }
        return new tracker_alert(alert_cast_to_tracker_alert, false);
    }

    public static torrent_removed_alert cast_to_torrent_removed_alert(alert alert) {
        long alert_cast_to_torrent_removed_alert = libtorrent_jni.alert_cast_to_torrent_removed_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_removed_alert == 0) {
            return null;
        }
        return new torrent_removed_alert(alert_cast_to_torrent_removed_alert, false);
    }

    public static read_piece_alert cast_to_read_piece_alert(alert alert) {
        long alert_cast_to_read_piece_alert = libtorrent_jni.alert_cast_to_read_piece_alert(getCPtr(alert), alert);
        if (alert_cast_to_read_piece_alert == 0) {
            return null;
        }
        return new read_piece_alert(alert_cast_to_read_piece_alert, false);
    }

    public static file_completed_alert cast_to_file_completed_alert(alert alert) {
        long alert_cast_to_file_completed_alert = libtorrent_jni.alert_cast_to_file_completed_alert(getCPtr(alert), alert);
        if (alert_cast_to_file_completed_alert == 0) {
            return null;
        }
        return new file_completed_alert(alert_cast_to_file_completed_alert, false);
    }

    public static file_renamed_alert cast_to_file_renamed_alert(alert alert) {
        long alert_cast_to_file_renamed_alert = libtorrent_jni.alert_cast_to_file_renamed_alert(getCPtr(alert), alert);
        if (alert_cast_to_file_renamed_alert == 0) {
            return null;
        }
        return new file_renamed_alert(alert_cast_to_file_renamed_alert, false);
    }

    public static file_rename_failed_alert cast_to_file_rename_failed_alert(alert alert) {
        long alert_cast_to_file_rename_failed_alert = libtorrent_jni.alert_cast_to_file_rename_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_file_rename_failed_alert == 0) {
            return null;
        }
        return new file_rename_failed_alert(alert_cast_to_file_rename_failed_alert, false);
    }

    public static performance_alert cast_to_performance_alert(alert alert) {
        long alert_cast_to_performance_alert = libtorrent_jni.alert_cast_to_performance_alert(getCPtr(alert), alert);
        if (alert_cast_to_performance_alert == 0) {
            return null;
        }
        return new performance_alert(alert_cast_to_performance_alert, false);
    }

    public static state_changed_alert cast_to_state_changed_alert(alert alert) {
        long alert_cast_to_state_changed_alert = libtorrent_jni.alert_cast_to_state_changed_alert(getCPtr(alert), alert);
        if (alert_cast_to_state_changed_alert == 0) {
            return null;
        }
        return new state_changed_alert(alert_cast_to_state_changed_alert, false);
    }

    public static tracker_error_alert cast_to_tracker_error_alert(alert alert) {
        long alert_cast_to_tracker_error_alert = libtorrent_jni.alert_cast_to_tracker_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_tracker_error_alert == 0) {
            return null;
        }
        return new tracker_error_alert(alert_cast_to_tracker_error_alert, false);
    }

    public static tracker_warning_alert cast_to_tracker_warning_alert(alert alert) {
        long alert_cast_to_tracker_warning_alert = libtorrent_jni.alert_cast_to_tracker_warning_alert(getCPtr(alert), alert);
        if (alert_cast_to_tracker_warning_alert == 0) {
            return null;
        }
        return new tracker_warning_alert(alert_cast_to_tracker_warning_alert, false);
    }

    public static scrape_reply_alert cast_to_scrape_reply_alert(alert alert) {
        long alert_cast_to_scrape_reply_alert = libtorrent_jni.alert_cast_to_scrape_reply_alert(getCPtr(alert), alert);
        if (alert_cast_to_scrape_reply_alert == 0) {
            return null;
        }
        return new scrape_reply_alert(alert_cast_to_scrape_reply_alert, false);
    }

    public static scrape_failed_alert cast_to_scrape_failed_alert(alert alert) {
        long alert_cast_to_scrape_failed_alert = libtorrent_jni.alert_cast_to_scrape_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_scrape_failed_alert == 0) {
            return null;
        }
        return new scrape_failed_alert(alert_cast_to_scrape_failed_alert, false);
    }

    public static tracker_reply_alert cast_to_tracker_reply_alert(alert alert) {
        long alert_cast_to_tracker_reply_alert = libtorrent_jni.alert_cast_to_tracker_reply_alert(getCPtr(alert), alert);
        if (alert_cast_to_tracker_reply_alert == 0) {
            return null;
        }
        return new tracker_reply_alert(alert_cast_to_tracker_reply_alert, false);
    }

    public static dht_reply_alert cast_to_dht_reply_alert(alert alert) {
        long alert_cast_to_dht_reply_alert = libtorrent_jni.alert_cast_to_dht_reply_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_reply_alert == 0) {
            return null;
        }
        return new dht_reply_alert(alert_cast_to_dht_reply_alert, false);
    }

    public static tracker_announce_alert cast_to_tracker_announce_alert(alert alert) {
        long alert_cast_to_tracker_announce_alert = libtorrent_jni.alert_cast_to_tracker_announce_alert(getCPtr(alert), alert);
        if (alert_cast_to_tracker_announce_alert == 0) {
            return null;
        }
        return new tracker_announce_alert(alert_cast_to_tracker_announce_alert, false);
    }

    public static hash_failed_alert cast_to_hash_failed_alert(alert alert) {
        long alert_cast_to_hash_failed_alert = libtorrent_jni.alert_cast_to_hash_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_hash_failed_alert == 0) {
            return null;
        }
        return new hash_failed_alert(alert_cast_to_hash_failed_alert, false);
    }

    public static peer_ban_alert cast_to_peer_ban_alert(alert alert) {
        long alert_cast_to_peer_ban_alert = libtorrent_jni.alert_cast_to_peer_ban_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_ban_alert == 0) {
            return null;
        }
        return new peer_ban_alert(alert_cast_to_peer_ban_alert, false);
    }

    public static peer_unsnubbed_alert cast_to_peer_unsnubbed_alert(alert alert) {
        long alert_cast_to_peer_unsnubbed_alert = libtorrent_jni.alert_cast_to_peer_unsnubbed_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_unsnubbed_alert == 0) {
            return null;
        }
        return new peer_unsnubbed_alert(alert_cast_to_peer_unsnubbed_alert, false);
    }

    public static peer_snubbed_alert cast_to_peer_snubbed_alert(alert alert) {
        long alert_cast_to_peer_snubbed_alert = libtorrent_jni.alert_cast_to_peer_snubbed_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_snubbed_alert == 0) {
            return null;
        }
        return new peer_snubbed_alert(alert_cast_to_peer_snubbed_alert, false);
    }

    public static peer_error_alert cast_to_peer_error_alert(alert alert) {
        long alert_cast_to_peer_error_alert = libtorrent_jni.alert_cast_to_peer_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_error_alert == 0) {
            return null;
        }
        return new peer_error_alert(alert_cast_to_peer_error_alert, false);
    }

    public static peer_connect_alert cast_to_peer_connect_alert(alert alert) {
        long alert_cast_to_peer_connect_alert = libtorrent_jni.alert_cast_to_peer_connect_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_connect_alert == 0) {
            return null;
        }
        return new peer_connect_alert(alert_cast_to_peer_connect_alert, false);
    }

    public static peer_disconnected_alert cast_to_peer_disconnected_alert(alert alert) {
        long alert_cast_to_peer_disconnected_alert = libtorrent_jni.alert_cast_to_peer_disconnected_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_disconnected_alert == 0) {
            return null;
        }
        return new peer_disconnected_alert(alert_cast_to_peer_disconnected_alert, false);
    }

    public static invalid_request_alert cast_to_invalid_request_alert(alert alert) {
        long alert_cast_to_invalid_request_alert = libtorrent_jni.alert_cast_to_invalid_request_alert(getCPtr(alert), alert);
        if (alert_cast_to_invalid_request_alert == 0) {
            return null;
        }
        return new invalid_request_alert(alert_cast_to_invalid_request_alert, false);
    }

    public static torrent_finished_alert cast_to_torrent_finished_alert(alert alert) {
        long alert_cast_to_torrent_finished_alert = libtorrent_jni.alert_cast_to_torrent_finished_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_finished_alert == 0) {
            return null;
        }
        return new torrent_finished_alert(alert_cast_to_torrent_finished_alert, false);
    }

    public static piece_finished_alert cast_to_piece_finished_alert(alert alert) {
        long alert_cast_to_piece_finished_alert = libtorrent_jni.alert_cast_to_piece_finished_alert(getCPtr(alert), alert);
        if (alert_cast_to_piece_finished_alert == 0) {
            return null;
        }
        return new piece_finished_alert(alert_cast_to_piece_finished_alert, false);
    }

    public static request_dropped_alert cast_to_request_dropped_alert(alert alert) {
        long alert_cast_to_request_dropped_alert = libtorrent_jni.alert_cast_to_request_dropped_alert(getCPtr(alert), alert);
        if (alert_cast_to_request_dropped_alert == 0) {
            return null;
        }
        return new request_dropped_alert(alert_cast_to_request_dropped_alert, false);
    }

    public static block_timeout_alert cast_to_block_timeout_alert(alert alert) {
        long alert_cast_to_block_timeout_alert = libtorrent_jni.alert_cast_to_block_timeout_alert(getCPtr(alert), alert);
        if (alert_cast_to_block_timeout_alert == 0) {
            return null;
        }
        return new block_timeout_alert(alert_cast_to_block_timeout_alert, false);
    }

    public static block_finished_alert cast_to_block_finished_alert(alert alert) {
        long alert_cast_to_block_finished_alert = libtorrent_jni.alert_cast_to_block_finished_alert(getCPtr(alert), alert);
        if (alert_cast_to_block_finished_alert == 0) {
            return null;
        }
        return new block_finished_alert(alert_cast_to_block_finished_alert, false);
    }

    public static block_downloading_alert cast_to_block_downloading_alert(alert alert) {
        long alert_cast_to_block_downloading_alert = libtorrent_jni.alert_cast_to_block_downloading_alert(getCPtr(alert), alert);
        if (alert_cast_to_block_downloading_alert == 0) {
            return null;
        }
        return new block_downloading_alert(alert_cast_to_block_downloading_alert, false);
    }

    public static unwanted_block_alert cast_to_unwanted_block_alert(alert alert) {
        long alert_cast_to_unwanted_block_alert = libtorrent_jni.alert_cast_to_unwanted_block_alert(getCPtr(alert), alert);
        if (alert_cast_to_unwanted_block_alert == 0) {
            return null;
        }
        return new unwanted_block_alert(alert_cast_to_unwanted_block_alert, false);
    }

    public static storage_moved_alert cast_to_storage_moved_alert(alert alert) {
        long alert_cast_to_storage_moved_alert = libtorrent_jni.alert_cast_to_storage_moved_alert(getCPtr(alert), alert);
        if (alert_cast_to_storage_moved_alert == 0) {
            return null;
        }
        return new storage_moved_alert(alert_cast_to_storage_moved_alert, false);
    }

    public static storage_moved_failed_alert cast_to_storage_moved_failed_alert(alert alert) {
        long alert_cast_to_storage_moved_failed_alert = libtorrent_jni.alert_cast_to_storage_moved_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_storage_moved_failed_alert == 0) {
            return null;
        }
        return new storage_moved_failed_alert(alert_cast_to_storage_moved_failed_alert, false);
    }

    public static torrent_deleted_alert cast_to_torrent_deleted_alert(alert alert) {
        long alert_cast_to_torrent_deleted_alert = libtorrent_jni.alert_cast_to_torrent_deleted_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_deleted_alert == 0) {
            return null;
        }
        return new torrent_deleted_alert(alert_cast_to_torrent_deleted_alert, false);
    }

    public static torrent_delete_failed_alert cast_to_torrent_delete_failed_alert(alert alert) {
        long alert_cast_to_torrent_delete_failed_alert = libtorrent_jni.alert_cast_to_torrent_delete_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_delete_failed_alert == 0) {
            return null;
        }
        return new torrent_delete_failed_alert(alert_cast_to_torrent_delete_failed_alert, false);
    }

    public static save_resume_data_alert cast_to_save_resume_data_alert(alert alert) {
        long alert_cast_to_save_resume_data_alert = libtorrent_jni.alert_cast_to_save_resume_data_alert(getCPtr(alert), alert);
        if (alert_cast_to_save_resume_data_alert == 0) {
            return null;
        }
        return new save_resume_data_alert(alert_cast_to_save_resume_data_alert, false);
    }

    public static save_resume_data_failed_alert cast_to_save_resume_data_failed_alert(alert alert) {
        long alert_cast_to_save_resume_data_failed_alert = libtorrent_jni.alert_cast_to_save_resume_data_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_save_resume_data_failed_alert == 0) {
            return null;
        }
        return new save_resume_data_failed_alert(alert_cast_to_save_resume_data_failed_alert, false);
    }

    public static torrent_paused_alert cast_to_torrent_paused_alert(alert alert) {
        long alert_cast_to_torrent_paused_alert = libtorrent_jni.alert_cast_to_torrent_paused_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_paused_alert == 0) {
            return null;
        }
        return new torrent_paused_alert(alert_cast_to_torrent_paused_alert, false);
    }

    public static torrent_resumed_alert cast_to_torrent_resumed_alert(alert alert) {
        long alert_cast_to_torrent_resumed_alert = libtorrent_jni.alert_cast_to_torrent_resumed_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_resumed_alert == 0) {
            return null;
        }
        return new torrent_resumed_alert(alert_cast_to_torrent_resumed_alert, false);
    }

    public static torrent_checked_alert cast_to_torrent_checked_alert(alert alert) {
        long alert_cast_to_torrent_checked_alert = libtorrent_jni.alert_cast_to_torrent_checked_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_checked_alert == 0) {
            return null;
        }
        return new torrent_checked_alert(alert_cast_to_torrent_checked_alert, false);
    }

    public static url_seed_alert cast_to_url_seed_alert(alert alert) {
        long alert_cast_to_url_seed_alert = libtorrent_jni.alert_cast_to_url_seed_alert(getCPtr(alert), alert);
        if (alert_cast_to_url_seed_alert == 0) {
            return null;
        }
        return new url_seed_alert(alert_cast_to_url_seed_alert, false);
    }

    public static file_error_alert cast_to_file_error_alert(alert alert) {
        long alert_cast_to_file_error_alert = libtorrent_jni.alert_cast_to_file_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_file_error_alert == 0) {
            return null;
        }
        return new file_error_alert(alert_cast_to_file_error_alert, false);
    }

    public static metadata_failed_alert cast_to_metadata_failed_alert(alert alert) {
        long alert_cast_to_metadata_failed_alert = libtorrent_jni.alert_cast_to_metadata_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_metadata_failed_alert == 0) {
            return null;
        }
        return new metadata_failed_alert(alert_cast_to_metadata_failed_alert, false);
    }

    public static metadata_received_alert cast_to_metadata_received_alert(alert alert) {
        long alert_cast_to_metadata_received_alert = libtorrent_jni.alert_cast_to_metadata_received_alert(getCPtr(alert), alert);
        if (alert_cast_to_metadata_received_alert == 0) {
            return null;
        }
        return new metadata_received_alert(alert_cast_to_metadata_received_alert, false);
    }

    public static udp_error_alert cast_to_udp_error_alert(alert alert) {
        long alert_cast_to_udp_error_alert = libtorrent_jni.alert_cast_to_udp_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_udp_error_alert == 0) {
            return null;
        }
        return new udp_error_alert(alert_cast_to_udp_error_alert, false);
    }

    public static external_ip_alert cast_to_external_ip_alert(alert alert) {
        long alert_cast_to_external_ip_alert = libtorrent_jni.alert_cast_to_external_ip_alert(getCPtr(alert), alert);
        if (alert_cast_to_external_ip_alert == 0) {
            return null;
        }
        return new external_ip_alert(alert_cast_to_external_ip_alert, false);
    }

    public static listen_failed_alert cast_to_listen_failed_alert(alert alert) {
        long alert_cast_to_listen_failed_alert = libtorrent_jni.alert_cast_to_listen_failed_alert(getCPtr(alert), alert);
        if (alert_cast_to_listen_failed_alert == 0) {
            return null;
        }
        return new listen_failed_alert(alert_cast_to_listen_failed_alert, false);
    }

    public static listen_succeeded_alert cast_to_listen_succeeded_alert(alert alert) {
        long alert_cast_to_listen_succeeded_alert = libtorrent_jni.alert_cast_to_listen_succeeded_alert(getCPtr(alert), alert);
        if (alert_cast_to_listen_succeeded_alert == 0) {
            return null;
        }
        return new listen_succeeded_alert(alert_cast_to_listen_succeeded_alert, false);
    }

    public static portmap_error_alert cast_to_portmap_error_alert(alert alert) {
        long alert_cast_to_portmap_error_alert = libtorrent_jni.alert_cast_to_portmap_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_portmap_error_alert == 0) {
            return null;
        }
        return new portmap_error_alert(alert_cast_to_portmap_error_alert, false);
    }

    public static portmap_alert cast_to_portmap_alert(alert alert) {
        long alert_cast_to_portmap_alert = libtorrent_jni.alert_cast_to_portmap_alert(getCPtr(alert), alert);
        if (alert_cast_to_portmap_alert == 0) {
            return null;
        }
        return new portmap_alert(alert_cast_to_portmap_alert, false);
    }

    public static portmap_log_alert cast_to_portmap_log_alert(alert alert) {
        long alert_cast_to_portmap_log_alert = libtorrent_jni.alert_cast_to_portmap_log_alert(getCPtr(alert), alert);
        if (alert_cast_to_portmap_log_alert == 0) {
            return null;
        }
        return new portmap_log_alert(alert_cast_to_portmap_log_alert, false);
    }

    public static fastresume_rejected_alert cast_to_fastresume_rejected_alert(alert alert) {
        long alert_cast_to_fastresume_rejected_alert = libtorrent_jni.alert_cast_to_fastresume_rejected_alert(getCPtr(alert), alert);
        if (alert_cast_to_fastresume_rejected_alert == 0) {
            return null;
        }
        return new fastresume_rejected_alert(alert_cast_to_fastresume_rejected_alert, false);
    }

    public static peer_blocked_alert cast_to_peer_blocked_alert(alert alert) {
        long alert_cast_to_peer_blocked_alert = libtorrent_jni.alert_cast_to_peer_blocked_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_blocked_alert == 0) {
            return null;
        }
        return new peer_blocked_alert(alert_cast_to_peer_blocked_alert, false);
    }

    public static dht_announce_alert cast_to_dht_announce_alert(alert alert) {
        long alert_cast_to_dht_announce_alert = libtorrent_jni.alert_cast_to_dht_announce_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_announce_alert == 0) {
            return null;
        }
        return new dht_announce_alert(alert_cast_to_dht_announce_alert, false);
    }

    public static dht_get_peers_alert cast_to_dht_get_peers_alert(alert alert) {
        long alert_cast_to_dht_get_peers_alert = libtorrent_jni.alert_cast_to_dht_get_peers_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_get_peers_alert == 0) {
            return null;
        }
        return new dht_get_peers_alert(alert_cast_to_dht_get_peers_alert, false);
    }

    public static stats_alert cast_to_stats_alert(alert alert) {
        long alert_cast_to_stats_alert = libtorrent_jni.alert_cast_to_stats_alert(getCPtr(alert), alert);
        if (alert_cast_to_stats_alert == 0) {
            return null;
        }
        return new stats_alert(alert_cast_to_stats_alert, false);
    }

    public static cache_flushed_alert cast_to_cache_flushed_alert(alert alert) {
        long alert_cast_to_cache_flushed_alert = libtorrent_jni.alert_cast_to_cache_flushed_alert(getCPtr(alert), alert);
        if (alert_cast_to_cache_flushed_alert == 0) {
            return null;
        }
        return new cache_flushed_alert(alert_cast_to_cache_flushed_alert, false);
    }

    public static anonymous_mode_alert cast_to_anonymous_mode_alert(alert alert) {
        long alert_cast_to_anonymous_mode_alert = libtorrent_jni.alert_cast_to_anonymous_mode_alert(getCPtr(alert), alert);
        if (alert_cast_to_anonymous_mode_alert == 0) {
            return null;
        }
        return new anonymous_mode_alert(alert_cast_to_anonymous_mode_alert, false);
    }

    public static lsd_peer_alert cast_to_lsd_peer_alert(alert alert) {
        long alert_cast_to_lsd_peer_alert = libtorrent_jni.alert_cast_to_lsd_peer_alert(getCPtr(alert), alert);
        if (alert_cast_to_lsd_peer_alert == 0) {
            return null;
        }
        return new lsd_peer_alert(alert_cast_to_lsd_peer_alert, false);
    }

    public static trackerid_alert cast_to_trackerid_alert(alert alert) {
        long alert_cast_to_trackerid_alert = libtorrent_jni.alert_cast_to_trackerid_alert(getCPtr(alert), alert);
        if (alert_cast_to_trackerid_alert == 0) {
            return null;
        }
        return new trackerid_alert(alert_cast_to_trackerid_alert, false);
    }

    public static dht_bootstrap_alert cast_to_dht_bootstrap_alert(alert alert) {
        long alert_cast_to_dht_bootstrap_alert = libtorrent_jni.alert_cast_to_dht_bootstrap_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_bootstrap_alert == 0) {
            return null;
        }
        return new dht_bootstrap_alert(alert_cast_to_dht_bootstrap_alert, false);
    }

    public static torrent_error_alert cast_to_torrent_error_alert(alert alert) {
        long alert_cast_to_torrent_error_alert = libtorrent_jni.alert_cast_to_torrent_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_error_alert == 0) {
            return null;
        }
        return new torrent_error_alert(alert_cast_to_torrent_error_alert, false);
    }

    public static torrent_need_cert_alert cast_to_torrent_need_cert_alert(alert alert) {
        long alert_cast_to_torrent_need_cert_alert = libtorrent_jni.alert_cast_to_torrent_need_cert_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_need_cert_alert == 0) {
            return null;
        }
        return new torrent_need_cert_alert(alert_cast_to_torrent_need_cert_alert, false);
    }

    public static incoming_connection_alert cast_to_incoming_connection_alert(alert alert) {
        long alert_cast_to_incoming_connection_alert = libtorrent_jni.alert_cast_to_incoming_connection_alert(getCPtr(alert), alert);
        if (alert_cast_to_incoming_connection_alert == 0) {
            return null;
        }
        return new incoming_connection_alert(alert_cast_to_incoming_connection_alert, false);
    }

    public static add_torrent_alert cast_to_add_torrent_alert(alert alert) {
        long alert_cast_to_add_torrent_alert = libtorrent_jni.alert_cast_to_add_torrent_alert(getCPtr(alert), alert);
        if (alert_cast_to_add_torrent_alert == 0) {
            return null;
        }
        return new add_torrent_alert(alert_cast_to_add_torrent_alert, false);
    }

    public static state_update_alert cast_to_state_update_alert(alert alert) {
        long alert_cast_to_state_update_alert = libtorrent_jni.alert_cast_to_state_update_alert(getCPtr(alert), alert);
        if (alert_cast_to_state_update_alert == 0) {
            return null;
        }
        return new state_update_alert(alert_cast_to_state_update_alert, false);
    }

    public static session_stats_alert cast_to_session_stats_alert(alert alert) {
        long alert_cast_to_session_stats_alert = libtorrent_jni.alert_cast_to_session_stats_alert(getCPtr(alert), alert);
        if (alert_cast_to_session_stats_alert == 0) {
            return null;
        }
        return new session_stats_alert(alert_cast_to_session_stats_alert, false);
    }

    public static dht_error_alert cast_to_dht_error_alert(alert alert) {
        long alert_cast_to_dht_error_alert = libtorrent_jni.alert_cast_to_dht_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_error_alert == 0) {
            return null;
        }
        return new dht_error_alert(alert_cast_to_dht_error_alert, false);
    }

    public static dht_immutable_item_alert cast_to_dht_immutable_item_alert(alert alert) {
        long alert_cast_to_dht_immutable_item_alert = libtorrent_jni.alert_cast_to_dht_immutable_item_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_immutable_item_alert == 0) {
            return null;
        }
        return new dht_immutable_item_alert(alert_cast_to_dht_immutable_item_alert, false);
    }

    public static dht_mutable_item_alert cast_to_dht_mutable_item_alert(alert alert) {
        long alert_cast_to_dht_mutable_item_alert = libtorrent_jni.alert_cast_to_dht_mutable_item_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_mutable_item_alert == 0) {
            return null;
        }
        return new dht_mutable_item_alert(alert_cast_to_dht_mutable_item_alert, false);
    }

    public static dht_put_alert cast_to_dht_put_alert(alert alert) {
        long alert_cast_to_dht_put_alert = libtorrent_jni.alert_cast_to_dht_put_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_put_alert == 0) {
            return null;
        }
        return new dht_put_alert(alert_cast_to_dht_put_alert, false);
    }

    public static i2p_alert cast_to_i2p_alert(alert alert) {
        long alert_cast_to_i2p_alert = libtorrent_jni.alert_cast_to_i2p_alert(getCPtr(alert), alert);
        if (alert_cast_to_i2p_alert == 0) {
            return null;
        }
        return new i2p_alert(alert_cast_to_i2p_alert, false);
    }

    public static dht_outgoing_get_peers_alert cast_to_dht_outgoing_get_peers_alert(alert alert) {
        long alert_cast_to_dht_outgoing_get_peers_alert = libtorrent_jni.alert_cast_to_dht_outgoing_get_peers_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_outgoing_get_peers_alert == 0) {
            return null;
        }
        return new dht_outgoing_get_peers_alert(alert_cast_to_dht_outgoing_get_peers_alert, false);
    }

    public static log_alert cast_to_log_alert(alert alert) {
        long alert_cast_to_log_alert = libtorrent_jni.alert_cast_to_log_alert(getCPtr(alert), alert);
        if (alert_cast_to_log_alert == 0) {
            return null;
        }
        return new log_alert(alert_cast_to_log_alert, false);
    }

    public static torrent_log_alert cast_to_torrent_log_alert(alert alert) {
        long alert_cast_to_torrent_log_alert = libtorrent_jni.alert_cast_to_torrent_log_alert(getCPtr(alert), alert);
        if (alert_cast_to_torrent_log_alert == 0) {
            return null;
        }
        return new torrent_log_alert(alert_cast_to_torrent_log_alert, false);
    }

    public static peer_log_alert cast_to_peer_log_alert(alert alert) {
        long alert_cast_to_peer_log_alert = libtorrent_jni.alert_cast_to_peer_log_alert(getCPtr(alert), alert);
        if (alert_cast_to_peer_log_alert == 0) {
            return null;
        }
        return new peer_log_alert(alert_cast_to_peer_log_alert, false);
    }

    public static lsd_error_alert cast_to_lsd_error_alert(alert alert) {
        long alert_cast_to_lsd_error_alert = libtorrent_jni.alert_cast_to_lsd_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_lsd_error_alert == 0) {
            return null;
        }
        return new lsd_error_alert(alert_cast_to_lsd_error_alert, false);
    }

    public static dht_stats_alert cast_to_dht_stats_alert(alert alert) {
        long alert_cast_to_dht_stats_alert = libtorrent_jni.alert_cast_to_dht_stats_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_stats_alert == 0) {
            return null;
        }
        return new dht_stats_alert(alert_cast_to_dht_stats_alert, false);
    }

    public static incoming_request_alert cast_to_incoming_request_alert(alert alert) {
        long alert_cast_to_incoming_request_alert = libtorrent_jni.alert_cast_to_incoming_request_alert(getCPtr(alert), alert);
        if (alert_cast_to_incoming_request_alert == 0) {
            return null;
        }
        return new incoming_request_alert(alert_cast_to_incoming_request_alert, false);
    }

    public static dht_log_alert cast_to_dht_log_alert(alert alert) {
        long alert_cast_to_dht_log_alert = libtorrent_jni.alert_cast_to_dht_log_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_log_alert == 0) {
            return null;
        }
        return new dht_log_alert(alert_cast_to_dht_log_alert, false);
    }

    public static dht_pkt_alert cast_to_dht_pkt_alert(alert alert) {
        long alert_cast_to_dht_pkt_alert = libtorrent_jni.alert_cast_to_dht_pkt_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_pkt_alert == 0) {
            return null;
        }
        return new dht_pkt_alert(alert_cast_to_dht_pkt_alert, false);
    }

    public static dht_get_peers_reply_alert cast_to_dht_get_peers_reply_alert(alert alert) {
        long alert_cast_to_dht_get_peers_reply_alert = libtorrent_jni.alert_cast_to_dht_get_peers_reply_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_get_peers_reply_alert == 0) {
            return null;
        }
        return new dht_get_peers_reply_alert(alert_cast_to_dht_get_peers_reply_alert, false);
    }

    public static dht_direct_response_alert cast_to_dht_direct_response_alert(alert alert) {
        long alert_cast_to_dht_direct_response_alert = libtorrent_jni.alert_cast_to_dht_direct_response_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_direct_response_alert == 0) {
            return null;
        }
        return new dht_direct_response_alert(alert_cast_to_dht_direct_response_alert, false);
    }

    public static picker_log_alert cast_to_picker_log_alert(alert alert) {
        long alert_cast_to_picker_log_alert = libtorrent_jni.alert_cast_to_picker_log_alert(getCPtr(alert), alert);
        if (alert_cast_to_picker_log_alert == 0) {
            return null;
        }
        return new picker_log_alert(alert_cast_to_picker_log_alert, false);
    }

    public static session_error_alert cast_to_session_error_alert(alert alert) {
        long alert_cast_to_session_error_alert = libtorrent_jni.alert_cast_to_session_error_alert(getCPtr(alert), alert);
        if (alert_cast_to_session_error_alert == 0) {
            return null;
        }
        return new session_error_alert(alert_cast_to_session_error_alert, false);
    }

    public static dht_live_nodes_alert cast_to_dht_live_nodes_alert(alert alert) {
        long alert_cast_to_dht_live_nodes_alert = libtorrent_jni.alert_cast_to_dht_live_nodes_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_live_nodes_alert == 0) {
            return null;
        }
        return new dht_live_nodes_alert(alert_cast_to_dht_live_nodes_alert, false);
    }

    public static session_stats_header_alert cast_to_session_stats_header_alert(alert alert) {
        long alert_cast_to_session_stats_header_alert = libtorrent_jni.alert_cast_to_session_stats_header_alert(getCPtr(alert), alert);
        if (alert_cast_to_session_stats_header_alert == 0) {
            return null;
        }
        return new session_stats_header_alert(alert_cast_to_session_stats_header_alert, false);
    }

    public static dht_sample_infohashes_alert cast_to_dht_sample_infohashes_alert(alert alert) {
        long alert_cast_to_dht_sample_infohashes_alert = libtorrent_jni.alert_cast_to_dht_sample_infohashes_alert(getCPtr(alert), alert);
        if (alert_cast_to_dht_sample_infohashes_alert == 0) {
            return null;
        }
        return new dht_sample_infohashes_alert(alert_cast_to_dht_sample_infohashes_alert, false);
    }

    public static block_uploaded_alert cast_to_block_uploaded_alert(alert alert) {
        long alert_cast_to_block_uploaded_alert = libtorrent_jni.alert_cast_to_block_uploaded_alert(getCPtr(alert), alert);
        if (alert_cast_to_block_uploaded_alert == 0) {
            return null;
        }
        return new block_uploaded_alert(alert_cast_to_block_uploaded_alert, false);
    }

    public long get_timestamp() {
        return libtorrent_jni.alert_get_timestamp(this.swigCPtr, this);
    }
}
