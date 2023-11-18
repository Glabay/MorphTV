package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;

public abstract class AbstractAlert<T extends alert> implements Alert<T> {
    protected final T alert;
    private final AlertType type;

    AbstractAlert(T t) {
        this.alert = t;
        this.type = AlertType.fromSwig(t.type());
    }

    public final T swig() {
        return this.alert;
    }

    public long timestamp() {
        return this.alert.get_timestamp();
    }

    public AlertType type() {
        return this.type;
    }

    public String what() {
        return this.alert.what();
    }

    public String message() {
        return this.alert.message();
    }

    public alert_category_t category() {
        return this.alert.category();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type());
        stringBuilder.append(" - ");
        stringBuilder.append(what());
        stringBuilder.append(" - ");
        stringBuilder.append(message());
        return stringBuilder.toString();
    }
}
