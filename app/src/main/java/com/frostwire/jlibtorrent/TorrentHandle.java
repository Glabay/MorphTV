package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_piece_flags_t;
import com.frostwire.jlibtorrent.swig.announce_entry_vector;
import com.frostwire.jlibtorrent.swig.deadline_flags_t;
import com.frostwire.jlibtorrent.swig.int64_vector;
import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.partial_piece_info_vector;
import com.frostwire.jlibtorrent.swig.peer_info_vector;
import com.frostwire.jlibtorrent.swig.status_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_handle.file_progress_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_info;
import java.util.ArrayList;
import java.util.List;

public final class TorrentHandle {
    public static final deadline_flags_t ALERT_WHEN_AVAILABLE = torrent_handle.alert_when_available;
    public static final add_piece_flags_t OVERWRITE_EXISTING = torrent_handle.overwrite_existing;
    public static final status_flags_t QUERY_ACCURATE_DOWNLOAD_COUNTERS = torrent_handle.query_accurate_download_counters;
    public static final status_flags_t QUERY_DISTRIBUTED_COPIES = torrent_handle.query_distributed_copies;
    public static final status_flags_t QUERY_LAST_SEEN_COMPLETE = torrent_handle.query_last_seen_complete;
    public static final status_flags_t QUERY_NAME = torrent_handle.query_name;
    public static final status_flags_t QUERY_PIECES = torrent_handle.query_pieces;
    public static final status_flags_t QUERY_SAVE_PATH = torrent_handle.query_save_path;
    public static final status_flags_t QUERY_TORRENT_FILE = torrent_handle.query_torrent_file;
    public static final status_flags_t QUERY_VERIFIED_PIECES = torrent_handle.query_verified_pieces;
    private static final long REQUEST_STATUS_RESOLUTION_MILLIS = 500;
    private static final status_flags_t STATUS_FLAGS_ZERO = new status_flags_t();
    private TorrentStatus lastStatus;
    private long lastStatusRequestTime;
    private final torrent_handle th;

    public enum FileProgressFlags {
        PIECE_GRANULARITY(file_progress_flags_t.piece_granularity.swigValue());
        
        private final int swigValue;

        private FileProgressFlags(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }
    }

    public TorrentHandle(torrent_handle torrent_handle) {
        this.th = torrent_handle;
    }

    public torrent_handle swig() {
        return this.th;
    }

    public void addPiece(int i, byte[] bArr, add_piece_flags_t add_piece_flags_t) {
        this.th.add_piece_bytes(i, Vectors.bytes2byte_vector(bArr), add_piece_flags_t);
    }

    public void addPiece(int i, byte[] bArr) {
        this.th.add_piece_bytes(i, Vectors.bytes2byte_vector(bArr));
    }

    public void readPiece(int i) {
        this.th.read_piece(i);
    }

    public boolean havePiece(int i) {
        return this.th.have_piece(i);
    }

    public ArrayList<PeerInfo> peerInfo() {
        if (!this.th.is_valid()) {
            return new ArrayList();
        }
        peer_info_vector peer_info_vector = new peer_info_vector();
        this.th.get_peer_info(peer_info_vector);
        int size = (int) peer_info_vector.size();
        ArrayList<PeerInfo> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new PeerInfo(peer_info_vector.get(i)));
        }
        return arrayList;
    }

    public TorrentInfo torrentFile() {
        TorrentInfo torrentInfo = null;
        if (!this.th.is_valid()) {
            return null;
        }
        torrent_info torrent_file_ptr = this.th.torrent_file_ptr();
        if (torrent_file_ptr != null) {
            torrentInfo = new TorrentInfo(torrent_file_ptr);
        }
        return torrentInfo;
    }

    public TorrentStatus status(boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        if (z || currentTimeMillis - this.lastStatusRequestTime >= 500) {
            this.lastStatusRequestTime = currentTimeMillis;
            this.lastStatus = new TorrentStatus(this.th.status(STATUS_FLAGS_ZERO));
        }
        return this.lastStatus;
    }

    public TorrentStatus status() {
        return status(false);
    }

    public TorrentStatus status(status_flags_t status_flags_t) {
        return new TorrentStatus(this.th.status(status_flags_t));
    }

    public ArrayList<PartialPieceInfo> getDownloadQueue() {
        partial_piece_info_vector partial_piece_info_vector = new partial_piece_info_vector();
        this.th.get_download_queue(partial_piece_info_vector);
        int size = (int) partial_piece_info_vector.size();
        ArrayList<PartialPieceInfo> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new PartialPieceInfo(partial_piece_info_vector.get(i)));
        }
        return arrayList;
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(this.th.info_hash());
    }

    public void pause() {
        this.th.pause();
    }

    public void resume() {
        this.th.resume();
    }

    public torrent_flags_t flags() {
        return this.th.flags();
    }

    public void setFlags(torrent_flags_t torrent_flags_t, torrent_flags_t torrent_flags_t2) {
        this.th.set_flags(torrent_flags_t, torrent_flags_t2);
    }

    public void setFlags(torrent_flags_t torrent_flags_t) {
        this.th.set_flags(torrent_flags_t);
    }

    public void unsetFlags(torrent_flags_t torrent_flags_t) {
        this.th.unset_flags(torrent_flags_t);
    }

    public void flushCache() {
        this.th.flush_cache();
    }

    public boolean needSaveResumeData() {
        return this.th.need_save_resume_data();
    }

    public int getQueuePosition() {
        return this.th.queue_position();
    }

    public void queuePositionUp() {
        this.th.queue_position_up();
    }

    public void queuePositionDown() {
        this.th.queue_position_down();
    }

    public void queuePositionTop() {
        this.th.queue_position_top();
    }

    public void queuePositionBottom() {
        this.th.queue_position_bottom();
    }

    public void saveResumeData() {
        this.th.save_resume_data(torrent_handle.save_info_dict);
    }

    public boolean isValid() {
        return this.th.is_valid();
    }

    public String makeMagnetUri() {
        return this.th.is_valid() ? libtorrent.make_magnet_uri(this.th) : null;
    }

    public int getUploadLimit() {
        return this.th.upload_limit();
    }

    public void setUploadLimit(int i) {
        this.th.set_upload_limit(i);
    }

    public int getDownloadLimit() {
        return this.th.download_limit();
    }

    public void setDownloadLimit(int i) {
        this.th.set_download_limit(i);
    }

    public void forceRecheck() {
        this.th.force_recheck();
    }

    public void forceReannounce(int i, int i2) {
        this.th.force_reannounce(i, i2);
    }

    public void forceReannounce(int i) {
        this.th.force_reannounce(i);
    }

    public void forceReannounce() {
        this.th.force_reannounce();
    }

    public void forceDHTAnnounce() {
        this.th.force_dht_announce();
    }

    public List<AnnounceEntry> trackers() {
        return TorrentInfo.trackers(this.th.trackers());
    }

    public void scrapeTracker() {
        this.th.scrape_tracker();
    }

    public void replaceTrackers(List<AnnounceEntry> list) {
        announce_entry_vector announce_entry_vector = new announce_entry_vector();
        for (AnnounceEntry swig : list) {
            announce_entry_vector.push_back(swig.swig());
        }
        this.th.replace_trackers(announce_entry_vector);
    }

    public void addTracker(AnnounceEntry announceEntry) {
        this.th.add_tracker(announceEntry.swig());
    }

    public void addUrlSeed(String str) {
        this.th.add_url_seed(str);
    }

    public void removeUrlSeed(String str) {
        this.th.remove_url_seed(str);
    }

    public List<String> urlSeeds() {
        return Vectors.string_vector2list(this.th.get_url_seeds());
    }

    public void addHttpSeed(String str) {
        this.th.add_url_seed(str);
    }

    public void removeHttpSeed(String str) {
        this.th.remove_http_seed(str);
    }

    public List<String> httpSeeds() {
        return Vectors.string_vector2list(this.th.get_http_seeds());
    }

    public int[] getPieceAvailability() {
        int_vector int_vector = new int_vector();
        this.th.piece_availability(int_vector);
        return Vectors.int_vector2ints(int_vector);
    }

    public void piecePriority(int i, Priority priority) {
        this.th.piece_priority(i, priority.swig());
    }

    public Priority piecePriority(int i) {
        return Priority.fromSwig(this.th.piece_priority(i));
    }

    public void prioritizePieces(Priority[] priorityArr) {
        this.th.prioritize_pieces(Priority.array2int_vector(priorityArr));
    }

    public Priority[] getPiecePriorities() {
        int_vector piece_priorities = this.th.piece_priorities();
        int size = (int) piece_priorities.size();
        Priority[] priorityArr = new Priority[size];
        for (int i = 0; i < size; i++) {
            priorityArr[i] = Priority.fromSwig(piece_priorities.get(i));
        }
        return priorityArr;
    }

    public void setFilePriority(int i, Priority priority) {
        this.th.file_priority(i, priority.swig());
    }

    public Priority getFilePriority(int i) {
        return Priority.fromSwig(this.th.file_priority(i));
    }

    public void prioritizeFiles(Priority[] priorityArr) {
        this.th.prioritize_files(Priority.array2int_vector(priorityArr));
    }

    public Priority[] filePriorities() {
        int_vector file_priorities = this.th.file_priorities();
        int size = (int) file_priorities.size();
        Priority[] priorityArr = new Priority[size];
        for (int i = 0; i < size; i++) {
            priorityArr[i] = Priority.fromSwig(file_priorities.get(i));
        }
        return priorityArr;
    }

    public void setPieceDeadline(int i, int i2) {
        this.th.set_piece_deadline(i, i2);
    }

    public void setPieceDeadline(int i, int i2, deadline_flags_t deadline_flags_t) {
        this.th.set_piece_deadline(i, i2, deadline_flags_t);
    }

    public void resetPieceDeadline(int i) {
        this.th.reset_piece_deadline(i);
    }

    public void clearPieceDeadlines() {
        this.th.clear_piece_deadlines();
    }

    public long[] fileProgress(FileProgressFlags fileProgressFlags) {
        int64_vector int64_vector = new int64_vector();
        this.th.file_progress(int64_vector, fileProgressFlags.swig());
        return Vectors.int64_vector2longs(int64_vector);
    }

    public long[] fileProgress() {
        int64_vector int64_vector = new int64_vector();
        this.th.file_progress(int64_vector);
        return Vectors.int64_vector2longs(int64_vector);
    }

    public String savePath() {
        return this.th.status(torrent_handle.query_save_path).getSave_path();
    }

    public String name() {
        return this.th.status(torrent_handle.query_name).getName();
    }

    public void moveStorage(String str, MoveFlags moveFlags) {
        this.th.move_storage(str, moveFlags.swig());
    }

    public void moveStorage(String str) {
        this.th.move_storage(str);
    }

    public void renameFile(int i, String str) {
        this.th.rename_file(i, str);
    }
}
