package com.frostwire.jlibtorrent.swig;

public class picker_log_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.picker_log_alert_alert_type_get();
    public static final picker_flags_t backup1 = new picker_flags_t(libtorrent_jni.picker_log_alert_backup1_get(), false);
    public static final picker_flags_t backup2 = new picker_flags_t(libtorrent_jni.picker_log_alert_backup2_get(), false);
    public static final picker_flags_t end_game = new picker_flags_t(libtorrent_jni.picker_log_alert_end_game_get(), false);
    public static final picker_flags_t partial_ratio = new picker_flags_t(libtorrent_jni.picker_log_alert_partial_ratio_get(), false);
    public static final picker_flags_t prefer_contiguous = new picker_flags_t(libtorrent_jni.picker_log_alert_prefer_contiguous_get(), false);
    public static final picker_flags_t prio_sequential_pieces = new picker_flags_t(libtorrent_jni.picker_log_alert_prio_sequential_pieces_get(), false);
    public static final picker_flags_t prioritize_partials = new picker_flags_t(libtorrent_jni.picker_log_alert_prioritize_partials_get(), false);
    public static final int priority = libtorrent_jni.picker_log_alert_priority_get();
    public static final picker_flags_t random_pieces = new picker_flags_t(libtorrent_jni.picker_log_alert_random_pieces_get(), false);
    public static final picker_flags_t rarest_first = new picker_flags_t(libtorrent_jni.picker_log_alert_rarest_first_get(), false);
    public static final picker_flags_t rarest_first_partials = new picker_flags_t(libtorrent_jni.picker_log_alert_rarest_first_partials_get(), false);
    public static final picker_flags_t reverse_pieces = new picker_flags_t(libtorrent_jni.picker_log_alert_reverse_pieces_get(), false);
    public static final picker_flags_t reverse_rarest_first = new picker_flags_t(libtorrent_jni.picker_log_alert_reverse_rarest_first_get(), false);
    public static final picker_flags_t reverse_sequential = new picker_flags_t(libtorrent_jni.picker_log_alert_reverse_sequential_get(), false);
    public static final picker_flags_t sequential_pieces = new picker_flags_t(libtorrent_jni.picker_log_alert_sequential_pieces_get(), false);
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.picker_log_alert_static_category_get(), false);
    public static final picker_flags_t suggested_pieces = new picker_flags_t(libtorrent_jni.picker_log_alert_suggested_pieces_get(), false);
    public static final picker_flags_t time_critical = new picker_flags_t(libtorrent_jni.picker_log_alert_time_critical_get(), false);
    private transient long swigCPtr;

    protected picker_log_alert(long j, boolean z) {
        super(libtorrent_jni.picker_log_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(picker_log_alert picker_log_alert) {
        return picker_log_alert == null ? 0 : picker_log_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_picker_log_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.picker_log_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.picker_log_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.picker_log_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.picker_log_alert_message(this.swigCPtr, this);
    }

    public picker_flags_t getPicker_flags() {
        long picker_log_alert_picker_flags_get = libtorrent_jni.picker_log_alert_picker_flags_get(this.swigCPtr, this);
        if (picker_log_alert_picker_flags_get == 0) {
            return null;
        }
        return new picker_flags_t(picker_log_alert_picker_flags_get, false);
    }
}
