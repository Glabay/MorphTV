package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.swig.state_update_alert;
import com.frostwire.jlibtorrent.swig.torrent_status_vector;
import java.util.ArrayList;
import java.util.List;

public final class StateUpdateAlert extends AbstractAlert<state_update_alert> {
    StateUpdateAlert(state_update_alert state_update_alert) {
        super(state_update_alert);
    }

    public List<TorrentStatus> status() {
        torrent_status_vector status = ((state_update_alert) this.alert).getStatus();
        int size = (int) status.size();
        List<TorrentStatus> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new TorrentStatus(status.get(i)));
        }
        return arrayList;
    }
}
