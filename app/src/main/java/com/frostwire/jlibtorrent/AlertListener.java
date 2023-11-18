package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;

public interface AlertListener {
    void alert(Alert<?> alert);

    int[] types();
}
