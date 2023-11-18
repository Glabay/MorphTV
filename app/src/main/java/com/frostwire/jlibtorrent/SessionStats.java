package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.SessionStatsAlert;

public final class SessionStats {
    private static final int DOWNLOAD_IP_PROTOCOL = 5;
    private static final int DOWNLOAD_PAYLOAD = 3;
    private static final int DOWNLOAD_PROTOCOL = 4;
    private static final int NUM_AVERAGES = 6;
    private static final int UPLOAD_IP_PROTOCOL = 2;
    private static final int UPLOAD_PAYLOAD = 0;
    private static final int UPLOAD_PROTOCOL = 1;
    private long dhtNodes;
    private long lastTickTime;
    private final Average[] stat = new Average[6];

    private static final class Average {
        private long averageSec5;
        private long counter;
        private long totalCounter;

        public void add(long j) {
            this.counter += j;
            this.totalCounter += j;
        }

        public void tick(long j) {
            if (j >= 1) {
                this.averageSec5 = ((this.averageSec5 * 4) / 5) + (((this.counter * 1000) / j) / 5);
                this.counter = 0;
            }
        }

        public long rate() {
            return this.averageSec5;
        }

        public long total() {
            return this.totalCounter;
        }

        public void clear() {
            this.counter = 0;
            this.averageSec5 = 0;
            this.totalCounter = 0;
        }
    }

    SessionStats() {
        for (int i = 0; i < this.stat.length; i++) {
            this.stat[i] = new Average();
        }
    }

    public long totalDownload() {
        return (this.stat[3].total() + this.stat[4].total()) + this.stat[5].total();
    }

    public long totalUpload() {
        return (this.stat[0].total() + this.stat[1].total()) + this.stat[2].total();
    }

    public long downloadRate() {
        return (this.stat[3].rate() + this.stat[4].rate()) + this.stat[5].rate();
    }

    public long uploadRate() {
        return (this.stat[0].rate() + this.stat[1].rate()) + this.stat[2].rate();
    }

    public long dhtNodes() {
        return this.dhtNodes;
    }

    void update(SessionStatsAlert sessionStatsAlert) {
        SessionStatsAlert sessionStatsAlert2 = sessionStatsAlert;
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.lastTickTime;
        this.lastTickTime = currentTimeMillis;
        currentTimeMillis = sessionStatsAlert2.value(StatsMetric.NET_RECV_BYTES_COUNTER_INDEX);
        long value = sessionStatsAlert2.value(StatsMetric.NET_RECV_PAYLOAD_BYTES_COUNTER_INDEX);
        long j2 = currentTimeMillis - value;
        long j3 = j;
        long total = j2 - this.stat[4].total();
        long value2 = sessionStatsAlert2.value(StatsMetric.NET_RECV_IP_OVERHEAD_BYTES_COUNTER_INDEX) - this.stat[5].total();
        this.stat[3].add(value - this.stat[3].total());
        this.stat[4].add(total);
        this.stat[5].add(value2);
        currentTimeMillis = sessionStatsAlert2.value(StatsMetric.NET_SENT_BYTES_COUNTER_INDEX);
        value = sessionStatsAlert2.value(StatsMetric.NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX);
        j = currentTimeMillis - value;
        long total2 = j - this.stat[1].total();
        long value3 = sessionStatsAlert2.value(StatsMetric.NET_SENT_IP_OVERHEAD_BYTES_COUNTER_INDEX) - this.stat[2].total();
        this.stat[0].add(value - this.stat[0].total());
        this.stat[1].add(total2);
        this.stat[2].add(value3);
        tick(j3);
        this.dhtNodes = sessionStatsAlert2.value(StatsMetric.DHT_NODES_GAUGE_INDEX);
    }

    void clear() {
        for (int i = 0; i < 6; i++) {
            this.stat[i].clear();
        }
        this.dhtNodes = 0;
    }

    private void tick(long j) {
        for (int i = 0; i < 6; i++) {
            this.stat[i].tick(j);
        }
    }
}
