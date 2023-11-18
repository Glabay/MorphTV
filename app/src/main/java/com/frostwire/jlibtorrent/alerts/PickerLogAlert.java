package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.picker_flags_t;
import com.frostwire.jlibtorrent.swig.picker_log_alert;

public final class PickerLogAlert extends PeerAlert<picker_log_alert> {
    public static final picker_flags_t BACKUP1 = picker_log_alert.backup1;
    public static final picker_flags_t BACKUP2 = picker_log_alert.backup2;
    public static final picker_flags_t END_GAME = picker_log_alert.end_game;
    public static final picker_flags_t PARTIAL_RATIO = picker_log_alert.partial_ratio;
    public static final picker_flags_t PREFER_CONTIGUOUS = picker_log_alert.prefer_contiguous;
    public static final picker_flags_t PRIORITIZE_PARTIALS = picker_log_alert.prioritize_partials;
    public static final picker_flags_t PRIO_SEQUENTIAL_PIECES = picker_log_alert.prio_sequential_pieces;
    public static final picker_flags_t RANDOM_PIECES = picker_log_alert.random_pieces;
    public static final picker_flags_t RAREST_FIRST = picker_log_alert.rarest_first;
    public static final picker_flags_t RAREST_FIRST_PARTIALS = picker_log_alert.rarest_first_partials;
    public static final picker_flags_t REVERSE_PIECES = picker_log_alert.time_critical;
    public static final picker_flags_t REVERSE_RAREST_FIRST = picker_log_alert.reverse_rarest_first;
    public static final picker_flags_t REVERSE_SEQUENTIAL = picker_log_alert.reverse_sequential;
    public static final picker_flags_t SEQUENTIAL_PIECES = picker_log_alert.sequential_pieces;
    public static final picker_flags_t SUGGESTED_PIECES = picker_log_alert.suggested_pieces;
    public static final picker_flags_t TIME_CRITICAL = picker_log_alert.time_critical;

    PickerLogAlert(picker_log_alert picker_log_alert) {
        super(picker_log_alert);
    }

    public picker_flags_t pickerFlags() {
        return ((picker_log_alert) this.alert).getPicker_flags();
    }
}
