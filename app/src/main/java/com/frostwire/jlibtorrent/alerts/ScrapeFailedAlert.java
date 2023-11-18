package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.scrape_failed_alert;

public final class ScrapeFailedAlert extends TrackerAlert<scrape_failed_alert> {
    public ScrapeFailedAlert(scrape_failed_alert scrape_failed_alert) {
        super(scrape_failed_alert);
    }

    public ErrorCode error() {
        return new ErrorCode(((scrape_failed_alert) this.alert).getError());
    }

    public String errorMessage() {
        return ((scrape_failed_alert) this.alert).error_message();
    }
}
