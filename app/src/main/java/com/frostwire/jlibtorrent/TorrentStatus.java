package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_status;
import com.frostwire.jlibtorrent.swig.torrent_status.state_t;

public final class TorrentStatus implements Cloneable {
    private final torrent_status ts;

    public enum State {
        CHECKING_FILES(state_t.checking_files.swigValue()),
        DOWNLOADING_METADATA(state_t.downloading_metadata.swigValue()),
        DOWNLOADING(state_t.downloading.swigValue()),
        FINISHED(state_t.finished.swigValue()),
        SEEDING(state_t.seeding.swigValue()),
        ALLOCATING(state_t.allocating.swigValue()),
        CHECKING_RESUME_DATA(state_t.checking_resume_data.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private State(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static State fromSwig(int i) {
            for (State state : (State[]) State.class.getEnumConstants()) {
                if (state.swig() == i) {
                    return state;
                }
            }
            return UNKNOWN;
        }
    }

    private static long time2millis(long j) {
        return j * 1000;
    }

    public TorrentStatus(torrent_status torrent_status) {
        this.ts = torrent_status;
    }

    public torrent_status swig() {
        return this.ts;
    }

    public ErrorCode errorCode() {
        return new ErrorCode(this.ts.getErrc());
    }

    public String name() {
        return this.ts.getName();
    }

    public long nextAnnounce() {
        return this.ts.get_next_announce();
    }

    public String currentTracker() {
        return this.ts.getCurrent_tracker();
    }

    public long totalDownload() {
        return this.ts.getTotal_download();
    }

    public long totalUpload() {
        return this.ts.getTotal_upload();
    }

    public long totalPayloadDownload() {
        return this.ts.getTotal_payload_download();
    }

    public long totalPayloadUpload() {
        return this.ts.getTotal_payload_upload();
    }

    public long totalFailedBytes() {
        return this.ts.getTotal_failed_bytes();
    }

    public long totalRedundantBytes() {
        return this.ts.getTotal_redundant_bytes();
    }

    public PieceIndexBitfield pieces() {
        return new PieceIndexBitfield(this.ts.getPieces(), this.ts);
    }

    public PieceIndexBitfield verifiedPieces() {
        return new PieceIndexBitfield(this.ts.getVerified_pieces(), this.ts);
    }

    public long totalDone() {
        return this.ts.getTotal_done();
    }

    public long totalWantedDone() {
        return this.ts.getTotal_wanted_done();
    }

    public long totalWanted() {
        return this.ts.getTotal_wanted();
    }

    public long allTimeUpload() {
        return this.ts.getAll_time_upload();
    }

    public long allTimeDownload() {
        return this.ts.getAll_time_download();
    }

    public long addedTime() {
        return time2millis(this.ts.getAdded_time());
    }

    public long completedTime() {
        return time2millis(this.ts.getCompleted_time());
    }

    public long lastSeenComplete() {
        return time2millis(this.ts.getLast_seen_complete());
    }

    public final StorageMode storageMode() {
        return StorageMode.fromSwig(this.ts.getStorage_mode().swigValue());
    }

    public float progress() {
        return this.ts.getProgress();
    }

    public int progressPpm() {
        return this.ts.getProgress_ppm();
    }

    public int queuePosition() {
        return this.ts.getQueue_position();
    }

    public int downloadRate() {
        return this.ts.getDownload_rate();
    }

    public int uploadRate() {
        return this.ts.getUpload_rate();
    }

    public int downloadPayloadRate() {
        return this.ts.getDownload_payload_rate();
    }

    public int uploadPayloadRate() {
        return this.ts.getUpload_payload_rate();
    }

    public int numSeeds() {
        return this.ts.getNum_seeds();
    }

    public int numPeers() {
        return this.ts.getNum_peers();
    }

    public int numComplete() {
        return this.ts.getNum_complete();
    }

    public int numIncomplete() {
        return this.ts.getNum_incomplete();
    }

    public int listSeeds() {
        return this.ts.getList_seeds();
    }

    public int listPeers() {
        return this.ts.getList_peers();
    }

    public int connectCandidates() {
        return this.ts.getConnect_candidates();
    }

    public int numPieces() {
        return this.ts.getNum_pieces();
    }

    public int distributedFullCopies() {
        return this.ts.getDistributed_full_copies();
    }

    public int distributedFraction() {
        return this.ts.getDistributed_fraction();
    }

    public float distributedCopies() {
        return this.ts.getDistributed_copies();
    }

    public int blockSize() {
        return this.ts.getBlock_size();
    }

    public int numUploads() {
        return this.ts.getNum_uploads();
    }

    public int numConnections() {
        return this.ts.getNum_connections();
    }

    public int uploadsLimit() {
        return this.ts.getUploads_limit();
    }

    public int connectionsLimit() {
        return this.ts.getConnections_limit();
    }

    public int upBandwidthQueue() {
        return this.ts.getUp_bandwidth_queue();
    }

    public int downBandwidthQueue() {
        return this.ts.getDown_bandwidth_queue();
    }

    public int seedRank() {
        return this.ts.getSeed_rank();
    }

    public State state() {
        return State.fromSwig(this.ts.getState().swigValue());
    }

    public boolean needSaveResume() {
        return this.ts.getNeed_save_resume();
    }

    public boolean isSeeding() {
        return this.ts.getIs_seeding();
    }

    public boolean isFinished() {
        return this.ts.getIs_finished();
    }

    public boolean hasMetadata() {
        return this.ts.getHas_metadata();
    }

    public boolean hasIncoming() {
        return this.ts.getHas_incoming();
    }

    public boolean isMovingStorage() {
        return this.ts.getMoving_storage();
    }

    public boolean announcingToTrackers() {
        return this.ts.getAnnouncing_to_trackers();
    }

    public boolean announcingToLsd() {
        return this.ts.getAnnouncing_to_lsd();
    }

    public boolean announcingToDht() {
        return this.ts.getAnnouncing_to_dht();
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(this.ts.getInfo_hash());
    }

    public long lastUpload() {
        return this.ts.get_last_upload();
    }

    public long lastDownload() {
        return this.ts.get_last_download();
    }

    public long activeDuration() {
        return this.ts.get_active_duration();
    }

    public long finishedDuration() {
        return this.ts.get_finished_duration();
    }

    public long seedingDuration() {
        return this.ts.get_seeding_duration();
    }

    public torrent_flags_t flags() {
        return this.ts.getFlags();
    }

    protected TorrentStatus clone() {
        return new TorrentStatus(new torrent_status(this.ts));
    }
}
