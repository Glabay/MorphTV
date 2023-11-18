package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_entry_vector;
import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.libtorrent_jni;
import com.frostwire.jlibtorrent.swig.sha1_hash_vector;
import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.string_int_pair_vector;
import com.frostwire.jlibtorrent.swig.string_string_pair_vector;
import com.frostwire.jlibtorrent.swig.string_vector;
import com.frostwire.jlibtorrent.swig.torrent_info;
import com.frostwire.jlibtorrent.swig.web_seed_entry_vector;
import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class TorrentInfo {
    private final torrent_info ti;

    public TorrentInfo(torrent_info torrent_info) {
        this.ti = torrent_info;
    }

    public TorrentInfo(File file) {
        this(bdecode0(file));
    }

    public TorrentInfo(MappedByteBuffer mappedByteBuffer) {
        StringBuilder stringBuilder;
        try {
            long directBufferAddress = libtorrent_jni.directBufferAddress(mappedByteBuffer);
            long directBufferCapacity = libtorrent_jni.directBufferCapacity(mappedByteBuffer);
            mappedByteBuffer = new error_code();
            this.ti = new torrent_info(directBufferAddress, (int) directBufferCapacity, mappedByteBuffer);
            if (mappedByteBuffer.value() != 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Can't decode data: ");
                stringBuilder.append(mappedByteBuffer.message());
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (MappedByteBuffer mappedByteBuffer2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can't decode data mapped buffer: ");
            stringBuilder.append(mappedByteBuffer2.getMessage());
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString(), mappedByteBuffer2);
        }
    }

    public torrent_info swig() {
        return this.ti;
    }

    public FileStorage files() {
        return new FileStorage(this.ti.files(), this.ti);
    }

    public FileStorage origFiles() {
        return new FileStorage(this.ti.orig_files(), this.ti);
    }

    public void renameFile(int i, String str) {
        this.ti.rename_file(i, str);
    }

    public void remapFiles(FileStorage fileStorage) {
        this.ti.remap_files(fileStorage.swig());
    }

    public void addTracker(String str) {
        this.ti.add_tracker(str);
    }

    public void addTracker(String str, int i) {
        this.ti.add_tracker(str, i);
    }

    public ArrayList<AnnounceEntry> trackers() {
        return trackers(this.ti.trackers());
    }

    public ArrayList<Sha1Hash> similarTorrents() {
        return Sha1Hash.convert(this.ti.similar_torrents());
    }

    public ArrayList<String> collections() {
        string_vector collections = this.ti.collections();
        int size = (int) collections.size();
        ArrayList<String> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(collections.get(i));
        }
        return arrayList;
    }

    public void clearTrackers() {
        this.ti.trackers().clear();
    }

    public void addUrlSeed(String str) {
        this.ti.add_url_seed(str);
    }

    public void addUrlSeed(String str, String str2) {
        this.ti.add_url_seed(str, str2);
    }

    public void addUrlSeed(String str, String str2, List<Pair<String, String>> list) {
        string_string_pair_vector string_string_pair_vector = new string_string_pair_vector();
        for (Pair to_string_string_pair : list) {
            string_string_pair_vector.push_back(to_string_string_pair.to_string_string_pair());
        }
        this.ti.add_url_seed(str, str2, string_string_pair_vector);
    }

    public void addHttpSeed(String str) {
        this.ti.add_url_seed(str);
    }

    public void addHttpSeed(String str, String str2) {
        this.ti.add_url_seed(str, str2);
    }

    public void addHttpSeed(String str, String str2, List<Pair<String, String>> list) {
        string_string_pair_vector string_string_pair_vector = new string_string_pair_vector();
        for (Pair to_string_string_pair : list) {
            string_string_pair_vector.push_back(to_string_string_pair.to_string_string_pair());
        }
        this.ti.add_url_seed(str, str2, string_string_pair_vector);
    }

    public ArrayList<WebSeedEntry> webSeeds() {
        web_seed_entry_vector web_seeds = this.ti.web_seeds();
        int size = (int) web_seeds.size();
        ArrayList<WebSeedEntry> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new WebSeedEntry(web_seeds.get(i)));
        }
        return arrayList;
    }

    public void setWebSeeds(List<WebSeedEntry> list) {
        web_seed_entry_vector web_seed_entry_vector = new web_seed_entry_vector();
        for (WebSeedEntry swig : list) {
            web_seed_entry_vector.push_back(swig.swig());
        }
        this.ti.set_web_seeds(web_seed_entry_vector);
    }

    public long totalSize() {
        return this.ti.total_size();
    }

    public int pieceLength() {
        return this.ti.piece_length();
    }

    public int numPieces() {
        return this.ti.num_pieces();
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(this.ti.info_hash());
    }

    public int numFiles() {
        return this.ti.num_files();
    }

    public ArrayList<FileSlice> mapBlock(int i, long j, int i2) {
        return FileStorage.mapBlock(this.ti.map_block(i, j, i2));
    }

    public PeerRequest mapFile(int i, long j, int i2) {
        return new PeerRequest(this.ti.map_file(i, j, i2));
    }

    public boolean isValid() {
        return this.ti.is_valid();
    }

    public boolean isPrivate() {
        return this.ti.priv();
    }

    public boolean isI2p() {
        return this.ti.is_i2p();
    }

    public int pieceSize(int i) {
        return this.ti.piece_size(i);
    }

    public Sha1Hash hashForPiece(int i) {
        return new Sha1Hash(this.ti.hash_for_piece(i));
    }

    public boolean isLoaded() {
        return this.ti.is_loaded();
    }

    public ArrayList<Sha1Hash> merkleTree() {
        return Sha1Hash.convert(this.ti.merkle_tree());
    }

    public void merkleTree(List<Sha1Hash> list) {
        sha1_hash_vector sha1_hash_vector = new sha1_hash_vector();
        for (Sha1Hash swig : list) {
            sha1_hash_vector.push_back(swig.swig());
        }
        this.ti.set_merkle_tree(sha1_hash_vector);
    }

    public String name() {
        return this.ti.name();
    }

    public long creationDate() {
        return this.ti.creation_date();
    }

    public String creator() {
        return this.ti.creator();
    }

    public String comment() {
        return this.ti.comment();
    }

    public ArrayList<Pair<String, Integer>> nodes() {
        string_int_pair_vector nodes = this.ti.nodes();
        int size = (int) nodes.size();
        ArrayList<Pair<String, Integer>> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            string_int_pair string_int_pair = nodes.get(i);
            arrayList.add(new Pair(string_int_pair.getFirst(), Integer.valueOf(string_int_pair.getSecond())));
        }
        return arrayList;
    }

    public void addNode(String str, int i) {
        this.ti.add_node(new string_int_pair(str, i));
    }

    public bdecode_node info(String str) {
        return this.ti.info(str);
    }

    public boolean isMerkleTorrent() {
        return this.ti.is_merkle_torrent();
    }

    public String makeMagnetUri() {
        return this.ti.is_valid() ? libtorrent.make_magnet_uri(this.ti) : null;
    }

    public Entry toEntry() {
        return new Entry(new create_torrent(this.ti).generate());
    }

    public byte[] bencode() {
        return toEntry().bencode();
    }

    public static TorrentInfo bdecode(byte[] bArr) {
        return new TorrentInfo(bdecode0(bArr));
    }

    static ArrayList<AnnounceEntry> trackers(announce_entry_vector announce_entry_vector) {
        int size = (int) announce_entry_vector.size();
        ArrayList<AnnounceEntry> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new AnnounceEntry(announce_entry_vector.get(i)));
        }
        return arrayList;
    }

    private static torrent_info bdecode0(File file) {
        try {
            return bdecode0(Files.bytes(file));
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't decode data from file: ");
            stringBuilder.append(file);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        }
    }

    private static torrent_info bdecode0(byte[] bArr) {
        bArr = Vectors.bytes2byte_vector(bArr);
        bdecode_node bdecode_node = new bdecode_node();
        error_code error_code = new error_code();
        if (bdecode_node.bdecode(bArr, bdecode_node, error_code) == 0) {
            error_code.clear();
            torrent_info torrent_info = new torrent_info(bdecode_node, error_code);
            bArr.clear();
            if (error_code.value() == null) {
                return torrent_info;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't decode data: ");
            stringBuilder.append(error_code.message());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Can't decode data: ");
        stringBuilder.append(error_code.message());
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
