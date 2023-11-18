package com.frostwire.jlibtorrent.swig;

public class create_torrent {
    public static final create_flags_t merkle = new create_flags_t(libtorrent_jni.create_torrent_merkle_get(), false);
    public static final create_flags_t modification_time = new create_flags_t(libtorrent_jni.create_torrent_modification_time_get(), false);
    public static final create_flags_t mutable_torrent_support = new create_flags_t(libtorrent_jni.create_torrent_mutable_torrent_support_get(), false);
    public static final create_flags_t optimize_alignment = new create_flags_t(libtorrent_jni.create_torrent_optimize_alignment_get(), false);
    public static final create_flags_t symlinks = new create_flags_t(libtorrent_jni.create_torrent_symlinks_get(), false);
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected create_torrent(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(create_torrent create_torrent) {
        return create_torrent == null ? 0 : create_torrent.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_create_torrent(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public create_torrent(file_storage file_storage, int i, int i2, create_flags_t create_flags_t, int i3) {
        this(libtorrent_jni.new_create_torrent__SWIG_0(file_storage.getCPtr(file_storage), file_storage, i, i2, create_flags_t.getCPtr(create_flags_t), create_flags_t, i3), (boolean) 1);
    }

    public create_torrent(file_storage file_storage, int i, int i2, create_flags_t create_flags_t) {
        this(libtorrent_jni.new_create_torrent__SWIG_1(file_storage.getCPtr(file_storage), file_storage, i, i2, create_flags_t.getCPtr(create_flags_t), create_flags_t), (boolean) 1);
    }

    public create_torrent(file_storage file_storage, int i, int i2) {
        this(libtorrent_jni.new_create_torrent__SWIG_2(file_storage.getCPtr(file_storage), file_storage, i, i2), (boolean) 1);
    }

    public create_torrent(file_storage file_storage, int i) {
        this(libtorrent_jni.new_create_torrent__SWIG_3(file_storage.getCPtr(file_storage), file_storage, i), true);
    }

    public create_torrent(file_storage file_storage) {
        this(libtorrent_jni.new_create_torrent__SWIG_4(file_storage.getCPtr(file_storage), file_storage), true);
    }

    public create_torrent(torrent_info torrent_info) {
        this(libtorrent_jni.new_create_torrent__SWIG_5(torrent_info.getCPtr(torrent_info), torrent_info), true);
    }

    public entry generate() {
        return new entry(libtorrent_jni.create_torrent_generate(this.swigCPtr, this), true);
    }

    public file_storage files() {
        return new file_storage(libtorrent_jni.create_torrent_files(this.swigCPtr, this), false);
    }

    public void set_comment(String str) {
        libtorrent_jni.create_torrent_set_comment(this.swigCPtr, this, str);
    }

    public void set_creator(String str) {
        libtorrent_jni.create_torrent_set_creator(this.swigCPtr, this, str);
    }

    public void set_hash(int i, sha1_hash sha1_hash) {
        libtorrent_jni.create_torrent_set_hash(this.swigCPtr, this, i, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void set_file_hash(int i, sha1_hash sha1_hash) {
        libtorrent_jni.create_torrent_set_file_hash(this.swigCPtr, this, i, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void add_node(string_int_pair string_int_pair) {
        libtorrent_jni.create_torrent_add_node(this.swigCPtr, this, string_int_pair.getCPtr(string_int_pair), string_int_pair);
    }

    public void set_priv(boolean z) {
        libtorrent_jni.create_torrent_set_priv(this.swigCPtr, this, z);
    }

    public boolean priv() {
        return libtorrent_jni.create_torrent_priv(this.swigCPtr, this);
    }

    public int num_pieces() {
        return libtorrent_jni.create_torrent_num_pieces(this.swigCPtr, this);
    }

    public int piece_length() {
        return libtorrent_jni.create_torrent_piece_length(this.swigCPtr, this);
    }

    public int piece_size(int i) {
        return libtorrent_jni.create_torrent_piece_size(this.swigCPtr, this, i);
    }

    public sha1_hash_vector merkle_tree() {
        return new sha1_hash_vector(libtorrent_jni.create_torrent_merkle_tree(this.swigCPtr, this), false);
    }

    public void add_similar_torrent(sha1_hash sha1_hash) {
        libtorrent_jni.create_torrent_add_similar_torrent(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void add_url_seed(String str) {
        libtorrent_jni.create_torrent_add_url_seed(this.swigCPtr, this, str);
    }

    public void add_http_seed(String str) {
        libtorrent_jni.create_torrent_add_http_seed(this.swigCPtr, this, str);
    }

    public void add_tracker(String str, int i) {
        libtorrent_jni.create_torrent_add_tracker(this.swigCPtr, this, str, i);
    }

    public void add_collection(String str) {
        libtorrent_jni.create_torrent_add_collection(this.swigCPtr, this, str);
    }
}
