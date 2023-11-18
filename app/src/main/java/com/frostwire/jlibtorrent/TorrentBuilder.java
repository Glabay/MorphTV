package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_files_listener;
import com.frostwire.jlibtorrent.swig.create_flags_t;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.file_storage;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.set_piece_hashes_listener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class TorrentBuilder {
    public static final create_flags_t MERKLE = create_torrent.merkle;
    public static final create_flags_t MODIFICATION_TIME = create_torrent.modification_time;
    public static final create_flags_t MUTABLE_TORRENT_SUPPORT = create_torrent.mutable_torrent_support;
    public static final create_flags_t OPTIMIZE_ALIGNMENT = create_torrent.optimize_alignment;
    public static final create_flags_t SYMLINKS = create_torrent.symlinks;
    private int alignment = -1;
    private List<String> collections = new LinkedList();
    private String comment;
    private String creator;
    private create_flags_t flags = OPTIMIZE_ALIGNMENT;
    private List<String> httpSeeds = new LinkedList();
    private Listener listener;
    private List<Pair<String, Integer>> nodes = new LinkedList();
    private int padFileLimit = -1;
    private File path;
    private int pieceSize = 0;
    private boolean priv;
    private List<Sha1Hash> similarTorrents = new LinkedList();
    private List<Pair<String, Integer>> trackers = new LinkedList();
    private List<String> urlSeeds = new LinkedList();

    /* renamed from: com.frostwire.jlibtorrent.TorrentBuilder$1 */
    class C06071 extends add_files_listener {
        C06071() {
        }

        public boolean pred(String str) {
            return TorrentBuilder.this.listener != null ? TorrentBuilder.this.listener.accept(str) : true;
        }
    }

    public interface Listener {
        boolean accept(String str);

        void progress(int i, int i2);
    }

    public static final class Result {
        private final Entry entry;
        /* renamed from: t */
        private final create_torrent f34t;

        private Result(create_torrent create_torrent) {
            this.f34t = create_torrent;
            this.entry = new Entry(create_torrent.generate());
        }

        public Entry entry() {
            return this.entry;
        }

        public int numPieces() {
            return this.f34t.num_pieces();
        }

        public int pieceLength() {
            return this.f34t.piece_length();
        }

        public int pieceSize(int i) {
            return this.f34t.piece_size(i);
        }

        public ArrayList<Sha1Hash> merkleTree() {
            return Sha1Hash.convert(this.f34t.merkle_tree());
        }
    }

    public File path() {
        return this.path;
    }

    public TorrentBuilder path(File file) {
        this.path = file;
        return this;
    }

    public int pieceSize() {
        return this.pieceSize;
    }

    public TorrentBuilder pieceSize(int i) {
        this.pieceSize = i;
        return this;
    }

    public int padFileLimit() {
        return this.padFileLimit;
    }

    public TorrentBuilder padFileLimit(int i) {
        this.padFileLimit = i;
        return this;
    }

    public create_flags_t flags() {
        return this.flags;
    }

    public TorrentBuilder flags(create_flags_t create_flags_t) {
        this.flags = create_flags_t;
        return this;
    }

    public int alignment() {
        return this.alignment;
    }

    public TorrentBuilder alignment(int i) {
        this.alignment = i;
        return this;
    }

    public String comment() {
        return this.comment;
    }

    public TorrentBuilder comment(String str) {
        this.comment = str;
        return this;
    }

    public String creator() {
        return this.creator;
    }

    public TorrentBuilder creator(String str) {
        this.creator = str;
        return this;
    }

    public List<String> urlSeeds() {
        return this.urlSeeds;
    }

    public TorrentBuilder addUrlSeeds(List<String> list) {
        if (list != null) {
            this.urlSeeds.addAll(list);
        }
        return this;
    }

    public TorrentBuilder addUrlSeed(String str) {
        if (str != null) {
            this.urlSeeds.add(str);
        }
        return this;
    }

    public List<String> httpSeeds() {
        return this.httpSeeds;
    }

    public TorrentBuilder addHttpSeeds(List<String> list) {
        if (list != null) {
            this.httpSeeds.addAll(list);
        }
        return this;
    }

    public TorrentBuilder addHttpSeed(String str) {
        if (str != null) {
            this.httpSeeds.add(str);
        }
        return this;
    }

    public List<Pair<String, Integer>> nodes() {
        return this.nodes;
    }

    public TorrentBuilder addNodes(List<Pair<String, Integer>> list) {
        if (list != null) {
            this.nodes.addAll(list);
        }
        return this;
    }

    public TorrentBuilder addNode(Pair<String, Integer> pair) {
        if (pair != null) {
            this.nodes.add(pair);
        }
        return this;
    }

    public List<Pair<String, Integer>> trackers() {
        return this.trackers;
    }

    public TorrentBuilder addTrackers(List<Pair<String, Integer>> list) {
        if (list != null) {
            this.trackers.addAll(list);
        }
        return this;
    }

    public TorrentBuilder addTracker(Pair<String, Integer> pair) {
        if (pair != null) {
            this.trackers.add(pair);
        }
        return this;
    }

    public TorrentBuilder addTracker(String str, int i) {
        return addTracker(new Pair(str, Integer.valueOf(i)));
    }

    public TorrentBuilder addTracker(String str) {
        return addTracker(str, 0);
    }

    public boolean isPrivate() {
        return this.priv;
    }

    public TorrentBuilder setPrivate(boolean z) {
        this.priv = z;
        return this;
    }

    public List<Sha1Hash> similarTorrents() {
        return this.similarTorrents;
    }

    public TorrentBuilder addSimilarTorrents(List<Sha1Hash> list) {
        if (list != null) {
            this.similarTorrents.addAll(list);
        }
        return this;
    }

    public TorrentBuilder addSimilarTorrent(Sha1Hash sha1Hash) {
        if (sha1Hash != null) {
            this.similarTorrents.add(sha1Hash);
        }
        return this;
    }

    public List<String> collections() {
        return this.collections;
    }

    public TorrentBuilder addCollections(List<String> list) {
        if (list != null) {
            this.collections.addAll(list);
        }
        return this;
    }

    public TorrentBuilder addCollection(String str) {
        if (str != null) {
            this.collections.add(str);
        }
        return this;
    }

    public Listener listener() {
        return this.listener;
    }

    public TorrentBuilder listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public Result generate() throws IOException {
        if (this.path == null) {
            throw new IOException("path can't be null");
        }
        File absoluteFile = this.path.getAbsoluteFile();
        file_storage file_storage = new file_storage();
        libtorrent.add_files_ex(file_storage, absoluteFile.getPath(), new C06071(), this.flags);
        if (file_storage.total_size() == 0) {
            throw new IOException("content total size can't be 0");
        }
        create_torrent create_torrent = new create_torrent(file_storage, this.pieceSize, this.padFileLimit, this.flags, this.alignment);
        final int num_pieces = create_torrent.num_pieces();
        set_piece_hashes_listener c06082 = new set_piece_hashes_listener() {
            public void progress(int i) {
                if (TorrentBuilder.this.listener != null) {
                    TorrentBuilder.this.listener.progress(i, num_pieces);
                }
            }
        };
        absoluteFile = absoluteFile.getParentFile();
        if (absoluteFile == null) {
            throw new IOException("path's parent can't be null");
        }
        error_code error_code = new error_code();
        libtorrent.set_piece_hashes_ex(create_torrent, absoluteFile.getAbsolutePath(), c06082, error_code);
        if (error_code.value() != 0) {
            throw new IOException(error_code.message());
        }
        if (this.comment != null) {
            create_torrent.set_comment(this.comment);
        }
        if (this.creator != null) {
            create_torrent.set_creator(this.creator);
        }
        for (String add_url_seed : this.urlSeeds) {
            create_torrent.add_url_seed(add_url_seed);
        }
        for (String add_url_seed2 : this.httpSeeds) {
            create_torrent.add_http_seed(add_url_seed2);
        }
        for (Pair to_string_int_pair : this.nodes) {
            create_torrent.add_node(to_string_int_pair.to_string_int_pair());
        }
        for (Pair to_string_int_pair2 : this.trackers) {
            create_torrent.add_tracker((String) to_string_int_pair2.first, ((Integer) to_string_int_pair2.second).intValue());
        }
        if (this.priv) {
            create_torrent.set_priv(this.priv);
        }
        if (!this.similarTorrents.isEmpty()) {
            for (Sha1Hash swig : this.similarTorrents) {
                create_torrent.add_similar_torrent(swig.swig());
            }
        }
        if (!this.collections.isEmpty()) {
            for (String add_url_seed22 : this.collections) {
                create_torrent.add_collection(add_url_seed22);
            }
        }
        return new Result(create_torrent);
    }
}
