package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.stats_alert;
import com.frostwire.jlibtorrent.swig.stats_alert.stats_channel;

public final class StatsAlert extends TorrentAlert<stats_alert> {

    public enum StatsChannel {
        UPLOAD_PAYLOAD(stats_channel.upload_payload.swigValue()),
        UPlOAD_PROTOCOL(stats_channel.upload_protocol.swigValue()),
        DOWNLOAD_PAYLOAD(stats_channel.download_payload.swigValue()),
        DOWNLOAD_PROTOCOL(stats_channel.download_protocol.swigValue()),
        UPLOAD_IP_PROTOCOL(stats_channel.upload_ip_protocol.swigValue()),
        DOWNLOAD_IP_PROTOCOL(stats_channel.download_ip_protocol.swigValue()),
        NUM_CHANNELS(stats_channel.num_channels.swigValue());
        
        private final int swigValue;

        private StatsChannel(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public int getIndex() {
            return this.swigValue;
        }
    }

    StatsAlert(stats_alert stats_alert) {
        super(stats_alert);
    }

    public int transferred(int i) {
        return ((stats_alert) this.alert).get_transferred(i);
    }

    public int interval() {
        return ((stats_alert) this.alert).getInterval();
    }
}
