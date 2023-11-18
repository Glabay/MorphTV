package com.github.se_bastiaan.torrentstream;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.FileStorage;
import com.frostwire.jlibtorrent.Priority;
import com.frostwire.jlibtorrent.TorrentFlags;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Torrent implements AlertListener {
    private static final Integer DEFAULT_PREPARE_COUNT = Integer.valueOf(5);
    private static final Integer MAX_PREPARE_COUNT = Integer.valueOf(20);
    private static final Integer MIN_PREPARE_COUNT = Integer.valueOf(2);
    private static final Integer SEQUENTIAL_CONCURRENT_PIECES_COUNT = Integer.valueOf(5);
    private Integer firstPieceIndex;
    private Boolean[] hasPieces;
    private Integer interestedPieceIndex = Integer.valueOf(0);
    private Integer lastPieceIndex;
    private final TorrentListener listener;
    private Integer piecesToPrepare;
    private List<Integer> preparePieces;
    private Double prepareProgress = Double.valueOf(0.0d);
    private final Long prepareSize;
    private Double progressStep = Double.valueOf(0.0d);
    private Integer selectedFileIndex = Integer.valueOf(-1);
    private State state = State.RETRIEVING_META;
    private final TorrentHandle torrentHandle;
    private List<WeakReference<TorrentInputStream>> torrentStreamReferences;

    public enum State {
        UNKNOWN,
        RETRIEVING_META,
        STARTING,
        STREAMING
    }

    public Torrent(TorrentHandle torrentHandle, TorrentListener torrentListener, Long l) {
        this.torrentHandle = torrentHandle;
        this.listener = torrentListener;
        this.prepareSize = l;
        this.torrentStreamReferences = new ArrayList();
        if (this.selectedFileIndex.intValue() == -1) {
            setLargestFile();
        }
        if (this.listener != null) {
            this.listener.onStreamPrepared(this);
        }
    }

    private void resetPriorities() {
        Priority[] piecePriorities = this.torrentHandle.getPiecePriorities();
        int i = 0;
        while (i < piecePriorities.length) {
            if (i < this.firstPieceIndex.intValue() || i > this.lastPieceIndex.intValue()) {
                this.torrentHandle.piecePriority(i, Priority.IGNORE);
            } else {
                this.torrentHandle.piecePriority(i, Priority.NORMAL);
            }
            i++;
        }
    }

    public TorrentHandle getTorrentHandle() {
        return this.torrentHandle;
    }

    public File getVideoFile() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.torrentHandle.savePath());
        stringBuilder.append("/");
        stringBuilder.append(this.torrentHandle.torrentFile().files().filePath(this.selectedFileIndex.intValue()));
        return new File(stringBuilder.toString());
    }

    public InputStream getVideoStream() throws FileNotFoundException {
        InputStream torrentInputStream = new TorrentInputStream(this, new FileInputStream(getVideoFile()));
        this.torrentStreamReferences.add(new WeakReference(torrentInputStream));
        return torrentInputStream;
    }

    public File getSaveLocation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.torrentHandle.savePath());
        stringBuilder.append("/");
        stringBuilder.append(this.torrentHandle.name());
        return new File(stringBuilder.toString());
    }

    public void resume() {
        this.torrentHandle.resume();
    }

    public void pause() {
        this.torrentHandle.pause();
    }

    public void setLargestFile() {
        setSelectedFileIndex(Integer.valueOf(-1));
    }

    public void setSelectedFileIndex(Integer num) {
        int i;
        int i2;
        FileStorage files = this.torrentHandle.torrentFile().files();
        if (num.intValue() == -1) {
            long j = 0;
            i = -1;
            for (num = null; num < files.numFiles(); num++) {
                long fileSize = files.fileSize(num);
                if (j < fileSize) {
                    this.torrentHandle.setFilePriority(i, Priority.IGNORE);
                    this.torrentHandle.setFilePriority(num, Priority.NORMAL);
                    i = num;
                    j = fileSize;
                } else {
                    this.torrentHandle.setFilePriority(num, Priority.IGNORE);
                }
            }
            num = Integer.valueOf(i);
        } else {
            for (i = 0; i < files.numFiles(); i++) {
                if (i == num.intValue()) {
                    this.torrentHandle.setFilePriority(i, Priority.NORMAL);
                } else {
                    this.torrentHandle.setFilePriority(i, Priority.IGNORE);
                }
            }
        }
        this.selectedFileIndex = num;
        num = this.torrentHandle.getPiecePriorities();
        int i3 = -1;
        i = -1;
        for (i2 = 0; i2 < num.length; i2++) {
            if (num[i2] != Priority.IGNORE) {
                if (i == -1) {
                    i = i2;
                }
                num[i2] = Priority.IGNORE;
            } else if (i != -1 && i3 == -1) {
                i3 = i2 - 1;
            }
        }
        if (i3 == -1) {
            i3 = num.length - 1;
        }
        num = (i3 - i) + 1;
        i2 = this.torrentHandle.torrentFile().pieceLength();
        if (i2 > 0) {
            i2 = (int) (this.prepareSize.longValue() / ((long) i2));
            if (i2 < MIN_PREPARE_COUNT.intValue()) {
                i2 = MIN_PREPARE_COUNT.intValue();
            } else if (i2 > MAX_PREPARE_COUNT.intValue()) {
                i2 = MAX_PREPARE_COUNT.intValue();
            }
        } else {
            i2 = DEFAULT_PREPARE_COUNT.intValue();
        }
        if (num < i2) {
            i2 = num / 2;
        }
        this.firstPieceIndex = Integer.valueOf(i);
        this.interestedPieceIndex = this.firstPieceIndex;
        this.lastPieceIndex = Integer.valueOf(i3);
        this.piecesToPrepare = Integer.valueOf(i2);
    }

    public String[] getFileNames() {
        FileStorage files = this.torrentHandle.torrentFile().files();
        String[] strArr = new String[files.numFiles()];
        for (int i = 0; i < files.numFiles(); i++) {
            strArr[i] = files.fileName(i);
        }
        return strArr;
    }

    public void startDownload() {
        if (this.state != State.STREAMING) {
            int i;
            this.state = State.STARTING;
            List arrayList = new ArrayList();
            Priority[] piecePriorities = this.torrentHandle.getPiecePriorities();
            for (int i2 = 0; i2 < piecePriorities.length; i2++) {
                if (piecePriorities[i2] != Priority.IGNORE) {
                    this.torrentHandle.piecePriority(i2, Priority.NORMAL);
                }
            }
            for (i = 0; i < this.piecesToPrepare.intValue(); i++) {
                arrayList.add(Integer.valueOf(this.lastPieceIndex.intValue() - i));
                this.torrentHandle.piecePriority(this.lastPieceIndex.intValue() - i, Priority.SEVEN);
                this.torrentHandle.setPieceDeadline(this.lastPieceIndex.intValue() - i, 1000);
            }
            for (i = 0; i < this.piecesToPrepare.intValue(); i++) {
                arrayList.add(Integer.valueOf(this.firstPieceIndex.intValue() + i));
                this.torrentHandle.piecePriority(this.firstPieceIndex.intValue() + i, Priority.SEVEN);
                this.torrentHandle.setPieceDeadline(this.firstPieceIndex.intValue() + i, 1000);
            }
            this.preparePieces = arrayList;
            this.hasPieces = new Boolean[((this.lastPieceIndex.intValue() - this.firstPieceIndex.intValue()) + 1)];
            Arrays.fill(this.hasPieces, Boolean.valueOf(false));
            this.progressStep = Double.valueOf(100.0d / ((double) ((arrayList.size() * this.torrentHandle.torrentFile().pieceLength()) / this.torrentHandle.status().blockSize())));
            this.torrentStreamReferences.clear();
            this.torrentHandle.resume();
            this.listener.onStreamStarted(this);
        }
    }

    public boolean hasBytes(long j) {
        if (this.hasPieces == null) {
            return 0;
        }
        return this.hasPieces[(int) (j / ((long) this.torrentHandle.torrentFile().pieceLength()))].booleanValue();
    }

    public void setInterestedBytes(long j) {
        if (this.hasPieces != null || j < 0) {
            j = (int) (j / ((long) this.torrentHandle.torrentFile().pieceLength()));
            this.interestedPieceIndex = Integer.valueOf(j);
            if (!this.hasPieces[j].booleanValue() && this.torrentHandle.piecePriority(this.firstPieceIndex.intValue() + j) != Priority.SEVEN) {
                this.interestedPieceIndex = Integer.valueOf(j);
                int i = 5;
                while (j < this.hasPieces.length) {
                    if (!this.hasPieces[j].booleanValue()) {
                        this.torrentHandle.piecePriority(this.firstPieceIndex.intValue() + j, Priority.SEVEN);
                        this.torrentHandle.setPieceDeadline(this.firstPieceIndex.intValue() + j, 1000);
                        i--;
                        if (i == 0) {
                            break;
                        }
                    }
                    j++;
                }
            }
        }
    }

    public boolean hasInterestedBytes(int i) {
        for (int i2 = 0; i2 < i + 5; i2++) {
            int intValue = this.interestedPieceIndex.intValue() + i2;
            if (this.hasPieces.length > intValue) {
                if (intValue >= 0) {
                    if (!this.hasPieces[this.interestedPieceIndex.intValue() + i2].booleanValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean hasInterestedBytes() {
        return hasInterestedBytes(5);
    }

    public int getInterestedPieceIndex() {
        return this.interestedPieceIndex.intValue();
    }

    public Integer getPiecesToPrepare() {
        return this.piecesToPrepare;
    }

    private void startSequentialMode() {
        resetPriorities();
        if (this.hasPieces == null) {
            this.torrentHandle.setFlags(this.torrentHandle.flags().and_(TorrentFlags.SEQUENTIAL_DOWNLOAD));
            return;
        }
        for (int intValue = this.firstPieceIndex.intValue() + this.piecesToPrepare.intValue(); intValue < (this.firstPieceIndex.intValue() + this.piecesToPrepare.intValue()) + SEQUENTIAL_CONCURRENT_PIECES_COUNT.intValue(); intValue++) {
            this.torrentHandle.piecePriority(intValue, Priority.SEVEN);
            this.torrentHandle.setPieceDeadline(intValue, 1000);
        }
    }

    public State getState() {
        return this.state;
    }

    private void pieceFinished(PieceFinishedAlert pieceFinishedAlert) {
        if (this.state != State.STREAMING || this.hasPieces == null) {
            Iterator it = this.preparePieces.iterator();
            while (it.hasNext()) {
                if (((Integer) it.next()).intValue() == pieceFinishedAlert.pieceIndex()) {
                    it.remove();
                }
            }
            if (this.hasPieces != null) {
                this.hasPieces[pieceFinishedAlert.pieceIndex() - this.firstPieceIndex.intValue()] = Boolean.valueOf(true);
            }
            if (this.preparePieces.size() == null) {
                startSequentialMode();
                this.prepareProgress = Double.valueOf(100.0d);
                sendStreamProgress();
                this.state = State.STREAMING;
                if (this.listener != null) {
                    this.listener.onStreamReady(this);
                    return;
                }
                return;
            }
            return;
        }
        pieceFinishedAlert = pieceFinishedAlert.pieceIndex() - this.firstPieceIndex.intValue();
        this.hasPieces[pieceFinishedAlert] = Boolean.valueOf(true);
        if (pieceFinishedAlert >= this.interestedPieceIndex.intValue()) {
            while (pieceFinishedAlert < this.hasPieces.length) {
                if (this.hasPieces[pieceFinishedAlert].booleanValue()) {
                    pieceFinishedAlert++;
                } else {
                    this.torrentHandle.piecePriority(this.firstPieceIndex.intValue() + pieceFinishedAlert, Priority.SEVEN);
                    this.torrentHandle.setPieceDeadline(pieceFinishedAlert + this.firstPieceIndex.intValue(), 1000);
                    return;
                }
            }
        }
    }

    private void blockFinished(BlockFinishedAlert blockFinishedAlert) {
        for (Integer intValue : this.preparePieces) {
            if (intValue.intValue() == blockFinishedAlert.pieceIndex()) {
                this.prepareProgress = Double.valueOf(this.prepareProgress.doubleValue() + this.progressStep.doubleValue());
                break;
            }
        }
        sendStreamProgress();
    }

    private void sendStreamProgress() {
        TorrentStatus status = this.torrentHandle.status();
        float progress = status.progress() * 100.0f;
        int numSeeds = status.numSeeds();
        int downloadPayloadRate = status.downloadPayloadRate();
        if (this.listener != null && this.prepareProgress.doubleValue() >= 1.0d) {
            this.listener.onStreamProgress(this, new StreamStatus(progress, this.prepareProgress.intValue(), numSeeds, downloadPayloadRate));
        }
    }

    public int[] types() {
        return new int[]{AlertType.PIECE_FINISHED.swig(), AlertType.BLOCK_FINISHED.swig()};
    }

    public void alert(Alert<?> alert) {
        switch (alert.type()) {
            case PIECE_FINISHED:
                pieceFinished((PieceFinishedAlert) alert);
                break;
            case BLOCK_FINISHED:
                blockFinished((BlockFinishedAlert) alert);
                break;
            default:
                break;
        }
        Iterator it = this.torrentStreamReferences.iterator();
        while (it.hasNext()) {
            TorrentInputStream torrentInputStream = (TorrentInputStream) ((WeakReference) it.next()).get();
            if (torrentInputStream == null) {
                it.remove();
            } else {
                torrentInputStream.alert(alert);
            }
        }
    }
}
