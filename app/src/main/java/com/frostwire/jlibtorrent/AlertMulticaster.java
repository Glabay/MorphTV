package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;

final class AlertMulticaster implements AlertListener {
    /* renamed from: a */
    private final AlertListener f15a;
    /* renamed from: b */
    private final AlertListener f16b;

    public int[] types() {
        return null;
    }

    public AlertMulticaster(AlertListener alertListener, AlertListener alertListener2) {
        this.f15a = alertListener;
        this.f16b = alertListener2;
    }

    public void alert(Alert<?> alert) {
        this.f15a.alert(alert);
        this.f16b.alert(alert);
    }

    public static AlertListener add(AlertListener alertListener, AlertListener alertListener2) {
        return addInternal(alertListener, alertListener2);
    }

    public static AlertListener remove(AlertListener alertListener, AlertListener alertListener2) {
        return removeInternal(alertListener, alertListener2);
    }

    private AlertListener remove(AlertListener alertListener) {
        if (alertListener == this.f15a) {
            return this.f16b;
        }
        if (alertListener == this.f16b) {
            return this.f15a;
        }
        AlertListener removeInternal = removeInternal(this.f15a, alertListener);
        alertListener = removeInternal(this.f16b, alertListener);
        if (removeInternal == this.f15a && alertListener == this.f16b) {
            return this;
        }
        return addInternal(removeInternal, alertListener);
    }

    private static AlertListener addInternal(AlertListener alertListener, AlertListener alertListener2) {
        if (alertListener == null) {
            return alertListener2;
        }
        return alertListener2 == null ? alertListener : new AlertMulticaster(alertListener, alertListener2);
    }

    private static AlertListener removeInternal(AlertListener alertListener, AlertListener alertListener2) {
        if (alertListener != alertListener2) {
            if (alertListener != null) {
                return alertListener instanceof AlertMulticaster ? ((AlertMulticaster) alertListener).remove(alertListener2) : alertListener;
            }
        }
        return null;
    }
}
