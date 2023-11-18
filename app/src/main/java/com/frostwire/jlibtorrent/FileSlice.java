package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_slice;

public final class FileSlice {
    private final int fileIndex;
    private final long offset;
    private final long size;

    public FileSlice(file_slice file_slice) {
        this.fileIndex = file_slice.getFile_index();
        this.offset = file_slice.getOffset();
        this.size = file_slice.getSize();
    }

    public int fileIndex() {
        return this.fileIndex;
    }

    public long offset() {
        return this.offset;
    }

    public long size() {
        return this.size;
    }

    public String toString() {
        return String.format("FileSlice(fileIndex: %d, offset: %d, size: %d)", new Object[]{Integer.valueOf(this.fileIndex), Long.valueOf(this.offset), Long.valueOf(this.size)});
    }
}
