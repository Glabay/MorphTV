package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentStatus.State;
import com.frostwire.jlibtorrent.swig.state_changed_alert;

public final class StateChangedAlert extends TorrentAlert<state_changed_alert> {
    public StateChangedAlert(state_changed_alert state_changed_alert) {
        super(state_changed_alert);
    }

    public State getState() {
        return State.fromSwig(((state_changed_alert) this.alert).getState().swigValue());
    }

    public State getPrevState() {
        return State.fromSwig(((state_changed_alert) this.alert).getPrev_state().swigValue());
    }
}
