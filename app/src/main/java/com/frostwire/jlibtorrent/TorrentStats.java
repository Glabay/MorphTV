package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.TorrentStatus.State;
import com.frostwire.jlibtorrent.swig.torrent_status;

public final class TorrentStats {
    private long allTimeDownload;
    private long allTimeUpload;
    private int downloadPayloadRate;
    private int downloadRate;
    private final IntSeries downloadRateSeries;
    private final Sha1Hash ih;
    private boolean isFinished;
    private boolean isPaused;
    private boolean isSeeding;
    private boolean isSequentialDownload;
    private int listPeers;
    private int listSeeds;
    private final int maxSamples;
    private boolean needSaveResume;
    private int numConnections;
    private int numPeers;
    private int numPieces;
    private int numSeeds;
    private float progress;
    private int progressPpm;
    private State state;
    private final IntSeries time;
    private long totalDone;
    private long totalDownload;
    private long totalPayloadDownload;
    private long totalPayloadUpload;
    private long totalUpload;
    private long totalWanted;
    private long totalWantedDone;
    private int uploadPayloadRate;
    private int uploadRate;
    private final IntSeries uploadRateSeries;

    public enum SeriesMetric {
        TIME,
        DOWNLOAD_RATE,
        UPLOAD_RATE
    }

    public TorrentStats(Sha1Hash sha1Hash, int i) {
        this.ih = sha1Hash.clone();
        this.maxSamples = i;
        this.time = new IntSeries(i);
        this.downloadRateSeries = new IntSeries(i);
        this.uploadRateSeries = new IntSeries(i);
    }

    public int maxSamples() {
        return this.maxSamples;
    }

    public IntSeries series(SeriesMetric seriesMetric) {
        switch (seriesMetric) {
            case TIME:
                return this.time;
            case DOWNLOAD_RATE:
                return this.downloadRateSeries;
            case UPLOAD_RATE:
                return this.uploadRateSeries;
            default:
                throw new UnsupportedOperationException("metric type not supported");
        }
    }

    public long last(SeriesMetric seriesMetric) {
        return series(seriesMetric).last();
    }

    public long totalDownload() {
        return this.totalDownload;
    }

    public long totalUpload() {
        return this.totalUpload;
    }

    public long totalPayloadDownload() {
        return this.totalPayloadDownload;
    }

    public long totalPayloadUpload() {
        return this.totalPayloadUpload;
    }

    public long totalDone() {
        return this.totalDone;
    }

    public long totalWantedDone() {
        return this.totalWantedDone;
    }

    public long totalWanted() {
        return this.totalWanted;
    }

    public long allTimeUpload() {
        return this.allTimeUpload;
    }

    public long allTimeDownload() {
        return this.allTimeDownload;
    }

    public float progress() {
        return this.progress;
    }

    public int progressPpm() {
        return this.progressPpm;
    }

    public int downloadRate() {
        return this.downloadRate;
    }

    public int uploadRate() {
        return this.uploadRate;
    }

    public int downloadPayloadRate() {
        return this.downloadPayloadRate;
    }

    public int uploadPayloadRate() {
        return this.uploadPayloadRate;
    }

    public int numSeeds() {
        return this.numSeeds;
    }

    public int numPeers() {
        return this.numPeers;
    }

    public int listSeeds() {
        return this.listSeeds;
    }

    public int listPeers() {
        return this.listPeers;
    }

    public int numPieces() {
        return this.numPieces;
    }

    public int numConnections() {
        return this.numConnections;
    }

    public State state() {
        return this.state;
    }

    public boolean needSaveResume() {
        return this.needSaveResume;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isSequentialDownload() {
        return this.isSequentialDownload;
    }

    public boolean isSeeding() {
        return this.isSeeding;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void update(TorrentStatus torrentStatus) {
        if (this.ih.equals(torrentStatus.infoHash())) {
            this.time.add(System.currentTimeMillis());
            torrent_status swig = torrentStatus.swig();
            this.downloadRateSeries.add((long) swig.getDownload_rate());
            this.uploadRateSeries.add((long) swig.getUpload_rate());
            this.totalDownload = swig.getTotal_download();
            this.totalUpload = swig.getTotal_upload();
            this.totalPayloadDownload = swig.getTotal_payload_download();
            this.totalPayloadUpload = swig.getTotal_payload_upload();
            this.totalDone = swig.getTotal_done();
            this.totalWantedDone = swig.getTotal_wanted_done();
            this.totalWanted = swig.getTotal_wanted();
            this.allTimeUpload = swig.getAll_time_upload();
            this.allTimeDownload = swig.getAll_time_download();
            this.progress = swig.getProgress();
            this.progressPpm = swig.getProgress_ppm();
            this.downloadRate = swig.getDownload_rate();
            this.uploadRate = swig.getUpload_rate();
            this.downloadPayloadRate = swig.getDownload_payload_rate();
            this.uploadPayloadRate = swig.getUpload_payload_rate();
            this.numSeeds = swig.getNum_seeds();
            this.numPeers = swig.getNum_peers();
            this.listSeeds = swig.getList_seeds();
            this.listPeers = swig.getList_peers();
            this.numPieces = swig.getNum_pieces();
            this.numConnections = swig.getNum_connections();
            this.state = torrentStatus.state();
            this.needSaveResume = swig.getNeed_save_resume();
            this.isPaused = swig.getFlags().and_(TorrentFlags.PAUSED).nonZero();
            this.isSequentialDownload = swig.getFlags().and_(TorrentFlags.SEQUENTIAL_DOWNLOAD).nonZero();
            this.isSeeding = swig.getIs_seeding();
            this.isFinished = swig.getIs_finished();
        }
    }
}
