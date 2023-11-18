package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.stats_metric;
import com.frostwire.jlibtorrent.swig.stats_metric.metric_type_t;

public final class StatsMetric {
    public static final int DHT_NODES_GAUGE_INDEX = LibTorrent.findMetricIdx(DHT_NODES_GAUGE_NAME);
    public static final String DHT_NODES_GAUGE_NAME = "dht.dht_nodes";
    public static final int NET_RECV_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_RECV_BYTES_COUNTER_NAME);
    public static final String NET_RECV_BYTES_COUNTER_NAME = "net.recv_bytes";
    public static final int NET_RECV_IP_OVERHEAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_RECV_IP_OVERHEAD_BYTES_COUNTER_NAME);
    public static final String NET_RECV_IP_OVERHEAD_BYTES_COUNTER_NAME = "net.recv_ip_overhead_bytes";
    public static final int NET_RECV_PAYLOAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_RECV_PAYLOAD_BYTES_COUNTER_NAME);
    public static final String NET_RECV_PAYLOAD_BYTES_COUNTER_NAME = "net.recv_payload_bytes";
    public static final int NET_SENT_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_SENT_BYTES_COUNTER_NAME);
    public static final String NET_SENT_BYTES_COUNTER_NAME = "net.sent_bytes";
    public static final int NET_SENT_IP_OVERHEAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_SENT_IP_OVERHEAD_BYTES_COUNTER_NAME);
    public static final String NET_SENT_IP_OVERHEAD_BYTES_COUNTER_NAME = "net.sent_ip_overhead_bytes";
    public static final int NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_SENT_PAYLOAD_BYTES_COUNTER_NAME);
    public static final String NET_SENT_PAYLOAD_BYTES_COUNTER_NAME = "net.sent_payload_bytes";
    public static final int TYPE_COUNTER = metric_type_t.type_counter.swigValue();
    public static final int TYPE_GAUGE = metric_type_t.type_gauge.swigValue();
    public final String name;
    public final int type;
    public final int valueIndex;

    StatsMetric(stats_metric stats_metric) {
        this.name = stats_metric.get_name();
        this.valueIndex = stats_metric.getValue_index();
        this.type = stats_metric.getType().swigValue();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append(":");
        stringBuilder.append(this.valueIndex);
        stringBuilder.append(":");
        stringBuilder.append(typeStr());
        return stringBuilder.toString();
    }

    private String typeStr() {
        String str = "unknown";
        if (this.type == TYPE_COUNTER) {
            return "counter";
        }
        return this.type == TYPE_GAUGE ? "gauge" : str;
    }
}
