package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

public enum StorageMode {
    STORAGE_MODE_ALLOCATE(storage_mode_t.storage_mode_allocate.swigValue()),
    STORAGE_MODE_SPARSE(storage_mode_t.storage_mode_sparse.swigValue()),
    UNKNOWN(-1);
    
    private final int swigValue;

    private StorageMode(int i) {
        this.swigValue = i;
    }

    public int swig() {
        return this.swigValue;
    }

    public static StorageMode fromSwig(int i) {
        for (StorageMode storageMode : (StorageMode[]) StorageMode.class.getEnumConstants()) {
            if (storageMode.swig() == i) {
                return storageMode;
            }
        }
        return UNKNOWN;
    }
}
