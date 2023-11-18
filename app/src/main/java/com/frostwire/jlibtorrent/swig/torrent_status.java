package com.frostwire.jlibtorrent.swig;

public class torrent_status {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class state_t {
        public static final state_t allocating = new state_t("allocating");
        public static final state_t checking_files = new state_t("checking_files", libtorrent_jni.torrent_status_checking_files_get());
        public static final state_t checking_resume_data = new state_t("checking_resume_data");
        public static final state_t downloading = new state_t("downloading");
        public static final state_t downloading_metadata = new state_t("downloading_metadata");
        public static final state_t finished = new state_t("finished");
        public static final state_t seeding = new state_t("seeding");
        private static int swigNext;
        private static state_t[] swigValues = new state_t[]{checking_files, downloading_metadata, downloading, finished, seeding, allocating, checking_resume_data};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static state_t swigToEnum(int i) {
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
            stringBuilder.append(state_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private state_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private state_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private state_t(String str, state_t state_t) {
            this.swigName = str;
            this.swigValue = state_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected torrent_status(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_status torrent_status) {
        return torrent_status == null ? 0 : torrent_status.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_status(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public torrent_status() {
        this(libtorrent_jni.new_torrent_status__SWIG_0(), true);
    }

    public torrent_status(torrent_status torrent_status) {
        this(libtorrent_jni.new_torrent_status__SWIG_1(getCPtr(torrent_status), torrent_status), true);
    }

    public boolean op_eq(torrent_status torrent_status) {
        return libtorrent_jni.torrent_status_op_eq(this.swigCPtr, this, getCPtr(torrent_status), torrent_status);
    }

    public void setHandle(torrent_handle torrent_handle) {
        libtorrent_jni.torrent_status_handle_set(this.swigCPtr, this, torrent_handle.getCPtr(torrent_handle), torrent_handle);
    }

    public torrent_handle getHandle() {
        long torrent_status_handle_get = libtorrent_jni.torrent_status_handle_get(this.swigCPtr, this);
        if (torrent_status_handle_get == 0) {
            return null;
        }
        return new torrent_handle(torrent_status_handle_get, false);
    }

    public void setErrc(error_code error_code) {
        libtorrent_jni.torrent_status_errc_set(this.swigCPtr, this, error_code.getCPtr(error_code), error_code);
    }

    public error_code getErrc() {
        long torrent_status_errc_get = libtorrent_jni.torrent_status_errc_get(this.swigCPtr, this);
        if (torrent_status_errc_get == 0) {
            return null;
        }
        return new error_code(torrent_status_errc_get, false);
    }

    public void setError_file(int i) {
        libtorrent_jni.torrent_status_error_file_set(this.swigCPtr, this, i);
    }

    public int getError_file() {
        return libtorrent_jni.torrent_status_error_file_get(this.swigCPtr, this);
    }

    public static int getError_file_none() {
        return libtorrent_jni.torrent_status_error_file_none_get();
    }

    public static int getError_file_url() {
        return libtorrent_jni.torrent_status_error_file_url_get();
    }

    public static int getError_file_ssl_ctx() {
        return libtorrent_jni.torrent_status_error_file_ssl_ctx_get();
    }

    public static int getError_file_metadata() {
        return libtorrent_jni.torrent_status_error_file_metadata_get();
    }

    public static int getError_file_exception() {
        return libtorrent_jni.torrent_status_error_file_exception_get();
    }

    public void setSave_path(String str) {
        libtorrent_jni.torrent_status_save_path_set(this.swigCPtr, this, str);
    }

    public String getSave_path() {
        return libtorrent_jni.torrent_status_save_path_get(this.swigCPtr, this);
    }

    public void setName(String str) {
        libtorrent_jni.torrent_status_name_set(this.swigCPtr, this, str);
    }

    public String getName() {
        return libtorrent_jni.torrent_status_name_get(this.swigCPtr, this);
    }

    public void setCurrent_tracker(String str) {
        libtorrent_jni.torrent_status_current_tracker_set(this.swigCPtr, this, str);
    }

    public String getCurrent_tracker() {
        return libtorrent_jni.torrent_status_current_tracker_get(this.swigCPtr, this);
    }

    public void setTotal_download(long j) {
        libtorrent_jni.torrent_status_total_download_set(this.swigCPtr, this, j);
    }

    public long getTotal_download() {
        return libtorrent_jni.torrent_status_total_download_get(this.swigCPtr, this);
    }

    public void setTotal_upload(long j) {
        libtorrent_jni.torrent_status_total_upload_set(this.swigCPtr, this, j);
    }

    public long getTotal_upload() {
        return libtorrent_jni.torrent_status_total_upload_get(this.swigCPtr, this);
    }

    public void setTotal_payload_download(long j) {
        libtorrent_jni.torrent_status_total_payload_download_set(this.swigCPtr, this, j);
    }

    public long getTotal_payload_download() {
        return libtorrent_jni.torrent_status_total_payload_download_get(this.swigCPtr, this);
    }

    public void setTotal_payload_upload(long j) {
        libtorrent_jni.torrent_status_total_payload_upload_set(this.swigCPtr, this, j);
    }

    public long getTotal_payload_upload() {
        return libtorrent_jni.torrent_status_total_payload_upload_get(this.swigCPtr, this);
    }

    public void setTotal_failed_bytes(long j) {
        libtorrent_jni.torrent_status_total_failed_bytes_set(this.swigCPtr, this, j);
    }

    public long getTotal_failed_bytes() {
        return libtorrent_jni.torrent_status_total_failed_bytes_get(this.swigCPtr, this);
    }

    public void setTotal_redundant_bytes(long j) {
        libtorrent_jni.torrent_status_total_redundant_bytes_set(this.swigCPtr, this, j);
    }

    public long getTotal_redundant_bytes() {
        return libtorrent_jni.torrent_status_total_redundant_bytes_get(this.swigCPtr, this);
    }

    public void setPieces(piece_index_bitfield piece_index_bitfield) {
        libtorrent_jni.torrent_status_pieces_set(this.swigCPtr, this, piece_index_bitfield.getCPtr(piece_index_bitfield), piece_index_bitfield);
    }

    public piece_index_bitfield getPieces() {
        long torrent_status_pieces_get = libtorrent_jni.torrent_status_pieces_get(this.swigCPtr, this);
        if (torrent_status_pieces_get == 0) {
            return null;
        }
        return new piece_index_bitfield(torrent_status_pieces_get, false);
    }

    public void setVerified_pieces(piece_index_bitfield piece_index_bitfield) {
        libtorrent_jni.torrent_status_verified_pieces_set(this.swigCPtr, this, piece_index_bitfield.getCPtr(piece_index_bitfield), piece_index_bitfield);
    }

    public piece_index_bitfield getVerified_pieces() {
        long torrent_status_verified_pieces_get = libtorrent_jni.torrent_status_verified_pieces_get(this.swigCPtr, this);
        if (torrent_status_verified_pieces_get == 0) {
            return null;
        }
        return new piece_index_bitfield(torrent_status_verified_pieces_get, false);
    }

    public void setTotal_done(long j) {
        libtorrent_jni.torrent_status_total_done_set(this.swigCPtr, this, j);
    }

    public long getTotal_done() {
        return libtorrent_jni.torrent_status_total_done_get(this.swigCPtr, this);
    }

    public void setTotal_wanted_done(long j) {
        libtorrent_jni.torrent_status_total_wanted_done_set(this.swigCPtr, this, j);
    }

    public long getTotal_wanted_done() {
        return libtorrent_jni.torrent_status_total_wanted_done_get(this.swigCPtr, this);
    }

    public void setTotal_wanted(long j) {
        libtorrent_jni.torrent_status_total_wanted_set(this.swigCPtr, this, j);
    }

    public long getTotal_wanted() {
        return libtorrent_jni.torrent_status_total_wanted_get(this.swigCPtr, this);
    }

    public void setAll_time_upload(long j) {
        libtorrent_jni.torrent_status_all_time_upload_set(this.swigCPtr, this, j);
    }

    public long getAll_time_upload() {
        return libtorrent_jni.torrent_status_all_time_upload_get(this.swigCPtr, this);
    }

    public void setAll_time_download(long j) {
        libtorrent_jni.torrent_status_all_time_download_set(this.swigCPtr, this, j);
    }

    public long getAll_time_download() {
        return libtorrent_jni.torrent_status_all_time_download_get(this.swigCPtr, this);
    }

    public void setAdded_time(long j) {
        libtorrent_jni.torrent_status_added_time_set(this.swigCPtr, this, j);
    }

    public long getAdded_time() {
        return libtorrent_jni.torrent_status_added_time_get(this.swigCPtr, this);
    }

    public void setCompleted_time(long j) {
        libtorrent_jni.torrent_status_completed_time_set(this.swigCPtr, this, j);
    }

    public long getCompleted_time() {
        return libtorrent_jni.torrent_status_completed_time_get(this.swigCPtr, this);
    }

    public void setLast_seen_complete(long j) {
        libtorrent_jni.torrent_status_last_seen_complete_set(this.swigCPtr, this, j);
    }

    public long getLast_seen_complete() {
        return libtorrent_jni.torrent_status_last_seen_complete_get(this.swigCPtr, this);
    }

    public void setStorage_mode(storage_mode_t storage_mode_t) {
        libtorrent_jni.torrent_status_storage_mode_set(this.swigCPtr, this, storage_mode_t.swigValue());
    }

    public storage_mode_t getStorage_mode() {
        return storage_mode_t.swigToEnum(libtorrent_jni.torrent_status_storage_mode_get(this.swigCPtr, this));
    }

    public void setProgress(float f) {
        libtorrent_jni.torrent_status_progress_set(this.swigCPtr, this, f);
    }

    public float getProgress() {
        return libtorrent_jni.torrent_status_progress_get(this.swigCPtr, this);
    }

    public void setProgress_ppm(int i) {
        libtorrent_jni.torrent_status_progress_ppm_set(this.swigCPtr, this, i);
    }

    public int getProgress_ppm() {
        return libtorrent_jni.torrent_status_progress_ppm_get(this.swigCPtr, this);
    }

    public void setQueue_position(int i) {
        libtorrent_jni.torrent_status_queue_position_set(this.swigCPtr, this, i);
    }

    public int getQueue_position() {
        return libtorrent_jni.torrent_status_queue_position_get(this.swigCPtr, this);
    }

    public void setDownload_rate(int i) {
        libtorrent_jni.torrent_status_download_rate_set(this.swigCPtr, this, i);
    }

    public int getDownload_rate() {
        return libtorrent_jni.torrent_status_download_rate_get(this.swigCPtr, this);
    }

    public void setUpload_rate(int i) {
        libtorrent_jni.torrent_status_upload_rate_set(this.swigCPtr, this, i);
    }

    public int getUpload_rate() {
        return libtorrent_jni.torrent_status_upload_rate_get(this.swigCPtr, this);
    }

    public void setDownload_payload_rate(int i) {
        libtorrent_jni.torrent_status_download_payload_rate_set(this.swigCPtr, this, i);
    }

    public int getDownload_payload_rate() {
        return libtorrent_jni.torrent_status_download_payload_rate_get(this.swigCPtr, this);
    }

    public void setUpload_payload_rate(int i) {
        libtorrent_jni.torrent_status_upload_payload_rate_set(this.swigCPtr, this, i);
    }

    public int getUpload_payload_rate() {
        return libtorrent_jni.torrent_status_upload_payload_rate_get(this.swigCPtr, this);
    }

    public void setNum_seeds(int i) {
        libtorrent_jni.torrent_status_num_seeds_set(this.swigCPtr, this, i);
    }

    public int getNum_seeds() {
        return libtorrent_jni.torrent_status_num_seeds_get(this.swigCPtr, this);
    }

    public void setNum_peers(int i) {
        libtorrent_jni.torrent_status_num_peers_set(this.swigCPtr, this, i);
    }

    public int getNum_peers() {
        return libtorrent_jni.torrent_status_num_peers_get(this.swigCPtr, this);
    }

    public void setNum_complete(int i) {
        libtorrent_jni.torrent_status_num_complete_set(this.swigCPtr, this, i);
    }

    public int getNum_complete() {
        return libtorrent_jni.torrent_status_num_complete_get(this.swigCPtr, this);
    }

    public void setNum_incomplete(int i) {
        libtorrent_jni.torrent_status_num_incomplete_set(this.swigCPtr, this, i);
    }

    public int getNum_incomplete() {
        return libtorrent_jni.torrent_status_num_incomplete_get(this.swigCPtr, this);
    }

    public void setList_seeds(int i) {
        libtorrent_jni.torrent_status_list_seeds_set(this.swigCPtr, this, i);
    }

    public int getList_seeds() {
        return libtorrent_jni.torrent_status_list_seeds_get(this.swigCPtr, this);
    }

    public void setList_peers(int i) {
        libtorrent_jni.torrent_status_list_peers_set(this.swigCPtr, this, i);
    }

    public int getList_peers() {
        return libtorrent_jni.torrent_status_list_peers_get(this.swigCPtr, this);
    }

    public void setConnect_candidates(int i) {
        libtorrent_jni.torrent_status_connect_candidates_set(this.swigCPtr, this, i);
    }

    public int getConnect_candidates() {
        return libtorrent_jni.torrent_status_connect_candidates_get(this.swigCPtr, this);
    }

    public void setNum_pieces(int i) {
        libtorrent_jni.torrent_status_num_pieces_set(this.swigCPtr, this, i);
    }

    public int getNum_pieces() {
        return libtorrent_jni.torrent_status_num_pieces_get(this.swigCPtr, this);
    }

    public void setDistributed_full_copies(int i) {
        libtorrent_jni.torrent_status_distributed_full_copies_set(this.swigCPtr, this, i);
    }

    public int getDistributed_full_copies() {
        return libtorrent_jni.torrent_status_distributed_full_copies_get(this.swigCPtr, this);
    }

    public void setDistributed_fraction(int i) {
        libtorrent_jni.torrent_status_distributed_fraction_set(this.swigCPtr, this, i);
    }

    public int getDistributed_fraction() {
        return libtorrent_jni.torrent_status_distributed_fraction_get(this.swigCPtr, this);
    }

    public void setDistributed_copies(float f) {
        libtorrent_jni.torrent_status_distributed_copies_set(this.swigCPtr, this, f);
    }

    public float getDistributed_copies() {
        return libtorrent_jni.torrent_status_distributed_copies_get(this.swigCPtr, this);
    }

    public void setBlock_size(int i) {
        libtorrent_jni.torrent_status_block_size_set(this.swigCPtr, this, i);
    }

    public int getBlock_size() {
        return libtorrent_jni.torrent_status_block_size_get(this.swigCPtr, this);
    }

    public void setNum_uploads(int i) {
        libtorrent_jni.torrent_status_num_uploads_set(this.swigCPtr, this, i);
    }

    public int getNum_uploads() {
        return libtorrent_jni.torrent_status_num_uploads_get(this.swigCPtr, this);
    }

    public void setNum_connections(int i) {
        libtorrent_jni.torrent_status_num_connections_set(this.swigCPtr, this, i);
    }

    public int getNum_connections() {
        return libtorrent_jni.torrent_status_num_connections_get(this.swigCPtr, this);
    }

    public void setUploads_limit(int i) {
        libtorrent_jni.torrent_status_uploads_limit_set(this.swigCPtr, this, i);
    }

    public int getUploads_limit() {
        return libtorrent_jni.torrent_status_uploads_limit_get(this.swigCPtr, this);
    }

    public void setConnections_limit(int i) {
        libtorrent_jni.torrent_status_connections_limit_set(this.swigCPtr, this, i);
    }

    public int getConnections_limit() {
        return libtorrent_jni.torrent_status_connections_limit_get(this.swigCPtr, this);
    }

    public void setUp_bandwidth_queue(int i) {
        libtorrent_jni.torrent_status_up_bandwidth_queue_set(this.swigCPtr, this, i);
    }

    public int getUp_bandwidth_queue() {
        return libtorrent_jni.torrent_status_up_bandwidth_queue_get(this.swigCPtr, this);
    }

    public void setDown_bandwidth_queue(int i) {
        libtorrent_jni.torrent_status_down_bandwidth_queue_set(this.swigCPtr, this, i);
    }

    public int getDown_bandwidth_queue() {
        return libtorrent_jni.torrent_status_down_bandwidth_queue_get(this.swigCPtr, this);
    }

    public void setSeed_rank(int i) {
        libtorrent_jni.torrent_status_seed_rank_set(this.swigCPtr, this, i);
    }

    public int getSeed_rank() {
        return libtorrent_jni.torrent_status_seed_rank_get(this.swigCPtr, this);
    }

    public void setState(state_t state_t) {
        libtorrent_jni.torrent_status_state_set(this.swigCPtr, this, state_t.swigValue());
    }

    public state_t getState() {
        return state_t.swigToEnum(libtorrent_jni.torrent_status_state_get(this.swigCPtr, this));
    }

    public void setNeed_save_resume(boolean z) {
        libtorrent_jni.torrent_status_need_save_resume_set(this.swigCPtr, this, z);
    }

    public boolean getNeed_save_resume() {
        return libtorrent_jni.torrent_status_need_save_resume_get(this.swigCPtr, this);
    }

    public void setIs_seeding(boolean z) {
        libtorrent_jni.torrent_status_is_seeding_set(this.swigCPtr, this, z);
    }

    public boolean getIs_seeding() {
        return libtorrent_jni.torrent_status_is_seeding_get(this.swigCPtr, this);
    }

    public void setIs_finished(boolean z) {
        libtorrent_jni.torrent_status_is_finished_set(this.swigCPtr, this, z);
    }

    public boolean getIs_finished() {
        return libtorrent_jni.torrent_status_is_finished_get(this.swigCPtr, this);
    }

    public void setHas_metadata(boolean z) {
        libtorrent_jni.torrent_status_has_metadata_set(this.swigCPtr, this, z);
    }

    public boolean getHas_metadata() {
        return libtorrent_jni.torrent_status_has_metadata_get(this.swigCPtr, this);
    }

    public void setHas_incoming(boolean z) {
        libtorrent_jni.torrent_status_has_incoming_set(this.swigCPtr, this, z);
    }

    public boolean getHas_incoming() {
        return libtorrent_jni.torrent_status_has_incoming_get(this.swigCPtr, this);
    }

    public void setMoving_storage(boolean z) {
        libtorrent_jni.torrent_status_moving_storage_set(this.swigCPtr, this, z);
    }

    public boolean getMoving_storage() {
        return libtorrent_jni.torrent_status_moving_storage_get(this.swigCPtr, this);
    }

    public void setAnnouncing_to_trackers(boolean z) {
        libtorrent_jni.torrent_status_announcing_to_trackers_set(this.swigCPtr, this, z);
    }

    public boolean getAnnouncing_to_trackers() {
        return libtorrent_jni.torrent_status_announcing_to_trackers_get(this.swigCPtr, this);
    }

    public void setAnnouncing_to_lsd(boolean z) {
        libtorrent_jni.torrent_status_announcing_to_lsd_set(this.swigCPtr, this, z);
    }

    public boolean getAnnouncing_to_lsd() {
        return libtorrent_jni.torrent_status_announcing_to_lsd_get(this.swigCPtr, this);
    }

    public void setAnnouncing_to_dht(boolean z) {
        libtorrent_jni.torrent_status_announcing_to_dht_set(this.swigCPtr, this, z);
    }

    public boolean getAnnouncing_to_dht() {
        return libtorrent_jni.torrent_status_announcing_to_dht_get(this.swigCPtr, this);
    }

    public void setInfo_hash(sha1_hash sha1_hash) {
        libtorrent_jni.torrent_status_info_hash_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getInfo_hash() {
        long torrent_status_info_hash_get = libtorrent_jni.torrent_status_info_hash_get(this.swigCPtr, this);
        if (torrent_status_info_hash_get == 0) {
            return null;
        }
        return new sha1_hash(torrent_status_info_hash_get, false);
    }

    public void setFlags(torrent_flags_t torrent_flags_t) {
        libtorrent_jni.torrent_status_flags_set(this.swigCPtr, this, torrent_flags_t.getCPtr(torrent_flags_t), torrent_flags_t);
    }

    public torrent_flags_t getFlags() {
        long torrent_status_flags_get = libtorrent_jni.torrent_status_flags_get(this.swigCPtr, this);
        if (torrent_status_flags_get == 0) {
            return null;
        }
        return new torrent_flags_t(torrent_status_flags_get, false);
    }

    public torrent_info torrent_file_ptr() {
        long torrent_status_torrent_file_ptr = libtorrent_jni.torrent_status_torrent_file_ptr(this.swigCPtr, this);
        if (torrent_status_torrent_file_ptr == 0) {
            return null;
        }
        return new torrent_info(torrent_status_torrent_file_ptr, false);
    }

    public long get_next_announce() {
        return libtorrent_jni.torrent_status_get_next_announce(this.swigCPtr, this);
    }

    public long get_last_upload() {
        return libtorrent_jni.torrent_status_get_last_upload(this.swigCPtr, this);
    }

    public long get_last_download() {
        return libtorrent_jni.torrent_status_get_last_download(this.swigCPtr, this);
    }

    public long get_active_duration() {
        return libtorrent_jni.torrent_status_get_active_duration(this.swigCPtr, this);
    }

    public long get_finished_duration() {
        return libtorrent_jni.torrent_status_get_finished_duration(this.swigCPtr, this);
    }

    public long get_seeding_duration() {
        return libtorrent_jni.torrent_status_get_seeding_duration(this.swigCPtr, this);
    }
}
