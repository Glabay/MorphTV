package com.frostwire.jlibtorrent;

import java.util.Iterator;

public final class PiecesTracker {
    private final boolean[] complete = new boolean[this.numPieces];
    private final int[][] files = new int[this.numFiles][];
    private final int numFiles;
    private final int numPieces;
    private final long[][] sizes = new long[this.numFiles][];

    public PiecesTracker(TorrentInfo torrentInfo) {
        this.numFiles = torrentInfo.numFiles();
        this.numPieces = torrentInfo.numPieces();
        FileStorage files = torrentInfo.files();
        for (int i = 0; i < this.numFiles; i++) {
            long fileSize = files.fileSize(i);
            int piece = torrentInfo.mapFile(i, 0, 1).piece();
            int piece2 = torrentInfo.mapFile(i, fileSize - 1, 1).piece();
            int i2 = (piece2 - piece) + 1;
            this.files[i] = new int[i2];
            this.sizes[i] = new long[i2];
            for (i2 = piece; i2 <= piece2; i2++) {
                int i3 = i2 - piece;
                this.files[i][i3] = i2;
                Iterator it = torrentInfo.mapBlock(i2, 0, torrentInfo.pieceSize(i2)).iterator();
                while (it.hasNext()) {
                    FileSlice fileSlice = (FileSlice) it.next();
                    if (fileSlice.fileIndex() == i) {
                        this.sizes[i][i3] = fileSlice.size();
                    }
                }
            }
        }
    }

    public int numFiles() {
        return this.numFiles;
    }

    public int numPieces() {
        return this.numPieces;
    }

    public boolean isComplete(int i) {
        return this.complete[i];
    }

    public void setComplete(int i, boolean z) {
        this.complete[i] = z;
    }

    public long getSequentialDownloadedBytes(int i) {
        int[] iArr = this.files[i];
        long j = 0;
        int i2 = 0;
        while (i2 < iArr.length) {
            if (!this.complete[iArr[i2]]) {
                break;
            }
            i2++;
            j += this.sizes[i][i2];
        }
        return j;
    }

    public int getSequentialDownloadedPieces(int i) {
        i = this.files[i];
        int i2 = 0;
        for (int i3 : i) {
            if (!this.complete[i3]) {
                break;
            }
            i2++;
        }
        return i2;
    }
}
