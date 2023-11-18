package com.frostwire.jlibtorrent.swig;

public class block_info {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class block_state_t {
        public static final block_state_t finished = new block_state_t("finished");
        public static final block_state_t none = new block_state_t("none");
        public static final block_state_t requested = new block_state_t("requested");
        private static int swigNext;
        private static block_state_t[] swigValues = new block_state_t[]{none, requested, writing, finished};
        public static final block_state_t writing = new block_state_t("writing");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static block_state_t swigToEnum(int i) {
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
            stringBuilder.append(block_state_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private block_state_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private block_state_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private block_state_t(String str, block_state_t block_state_t) {
            this.swigName = str;
            this.swigValue = block_state_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected block_info(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(block_info block_info) {
        return block_info == null ? 0 : block_info.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_block_info(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public tcp_endpoint peer() {
        return new tcp_endpoint(libtorrent_jni.block_info_peer(this.swigCPtr, this), true);
    }

    public void setBytes_progress(long j) {
        libtorrent_jni.block_info_bytes_progress_set(this.swigCPtr, this, j);
    }

    public long getBytes_progress() {
        return libtorrent_jni.block_info_bytes_progress_get(this.swigCPtr, this);
    }

    public void setBlock_size(long j) {
        libtorrent_jni.block_info_block_size_set(this.swigCPtr, this, j);
    }

    public long getBlock_size() {
        return libtorrent_jni.block_info_block_size_get(this.swigCPtr, this);
    }

    public void setState(long j) {
        libtorrent_jni.block_info_state_set(this.swigCPtr, this, j);
    }

    public long getState() {
        return libtorrent_jni.block_info_state_get(this.swigCPtr, this);
    }

    public void setNum_peers(long j) {
        libtorrent_jni.block_info_num_peers_set(this.swigCPtr, this, j);
    }

    public long getNum_peers() {
        return libtorrent_jni.block_info_num_peers_get(this.swigCPtr, this);
    }

    public block_info() {
        this(libtorrent_jni.new_block_info(), true);
    }
}
