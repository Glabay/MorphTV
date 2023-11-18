package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.session_params;

public class SessionParams {
    /* renamed from: p */
    private final session_params f31p;

    public SessionParams(session_params session_params) {
        this.f31p = session_params;
    }

    public SessionParams() {
        this(new session_params());
    }

    public SessionParams(SettingsPack settingsPack) {
        this(new session_params(settingsPack.swig()));
    }

    public session_params swig() {
        return this.f31p;
    }

    public SettingsPack settings() {
        return new SettingsPack(this.f31p.getSettings());
    }
}
