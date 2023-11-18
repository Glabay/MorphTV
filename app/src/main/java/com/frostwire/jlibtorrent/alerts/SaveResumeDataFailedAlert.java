package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.save_resume_data_failed_alert;

public final class SaveResumeDataFailedAlert extends TorrentAlert<save_resume_data_failed_alert> {
    public SaveResumeDataFailedAlert(save_resume_data_failed_alert save_resume_data_failed_alert) {
        super(save_resume_data_failed_alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(((save_resume_data_failed_alert) this.alert).getError());
    }
}
