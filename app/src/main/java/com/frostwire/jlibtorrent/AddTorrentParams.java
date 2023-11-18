package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.storage_mode_t;
import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.string_int_pair_vector;
import com.frostwire.jlibtorrent.swig.string_vector;
import com.frostwire.jlibtorrent.swig.tcp_endpoint_vector;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_info;
import java.util.ArrayList;
import java.util.List;

public final class AddTorrentParams {
    /* renamed from: p */
    private final add_torrent_params f14p;

    public AddTorrentParams(add_torrent_params add_torrent_params) {
        this.f14p = add_torrent_params;
    }

    public AddTorrentParams() {
        this(add_torrent_params.create_instance());
    }

    public add_torrent_params swig() {
        return this.f14p;
    }

    public int version() {
        return this.f14p.getVersion();
    }

    public TorrentInfo torrentInfo() {
        torrent_info ti_ptr = this.f14p.ti_ptr();
        return (ti_ptr == null || !ti_ptr.is_valid()) ? null : new TorrentInfo(ti_ptr);
    }

    public void torrentInfo(TorrentInfo torrentInfo) {
        this.f14p.set_ti(torrentInfo.swig());
    }

    public ArrayList<String> trackers() {
        string_vector string_vector = this.f14p.get_trackers();
        int size = (int) string_vector.size();
        ArrayList<String> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            arrayList.add(string_vector.get(i));
        }
        return arrayList;
    }

    public void trackers(List<String> list) {
        string_vector string_vector = new string_vector();
        for (String push_back : list) {
            string_vector.push_back(push_back);
        }
        this.f14p.set_trackers(string_vector);
    }

    public ArrayList<Integer> trackerTiers() {
        int_vector int_vector = this.f14p.get_tracker_tiers();
        int size = (int) int_vector.size();
        ArrayList<Integer> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            arrayList.add(Integer.valueOf(int_vector.get(i)));
        }
        return arrayList;
    }

    public void trackerTiers(List<Integer> list) {
        int_vector int_vector = new int_vector();
        for (Integer intValue : list) {
            int_vector.push_back(intValue.intValue());
        }
        this.f14p.set_tracker_tiers(int_vector);
    }

    public ArrayList<Pair<String, Integer>> dhtNodes() {
        string_int_pair_vector string_int_pair_vector = this.f14p.get_dht_nodes();
        int size = (int) string_int_pair_vector.size();
        ArrayList<Pair<String, Integer>> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            string_int_pair string_int_pair = string_int_pair_vector.get(i);
            arrayList.add(new Pair(string_int_pair.getFirst(), Integer.valueOf(string_int_pair.getSecond())));
        }
        return arrayList;
    }

    public void dhtNodes(List<Pair<String, Integer>> list) {
        string_int_pair_vector string_int_pair_vector = new string_int_pair_vector();
        for (Pair to_string_int_pair : list) {
            string_int_pair_vector.push_back(to_string_int_pair.to_string_int_pair());
        }
        this.f14p.set_dht_nodes(string_int_pair_vector);
    }

    public String name() {
        return this.f14p.getName();
    }

    public void name(String str) {
        this.f14p.setName(str);
    }

    public String savePath() {
        return this.f14p.getSave_path();
    }

    public void savePath(String str) {
        this.f14p.setSave_path(str);
    }

    public StorageMode storageMode() {
        return StorageMode.fromSwig(this.f14p.getStorage_mode().swigValue());
    }

    public void storageMode(StorageMode storageMode) {
        this.f14p.setStorage_mode(storage_mode_t.swigToEnum(storageMode.swig()));
    }

    public String trackerId() {
        return this.f14p.getTrackerid();
    }

    public void trackerId(String str) {
        this.f14p.setTrackerid(str);
    }

    public Sha1Hash infoHash() {
        return new Sha1Hash(this.f14p.getInfo_hash());
    }

    public void infoHash(Sha1Hash sha1Hash) {
        this.f14p.setInfo_hash(sha1Hash.swig());
    }

    public int maxUploads() {
        return this.f14p.getMax_uploads();
    }

    public void maxUploads(int i) {
        this.f14p.setMax_uploads(i);
    }

    public int maxConnections() {
        return this.f14p.getMax_connections();
    }

    public void maxConnections(int i) {
        this.f14p.setMax_connections(i);
    }

    public int uploadLimit() {
        return this.f14p.getUpload_limit();
    }

    public void uploadLimit(int i) {
        this.f14p.setUpload_limit(i);
    }

    public int downloadLimit() {
        return this.f14p.getDownload_limit();
    }

    public void downloadLimit(int i) {
        this.f14p.setDownload_limit(i);
    }

    public torrent_flags_t flags() {
        return this.f14p.getFlags();
    }

    public void flags(torrent_flags_t torrent_flags_t) {
        this.f14p.setFlags(torrent_flags_t);
    }

    public ArrayList<String> urlSeeds() {
        string_vector string_vector = this.f14p.get_url_seeds();
        int size = (int) string_vector.size();
        ArrayList<String> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            arrayList.add(string_vector.get(i));
        }
        return arrayList;
    }

    public void urlSeeds(List<String> list) {
        string_vector string_vector = new string_vector();
        for (String push_back : list) {
            string_vector.push_back(push_back);
        }
        this.f14p.set_url_seeds(string_vector);
    }

    public void filePriorities(Priority[] priorityArr) {
        this.f14p.set_file_priorities(Priority.array2byte_vector(priorityArr));
    }

    public void piecePriorities(Priority[] priorityArr) {
        this.f14p.set_piece_priorities(Priority.array2byte_vector(priorityArr));
    }

    public ArrayList<TcpEndpoint> peers() {
        tcp_endpoint_vector tcp_endpoint_vector = this.f14p.get_peers();
        int size = (int) tcp_endpoint_vector.size();
        ArrayList<TcpEndpoint> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            arrayList.add(new TcpEndpoint(tcp_endpoint_vector.get(i)));
        }
        return arrayList;
    }

    public void peers(List<TcpEndpoint> list) {
        tcp_endpoint_vector tcp_endpoint_vector = new tcp_endpoint_vector();
        for (TcpEndpoint swig : list) {
            tcp_endpoint_vector.push_back(swig.swig());
        }
        this.f14p.set_peers(tcp_endpoint_vector);
    }

    public ArrayList<TcpEndpoint> bannedPeers() {
        tcp_endpoint_vector tcp_endpoint_vector = this.f14p.get_banned_peers();
        int size = (int) tcp_endpoint_vector.size();
        ArrayList<TcpEndpoint> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            arrayList.add(new TcpEndpoint(tcp_endpoint_vector.get(i)));
        }
        return arrayList;
    }

    public void bannedPeers(List<TcpEndpoint> list) {
        tcp_endpoint_vector tcp_endpoint_vector = new tcp_endpoint_vector();
        for (TcpEndpoint swig : list) {
            tcp_endpoint_vector.push_back(swig.swig());
        }
        this.f14p.set_banned_peers(tcp_endpoint_vector);
    }

    public static AddTorrentParams createInstance() {
        return new AddTorrentParams(add_torrent_params.create_instance());
    }

    public static AddTorrentParams createInstanceDisabledStorage() {
        return new AddTorrentParams(add_torrent_params.create_instance_disabled_storage());
    }

    public static AddTorrentParams createInstanceZeroStorage() {
        return new AddTorrentParams(add_torrent_params.create_instance_zero_storage());
    }

    public static AddTorrentParams parseMagnetUri(String str, AddTorrentParams addTorrentParams) {
        error_code error_code = new error_code();
        add_torrent_params.parse_magnet_uri(str, addTorrentParams.swig(), error_code);
        if (error_code.value() == null) {
            return addTorrentParams;
        }
        addTorrentParams = new StringBuilder();
        addTorrentParams.append("Invalid magnet uri: ");
        addTorrentParams.append(error_code.message());
        throw new IllegalArgumentException(addTorrentParams.toString());
    }
}
