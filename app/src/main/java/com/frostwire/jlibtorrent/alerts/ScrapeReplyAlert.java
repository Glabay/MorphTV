package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.scrape_reply_alert;

public final class ScrapeReplyAlert extends TrackerAlert<scrape_reply_alert> {
    public ScrapeReplyAlert(scrape_reply_alert scrape_reply_alert) {
        super(scrape_reply_alert);
    }

    public int getIncomplete() {
        return ((scrape_reply_alert) this.alert).getIncomplete();
    }

    public int getComplete() {
        return ((scrape_reply_alert) this.alert).getComplete();
    }
}
