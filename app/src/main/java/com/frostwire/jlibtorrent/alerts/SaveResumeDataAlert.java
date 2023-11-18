package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.swig.save_resume_data_alert;

public final class SaveResumeDataAlert extends TorrentAlert<save_resume_data_alert> {
    SaveResumeDataAlert(save_resume_data_alert save_resume_data_alert) {
        super(save_resume_data_alert);
    }

    public AddTorrentParams params() {
        return new AddTorrentParams(((save_resume_data_alert) this.alert).getParams());
    }
}
