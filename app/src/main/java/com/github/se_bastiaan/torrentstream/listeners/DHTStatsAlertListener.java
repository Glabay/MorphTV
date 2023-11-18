package com.github.se_bastiaan.torrentstream.listeners;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.DhtRoutingBucket;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;

public abstract class DHTStatsAlertListener implements AlertListener {
    public abstract void stats(int i);

    public int[] types() {
        return new int[]{AlertType.DHT_STATS.swig()};
    }

    public void alert(Alert<?> alert) {
        if (alert instanceof DhtStatsAlert) {
            stats(countTotalDHTNodes((DhtStatsAlert) alert));
        }
    }

    private int countTotalDHTNodes(DhtStatsAlert dhtStatsAlert) {
        dhtStatsAlert = dhtStatsAlert.routingTable();
        int i = 0;
        if (dhtStatsAlert != null) {
            dhtStatsAlert = dhtStatsAlert.iterator();
            while (dhtStatsAlert.hasNext()) {
                i += ((DhtRoutingBucket) dhtStatsAlert.next()).numNodes();
            }
        }
        return i;
    }
}
