package com.github.se_bastiaan.torrentstream;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import java.io.File;

public final class TorrentOptions {
    protected Boolean anonymousMode;
    protected Boolean autoDownload;
    protected Integer listeningPort;
    protected Integer maxConnections;
    protected Integer maxDht;
    protected Integer maxDownloadSpeed;
    protected Integer maxUploadSpeed;
    protected String peerFingerprint;
    protected Long prepareSize;
    protected String proxyHost;
    protected String proxyPassword;
    protected String proxyUsername;
    protected Boolean removeFiles;
    protected String saveLocation;

    public static class Builder {
        private TorrentOptions torrentOptions;

        public Builder() {
            this.torrentOptions = new TorrentOptions();
        }

        private Builder(TorrentOptions torrentOptions) {
            TorrentOptions torrentOptions2 = new TorrentOptions();
        }

        public Builder saveLocation(String str) {
            this.torrentOptions.saveLocation = str;
            return this;
        }

        public Builder saveLocation(File file) {
            this.torrentOptions.saveLocation = file.getAbsolutePath();
            return this;
        }

        public Builder maxUploadSpeed(Integer num) {
            this.torrentOptions.maxUploadSpeed = num;
            return this;
        }

        public Builder maxDownloadSpeed(Integer num) {
            this.torrentOptions.maxDownloadSpeed = num;
            return this;
        }

        public Builder maxConnections(Integer num) {
            this.torrentOptions.maxConnections = num;
            return this;
        }

        public Builder maxActiveDHT(Integer num) {
            this.torrentOptions.maxDht = num;
            return this;
        }

        public Builder removeFilesAfterStop(Boolean bool) {
            this.torrentOptions.removeFiles = bool;
            return this;
        }

        public Builder prepareSize(Long l) {
            this.torrentOptions.prepareSize = l;
            return this;
        }

        public Builder listeningPort(Integer num) {
            this.torrentOptions.listeningPort = num;
            return this;
        }

        public Builder proxy(String str, String str2, String str3) {
            this.torrentOptions.proxyHost = str;
            this.torrentOptions.proxyUsername = str2;
            this.torrentOptions.proxyPassword = str3;
            return this;
        }

        public Builder peerFingerprint(String str) {
            this.torrentOptions.peerFingerprint = str;
            this.torrentOptions.anonymousMode = Boolean.valueOf(false);
            return this;
        }

        public Builder anonymousMode(Boolean bool) {
            this.torrentOptions.anonymousMode = bool;
            if (this.torrentOptions.anonymousMode.booleanValue() != null) {
                this.torrentOptions.peerFingerprint = null;
            }
            return this;
        }

        public Builder autoDownload(Boolean bool) {
            this.torrentOptions.autoDownload = bool;
            return this;
        }

        public TorrentOptions build() {
            return this.torrentOptions;
        }
    }

    private TorrentOptions() {
        this.saveLocation = "/";
        this.maxDownloadSpeed = Integer.valueOf(0);
        this.maxUploadSpeed = Integer.valueOf(0);
        this.maxConnections = Integer.valueOf(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.maxDht = Integer.valueOf(88);
        this.listeningPort = Integer.valueOf(-1);
        this.removeFiles = Boolean.valueOf(false);
        this.anonymousMode = Boolean.valueOf(false);
        this.autoDownload = Boolean.valueOf(true);
        this.prepareSize = Long.valueOf(15728640);
    }

    private TorrentOptions(TorrentOptions torrentOptions) {
        this.saveLocation = "/";
        this.maxDownloadSpeed = Integer.valueOf(0);
        this.maxUploadSpeed = Integer.valueOf(0);
        this.maxConnections = Integer.valueOf(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.maxDht = Integer.valueOf(88);
        this.listeningPort = Integer.valueOf(-1);
        this.removeFiles = Boolean.valueOf(false);
        this.anonymousMode = Boolean.valueOf(false);
        this.autoDownload = Boolean.valueOf(true);
        this.prepareSize = Long.valueOf(15728640);
        this.saveLocation = torrentOptions.saveLocation;
        this.proxyHost = torrentOptions.proxyHost;
        this.proxyUsername = torrentOptions.proxyUsername;
        this.proxyPassword = torrentOptions.proxyPassword;
        this.peerFingerprint = torrentOptions.peerFingerprint;
        this.maxDownloadSpeed = torrentOptions.maxDownloadSpeed;
        this.maxUploadSpeed = torrentOptions.maxUploadSpeed;
        this.maxConnections = torrentOptions.maxConnections;
        this.maxDht = torrentOptions.maxDht;
        this.listeningPort = torrentOptions.listeningPort;
        this.removeFiles = torrentOptions.removeFiles;
        this.anonymousMode = torrentOptions.anonymousMode;
        this.autoDownload = torrentOptions.autoDownload;
        this.prepareSize = torrentOptions.prepareSize;
    }

    public Builder toBuilder() {
        return new Builder();
    }
}
