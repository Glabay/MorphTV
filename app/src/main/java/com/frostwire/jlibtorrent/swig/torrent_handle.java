package com.frostwire.jlibtorrent.swig;

public class torrent_handle {
    public static final deadline_flags_t alert_when_available = new deadline_flags_t(libtorrent_jni.torrent_handle_alert_when_available_get(), false);
    public static final resume_data_flags_t flush_disk_cache = new resume_data_flags_t(libtorrent_jni.torrent_handle_flush_disk_cache_get(), false);
    public static final pause_flags_t graceful_pause = new pause_flags_t(libtorrent_jni.torrent_handle_graceful_pause_get(), false);
    public static final resume_data_flags_t only_if_modified = new resume_data_flags_t(libtorrent_jni.torrent_handle_only_if_modified_get(), false);
    public static final add_piece_flags_t overwrite_existing = new add_piece_flags_t(libtorrent_jni.torrent_handle_overwrite_existing_get(), false);
    public static final status_flags_t query_accurate_download_counters = new status_flags_t(libtorrent_jni.torrent_handle_query_accurate_download_counters_get(), false);
    public static final status_flags_t query_distributed_copies = new status_flags_t(libtorrent_jni.torrent_handle_query_distributed_copies_get(), false);
    public static final status_flags_t query_last_seen_complete = new status_flags_t(libtorrent_jni.torrent_handle_query_last_seen_complete_get(), false);
    public static final status_flags_t query_name = new status_flags_t(libtorrent_jni.torrent_handle_query_name_get(), false);
    public static final status_flags_t query_pieces = new status_flags_t(libtorrent_jni.torrent_handle_query_pieces_get(), false);
    public static final status_flags_t query_save_path = new status_flags_t(libtorrent_jni.torrent_handle_query_save_path_get(), false);
    public static final status_flags_t query_torrent_file = new status_flags_t(libtorrent_jni.torrent_handle_query_torrent_file_get(), false);
    public static final status_flags_t query_verified_pieces = new status_flags_t(libtorrent_jni.torrent_handle_query_verified_pieces_get(), false);
    public static final resume_data_flags_t save_info_dict = new resume_data_flags_t(libtorrent_jni.torrent_handle_save_info_dict_get(), false);
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class file_progress_flags_t {
        public static final file_progress_flags_t piece_granularity = new file_progress_flags_t("piece_granularity", libtorrent_jni.torrent_handle_piece_granularity_get());
        private static int swigNext;
        private static file_progress_flags_t[] swigValues = new file_progress_flags_t[]{piece_granularity};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static file_progress_flags_t swigToEnum(int i) {
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
            stringBuilder.append(file_progress_flags_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private file_progress_flags_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private file_progress_flags_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private file_progress_flags_t(String str, file_progress_flags_t file_progress_flags_t) {
            this.swigName = str;
            this.swigValue = file_progress_flags_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected torrent_handle(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_handle torrent_handle) {
        return torrent_handle == null ? 0 : torrent_handle.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_handle(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void read_piece(int i) {
        libtorrent_jni.torrent_handle_read_piece(this.swigCPtr, this, i);
    }

    public boolean have_piece(int i) {
        return libtorrent_jni.torrent_handle_have_piece(this.swigCPtr, this, i);
    }

    public void get_peer_info(peer_info_vector peer_info_vector) {
        libtorrent_jni.torrent_handle_get_peer_info(this.swigCPtr, this, peer_info_vector.getCPtr(peer_info_vector), peer_info_vector);
    }

    public torrent_status status(status_flags_t status_flags_t) {
        return new torrent_status(libtorrent_jni.torrent_handle_status__SWIG_0(this.swigCPtr, this, status_flags_t.getCPtr(status_flags_t), status_flags_t), true);
    }

    public torrent_status status() {
        return new torrent_status(libtorrent_jni.torrent_handle_status__SWIG_1(this.swigCPtr, this), true);
    }

    public void get_download_queue(partial_piece_info_vector partial_piece_info_vector) {
        libtorrent_jni.torrent_handle_get_download_queue(this.swigCPtr, this, partial_piece_info_vector.getCPtr(partial_piece_info_vector), partial_piece_info_vector);
    }

    public void set_piece_deadline(int i, int i2, deadline_flags_t deadline_flags_t) {
        libtorrent_jni.torrent_handle_set_piece_deadline__SWIG_0(this.swigCPtr, this, i, i2, deadline_flags_t.getCPtr(deadline_flags_t), deadline_flags_t);
    }

    public void set_piece_deadline(int i, int i2) {
        libtorrent_jni.torrent_handle_set_piece_deadline__SWIG_1(this.swigCPtr, this, i, i2);
    }

    public void reset_piece_deadline(int i) {
        libtorrent_jni.torrent_handle_reset_piece_deadline(this.swigCPtr, this, i);
    }

    public void clear_piece_deadlines() {
        libtorrent_jni.torrent_handle_clear_piece_deadlines(this.swigCPtr, this);
    }

    public void file_progress(int64_vector int64_vector, int i) {
        libtorrent_jni.torrent_handle_file_progress__SWIG_0(this.swigCPtr, this, int64_vector.getCPtr(int64_vector), int64_vector, i);
    }

    public void file_progress(int64_vector int64_vector) {
        libtorrent_jni.torrent_handle_file_progress__SWIG_1(this.swigCPtr, this, int64_vector.getCPtr(int64_vector), int64_vector);
    }

    public void clear_error() {
        libtorrent_jni.torrent_handle_clear_error(this.swigCPtr, this);
    }

    public announce_entry_vector trackers() {
        return new announce_entry_vector(libtorrent_jni.torrent_handle_trackers(this.swigCPtr, this), true);
    }

    public void replace_trackers(announce_entry_vector announce_entry_vector) {
        libtorrent_jni.torrent_handle_replace_trackers(this.swigCPtr, this, announce_entry_vector.getCPtr(announce_entry_vector), announce_entry_vector);
    }

    public void add_tracker(announce_entry announce_entry) {
        libtorrent_jni.torrent_handle_add_tracker(this.swigCPtr, this, announce_entry.getCPtr(announce_entry), announce_entry);
    }

    public void add_url_seed(String str) {
        libtorrent_jni.torrent_handle_add_url_seed(this.swigCPtr, this, str);
    }

    public void remove_url_seed(String str) {
        libtorrent_jni.torrent_handle_remove_url_seed(this.swigCPtr, this, str);
    }

    public void add_http_seed(String str) {
        libtorrent_jni.torrent_handle_add_http_seed(this.swigCPtr, this, str);
    }

    public void remove_http_seed(String str) {
        libtorrent_jni.torrent_handle_remove_http_seed(this.swigCPtr, this, str);
    }

    public boolean is_valid() {
        return libtorrent_jni.torrent_handle_is_valid(this.swigCPtr, this);
    }

    public void pause(pause_flags_t pause_flags_t) {
        libtorrent_jni.torrent_handle_pause__SWIG_0(this.swigCPtr, this, pause_flags_t.getCPtr(pause_flags_t), pause_flags_t);
    }

    public void pause() {
        libtorrent_jni.torrent_handle_pause__SWIG_1(this.swigCPtr, this);
    }

    public void resume() {
        libtorrent_jni.torrent_handle_resume(this.swigCPtr, this);
    }

    public torrent_flags_t flags() {
        return new torrent_flags_t(libtorrent_jni.torrent_handle_flags(this.swigCPtr, this), true);
    }

    public void set_flags(torrent_flags_t torrent_flags_t, torrent_flags_t torrent_flags_t2) {
        libtorrent_jni.torrent_handle_set_flags__SWIG_0(this.swigCPtr, this, torrent_flags_t.getCPtr(torrent_flags_t), torrent_flags_t, torrent_flags_t.getCPtr(torrent_flags_t2), torrent_flags_t2);
    }

    public void set_flags(torrent_flags_t torrent_flags_t) {
        libtorrent_jni.torrent_handle_set_flags__SWIG_1(this.swigCPtr, this, torrent_flags_t.getCPtr(torrent_flags_t), torrent_flags_t);
    }

    public void unset_flags(torrent_flags_t torrent_flags_t) {
        libtorrent_jni.torrent_handle_unset_flags(this.swigCPtr, this, torrent_flags_t.getCPtr(torrent_flags_t), torrent_flags_t);
    }

    public void flush_cache() {
        libtorrent_jni.torrent_handle_flush_cache(this.swigCPtr, this);
    }

    public void force_recheck() {
        libtorrent_jni.torrent_handle_force_recheck(this.swigCPtr, this);
    }

    public void save_resume_data(resume_data_flags_t resume_data_flags_t) {
        libtorrent_jni.torrent_handle_save_resume_data__SWIG_0(this.swigCPtr, this, resume_data_flags_t.getCPtr(resume_data_flags_t), resume_data_flags_t);
    }

    public void save_resume_data() {
        libtorrent_jni.torrent_handle_save_resume_data__SWIG_1(this.swigCPtr, this);
    }

    public boolean need_save_resume_data() {
        return libtorrent_jni.torrent_handle_need_save_resume_data(this.swigCPtr, this);
    }

    public int queue_position() {
        return libtorrent_jni.torrent_handle_queue_position(this.swigCPtr, this);
    }

    public void queue_position_up() {
        libtorrent_jni.torrent_handle_queue_position_up(this.swigCPtr, this);
    }

    public void queue_position_down() {
        libtorrent_jni.torrent_handle_queue_position_down(this.swigCPtr, this);
    }

    public void queue_position_top() {
        libtorrent_jni.torrent_handle_queue_position_top(this.swigCPtr, this);
    }

    public void queue_position_bottom() {
        libtorrent_jni.torrent_handle_queue_position_bottom(this.swigCPtr, this);
    }

    public void queue_position_set(int i) {
        libtorrent_jni.torrent_handle_queue_position_set(this.swigCPtr, this, i);
    }

    public void piece_availability(int_vector int_vector) {
        libtorrent_jni.torrent_handle_piece_availability(this.swigCPtr, this, int_vector.getCPtr(int_vector), int_vector);
    }

    public void piece_priority(int i, int i2) {
        libtorrent_jni.torrent_handle_piece_priority__SWIG_0(this.swigCPtr, this, i, i2);
    }

    public int piece_priority(int i) {
        return libtorrent_jni.torrent_handle_piece_priority__SWIG_1(this.swigCPtr, this, i);
    }

    public void prioritize_pieces(int_vector int_vector) {
        libtorrent_jni.torrent_handle_prioritize_pieces__SWIG_0(this.swigCPtr, this, int_vector.getCPtr(int_vector), int_vector);
    }

    public void prioritize_pieces(piece_index_int_pair_vector piece_index_int_pair_vector) {
        libtorrent_jni.torrent_handle_prioritize_pieces__SWIG_1(this.swigCPtr, this, piece_index_int_pair_vector.getCPtr(piece_index_int_pair_vector), piece_index_int_pair_vector);
    }

    public int_vector piece_priorities() {
        return new int_vector(libtorrent_jni.torrent_handle_piece_priorities(this.swigCPtr, this), true);
    }

    public void file_priority(int i, int i2) {
        libtorrent_jni.torrent_handle_file_priority__SWIG_0(this.swigCPtr, this, i, i2);
    }

    public int file_priority(int i) {
        return libtorrent_jni.torrent_handle_file_priority__SWIG_1(this.swigCPtr, this, i);
    }

    public void prioritize_files(int_vector int_vector) {
        libtorrent_jni.torrent_handle_prioritize_files(this.swigCPtr, this, int_vector.getCPtr(int_vector), int_vector);
    }

    public int_vector file_priorities() {
        return new int_vector(libtorrent_jni.torrent_handle_file_priorities(this.swigCPtr, this), true);
    }

    public void force_reannounce(int i, int i2) {
        libtorrent_jni.torrent_handle_force_reannounce__SWIG_0(this.swigCPtr, this, i, i2);
    }

    public void force_reannounce(int i) {
        libtorrent_jni.torrent_handle_force_reannounce__SWIG_1(this.swigCPtr, this, i);
    }

    public void force_reannounce() {
        libtorrent_jni.torrent_handle_force_reannounce__SWIG_2(this.swigCPtr, this);
    }

    public void force_dht_announce() {
        libtorrent_jni.torrent_handle_force_dht_announce(this.swigCPtr, this);
    }

    public void scrape_tracker(int i) {
        libtorrent_jni.torrent_handle_scrape_tracker__SWIG_0(this.swigCPtr, this, i);
    }

    public void scrape_tracker() {
        libtorrent_jni.torrent_handle_scrape_tracker__SWIG_1(this.swigCPtr, this);
    }

    public void set_upload_limit(int i) {
        libtorrent_jni.torrent_handle_set_upload_limit(this.swigCPtr, this, i);
    }

    public int upload_limit() {
        return libtorrent_jni.torrent_handle_upload_limit(this.swigCPtr, this);
    }

    public void set_download_limit(int i) {
        libtorrent_jni.torrent_handle_set_download_limit(this.swigCPtr, this, i);
    }

    public int download_limit() {
        return libtorrent_jni.torrent_handle_download_limit(this.swigCPtr, this);
    }

    public void connect_peer(tcp_endpoint tcp_endpoint, peer_source_flags_t peer_source_flags_t, int i) {
        libtorrent_jni.torrent_handle_connect_peer__SWIG_0(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint, peer_source_flags_t.getCPtr(peer_source_flags_t), peer_source_flags_t, i);
    }

    public void connect_peer(tcp_endpoint tcp_endpoint, peer_source_flags_t peer_source_flags_t) {
        libtorrent_jni.torrent_handle_connect_peer__SWIG_1(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint, peer_source_flags_t.getCPtr(peer_source_flags_t), peer_source_flags_t);
    }

    public void connect_peer(tcp_endpoint tcp_endpoint) {
        libtorrent_jni.torrent_handle_connect_peer__SWIG_2(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint);
    }

    public void set_max_uploads(int i) {
        libtorrent_jni.torrent_handle_set_max_uploads(this.swigCPtr, this, i);
    }

    public int max_uploads() {
        return libtorrent_jni.torrent_handle_max_uploads(this.swigCPtr, this);
    }

    public void set_max_connections(int i) {
        libtorrent_jni.torrent_handle_set_max_connections(this.swigCPtr, this, i);
    }

    public int max_connections() {
        return libtorrent_jni.torrent_handle_max_connections(this.swigCPtr, this);
    }

    public void move_storage(String str, move_flags_t move_flags_t) {
        libtorrent_jni.torrent_handle_move_storage__SWIG_0(this.swigCPtr, this, str, move_flags_t.swigValue());
    }

    public void move_storage(String str) {
        libtorrent_jni.torrent_handle_move_storage__SWIG_1(this.swigCPtr, this, str);
    }

    public void rename_file(int i, String str) {
        libtorrent_jni.torrent_handle_rename_file(this.swigCPtr, this, i, str);
    }

    public sha1_hash info_hash() {
        return new sha1_hash(libtorrent_jni.torrent_handle_info_hash(this.swigCPtr, this), true);
    }

    public boolean op_eq(torrent_handle torrent_handle) {
        return libtorrent_jni.torrent_handle_op_eq(this.swigCPtr, this, getCPtr(torrent_handle), torrent_handle);
    }

    public boolean op_ne(torrent_handle torrent_handle) {
        return libtorrent_jni.torrent_handle_op_ne(this.swigCPtr, this, getCPtr(torrent_handle), torrent_handle);
    }

    public boolean op_lt(torrent_handle torrent_handle) {
        return libtorrent_jni.torrent_handle_op_lt(this.swigCPtr, this, getCPtr(torrent_handle), torrent_handle);
    }

    public long id() {
        return libtorrent_jni.torrent_handle_id(this.swigCPtr, this);
    }

    public void add_piece_bytes(int i, byte_vector byte_vector, add_piece_flags_t add_piece_flags_t) {
        libtorrent_jni.torrent_handle_add_piece_bytes__SWIG_0(this.swigCPtr, this, i, byte_vector.getCPtr(byte_vector), byte_vector, add_piece_flags_t.getCPtr(add_piece_flags_t), add_piece_flags_t);
    }

    public void add_piece_bytes(int i, byte_vector byte_vector) {
        libtorrent_jni.torrent_handle_add_piece_bytes__SWIG_1(this.swigCPtr, this, i, byte_vector.getCPtr(byte_vector), byte_vector);
    }

    public torrent_info torrent_file_ptr() {
        long torrent_handle_torrent_file_ptr = libtorrent_jni.torrent_handle_torrent_file_ptr(this.swigCPtr, this);
        if (torrent_handle_torrent_file_ptr == 0) {
            return null;
        }
        return new torrent_info(torrent_handle_torrent_file_ptr, false);
    }

    public string_vector get_url_seeds() {
        return new string_vector(libtorrent_jni.torrent_handle_get_url_seeds(this.swigCPtr, this), true);
    }

    public string_vector get_http_seeds() {
        return new string_vector(libtorrent_jni.torrent_handle_get_http_seeds(this.swigCPtr, this), true);
    }

    public void connect_peer2(tcp_endpoint tcp_endpoint, byte b, int i) {
        libtorrent_jni.torrent_handle_connect_peer2__SWIG_0(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint, b, i);
    }

    public void connect_peer2(tcp_endpoint tcp_endpoint, byte b) {
        libtorrent_jni.torrent_handle_connect_peer2__SWIG_1(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint, b);
    }

    public void connect_peer2(tcp_endpoint tcp_endpoint) {
        libtorrent_jni.torrent_handle_connect_peer2__SWIG_2(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint);
    }
}
