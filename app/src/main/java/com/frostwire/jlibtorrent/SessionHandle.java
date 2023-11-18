package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.remove_flags_t;
import com.frostwire.jlibtorrent.swig.save_state_flags_t;
import com.frostwire.jlibtorrent.swig.session_handle;
import com.frostwire.jlibtorrent.swig.session_handle.protocol_type;
import com.frostwire.jlibtorrent.swig.status_flags_t;
import com.frostwire.jlibtorrent.swig.swig_plugin;
import com.frostwire.jlibtorrent.swig.torrent_handle_vector;
import java.util.ArrayList;
import java.util.List;

public class SessionHandle {
    public static final remove_flags_t DELETE_FILES = session_handle.delete_files;
    public static final remove_flags_t DELETE_PARTFILE = session_handle.delete_partfile;
    private static final Logger LOG = Logger.getLogger(SessionHandle.class);
    public static final save_state_flags_t SAVE_DHT_SETTINGS = session_handle.save_dht_settings;
    public static final save_state_flags_t SAVE_DHT_STATE = session_handle.save_dht_state;
    public static final save_state_flags_t SAVE_ENCRYPTION_SETTINGS = session_handle.save_encryption_settings;
    public static final save_state_flags_t SAVE_SETTINGS = session_handle.save_settings;
    /* renamed from: s */
    protected final session_handle f30s;

    public enum ProtocolType {
        UDP(protocol_type.udp.swigValue()),
        TCP(protocol_type.tcp.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private ProtocolType(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static ProtocolType fromSwig(int i) {
            for (ProtocolType protocolType : (ProtocolType[]) ProtocolType.class.getEnumConstants()) {
                if (protocolType.swig() == i) {
                    return protocolType;
                }
            }
            return UNKNOWN;
        }
    }

    public SessionHandle(session_handle session_handle) {
        this.f30s = session_handle;
    }

    public session_handle swig() {
        return this.f30s;
    }

    public boolean isValid() {
        return this.f30s.is_valid();
    }

    public byte[] saveState(save_state_flags_t save_state_flags_t) {
        entry entry = new entry();
        this.f30s.save_state(entry, save_state_flags_t);
        return Vectors.byte_vector2bytes(entry.bencode());
    }

    public byte[] saveState() {
        entry entry = new entry();
        this.f30s.save_state(entry);
        return Vectors.byte_vector2bytes(entry.bencode());
    }

    public void loadState(byte[] bArr, save_state_flags_t save_state_flags_t) {
        bArr = Vectors.bytes2byte_vector(bArr);
        bdecode_node bdecode_node = new bdecode_node();
        error_code error_code = new error_code();
        if (bdecode_node.bdecode(bArr, bdecode_node, error_code) == 0) {
            this.f30s.load_state(bdecode_node, save_state_flags_t);
            bArr.clear();
            return;
        }
        bArr = LOG;
        save_state_flags_t = new StringBuilder();
        save_state_flags_t.append("failed to decode bencoded data: ");
        save_state_flags_t.append(error_code.message());
        bArr.error(save_state_flags_t.toString());
    }

    public void loadState(byte[] bArr) {
        bArr = Vectors.bytes2byte_vector(bArr);
        bdecode_node bdecode_node = new bdecode_node();
        error_code error_code = new error_code();
        if (bdecode_node.bdecode(bArr, bdecode_node, error_code) == 0) {
            this.f30s.load_state(bdecode_node);
            bArr.clear();
            return;
        }
        bArr = LOG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("failed to decode bencoded data: ");
        stringBuilder.append(error_code.message());
        bArr.error(stringBuilder.toString());
    }

    public void postTorrentUpdates(status_flags_t status_flags_t) {
        this.f30s.post_torrent_updates(status_flags_t);
    }

    public void postTorrentUpdates() {
        this.f30s.post_torrent_updates();
    }

    public void postSessionStats() {
        this.f30s.post_session_stats();
    }

    public void postDHTStats() {
        this.f30s.post_dht_stats();
    }

    public TorrentHandle findTorrent(Sha1Hash sha1Hash) {
        sha1Hash = this.f30s.find_torrent(sha1Hash.swig());
        return (sha1Hash == null || !sha1Hash.is_valid()) ? null : new TorrentHandle(sha1Hash);
    }

    public List<TorrentHandle> torrents() {
        torrent_handle_vector torrent_handle_vector = this.f30s.get_torrents();
        int size = (int) torrent_handle_vector.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new TorrentHandle(torrent_handle_vector.get(i)));
        }
        return arrayList;
    }

    public TorrentHandle addTorrent(AddTorrentParams addTorrentParams, ErrorCode errorCode) {
        return new TorrentHandle(this.f30s.add_torrent(addTorrentParams.swig(), errorCode.swig()));
    }

    public void asyncAddTorrent(AddTorrentParams addTorrentParams) {
        this.f30s.async_add_torrent(addTorrentParams.swig());
    }

    public void removeTorrent(TorrentHandle torrentHandle, remove_flags_t remove_flags_t) {
        if (torrentHandle.isValid()) {
            this.f30s.remove_torrent(torrentHandle.swig(), remove_flags_t);
        }
    }

    public void removeTorrent(TorrentHandle torrentHandle) {
        if (torrentHandle.isValid()) {
            this.f30s.remove_torrent(torrentHandle.swig());
        }
    }

    public void pause() {
        this.f30s.pause();
    }

    public void resume() {
        this.f30s.resume();
    }

    public boolean isPaused() {
        return this.f30s.is_paused();
    }

    void setDHTSettings(DhtSettings dhtSettings) {
        this.f30s.set_dht_settings(dhtSettings.swig());
    }

    public boolean isDHTRunning() {
        return this.f30s.is_dht_running();
    }

    public void addDHTNode(Pair<String, Integer> pair) {
        this.f30s.add_dht_node(pair.to_string_int_pair());
    }

    public void applySettings(SettingsPack settingsPack) {
        this.f30s.apply_settings(settingsPack.swig());
    }

    public SettingsPack settings() {
        return new SettingsPack(this.f30s.get_settings());
    }

    public int addPortMapping(ProtocolType protocolType, int i, int i2) {
        return this.f30s.add_port_mapping(protocol_type.swigToEnum(protocolType.swig()), i, i2);
    }

    public void deletePortMapping(int i) {
        this.f30s.delete_port_mapping(i);
    }

    public void dhtGetItem(Sha1Hash sha1Hash) {
        this.f30s.dht_get_item(sha1Hash.swig());
    }

    public void dhtGetItem(byte[] bArr, byte[] bArr2) {
        this.f30s.dht_get_item(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2));
    }

    public Sha1Hash dhtPutItem(Entry entry) {
        return new Sha1Hash(this.f30s.dht_put_item(entry.swig()));
    }

    public void dhtPutItem(byte[] bArr, byte[] bArr2, Entry entry, byte[] bArr3) {
        this.f30s.dht_put_item(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2), entry.swig(), Vectors.bytes2byte_vector(bArr3));
    }

    public void dhtGetPeers(Sha1Hash sha1Hash) {
        this.f30s.dht_get_peers(sha1Hash.swig());
    }

    public void dhtAnnounce(Sha1Hash sha1Hash, int i, int i2) {
        this.f30s.dht_announce(sha1Hash.swig(), i, i2);
    }

    public void dhtAnnounce(Sha1Hash sha1Hash) {
        this.f30s.dht_announce(sha1Hash.swig());
    }

    public void dhtDirectRequest(UdpEndpoint udpEndpoint, Entry entry, long j) {
        this.f30s.dht_direct_request(udpEndpoint.swig(), entry.swig(), j);
    }

    public void dhtDirectRequest(UdpEndpoint udpEndpoint, Entry entry) {
        this.f30s.dht_direct_request(udpEndpoint.swig(), entry.swig());
    }

    public int getListenPort() {
        return this.f30s.listen_port();
    }

    public int getSslListenPort() {
        return this.f30s.ssl_listen_port();
    }

    public boolean isListening() {
        return this.f30s.is_listening();
    }

    public void addExtension(Plugin plugin) {
        swig_plugin swigPlugin = new SwigPlugin(plugin);
        this.f30s.add_extension(swigPlugin);
        swigPlugin.swigReleaseOwnership();
    }
}
