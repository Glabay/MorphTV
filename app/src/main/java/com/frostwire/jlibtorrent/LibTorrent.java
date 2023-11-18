package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.stats_metric_vector;
import java.util.ArrayList;
import java.util.List;

public final class LibTorrent {
    public static String jlibtorrentVersion() {
        return "1.2.0.12";
    }

    public static String revision() {
        return "f311bf354e5721e5110f580ee40b38f774dea1fe";
    }

    private LibTorrent() {
    }

    public static int versionNum() {
        return libtorrent.LIBTORRENT_VERSION_NUM;
    }

    public static String version() {
        return libtorrent.version();
    }

    public static int boostVersionNum() {
        return libtorrent.boost_version();
    }

    public static String boostVersion() {
        return libtorrent.boost_lib_version();
    }

    public static int opensslVersionNum() {
        return libtorrent.openssl_version_number();
    }

    public static String opensslVersion() {
        return libtorrent.openssl_version_text();
    }

    public static List<StatsMetric> sessionStatsMetrics() {
        stats_metric_vector session_stats_metrics = libtorrent.session_stats_metrics();
        int size = (int) session_stats_metrics.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new StatsMetric(session_stats_metrics.get(i)));
        }
        return arrayList;
    }

    public static int findMetricIdx(String str) {
        return libtorrent.find_metric_idx(str);
    }
}
