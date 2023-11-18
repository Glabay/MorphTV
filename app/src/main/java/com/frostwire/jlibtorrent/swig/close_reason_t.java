package com.frostwire.jlibtorrent.swig;

import com.tonyodev.fetch2.util.FetchErrorStrings;

public final class close_reason_t {
    public static final close_reason_t blocked = new close_reason_t("blocked");
    public static final close_reason_t corrupt_pieces = new close_reason_t("corrupt_pieces");
    public static final close_reason_t duplicate_peer_id = new close_reason_t("duplicate_peer_id");
    public static final close_reason_t encryption_error = new close_reason_t("encryption_error", libtorrent_jni.close_reason_t_encryption_error_get());
    public static final close_reason_t invalid_allow_fast_message = new close_reason_t("invalid_allow_fast_message");
    public static final close_reason_t invalid_bitfield_message = new close_reason_t("invalid_bitfield_message");
    public static final close_reason_t invalid_cancel_message = new close_reason_t("invalid_cancel_message");
    public static final close_reason_t invalid_choke_message = new close_reason_t("invalid_choke_message");
    public static final close_reason_t invalid_dht_port_message = new close_reason_t("invalid_dht_port_message");
    public static final close_reason_t invalid_dont_have_message = new close_reason_t("invalid_dont_have_message");
    public static final close_reason_t invalid_extended_message = new close_reason_t("invalid_extended_message");
    public static final close_reason_t invalid_have_all_message = new close_reason_t("invalid_have_all_message");
    public static final close_reason_t invalid_have_message = new close_reason_t("invalid_have_message");
    public static final close_reason_t invalid_have_none_message = new close_reason_t("invalid_have_none_message");
    public static final close_reason_t invalid_info_hash = new close_reason_t("invalid_info_hash");
    public static final close_reason_t invalid_interested_message = new close_reason_t("invalid_interested_message");
    public static final close_reason_t invalid_message = new close_reason_t("invalid_message");
    public static final close_reason_t invalid_message_id = new close_reason_t("invalid_message_id");
    public static final close_reason_t invalid_metadata = new close_reason_t("invalid_metadata");
    public static final close_reason_t invalid_metadata_message = new close_reason_t("invalid_metadata_message");
    public static final close_reason_t invalid_metadata_offset = new close_reason_t("invalid_metadata_offset");
    public static final close_reason_t invalid_metadata_request_message = new close_reason_t("invalid_metadata_request_message");
    public static final close_reason_t invalid_not_interested_message = new close_reason_t("invalid_not_interested_message");
    public static final close_reason_t invalid_pex_message = new close_reason_t("invalid_pex_message");
    public static final close_reason_t invalid_piece_message = new close_reason_t("invalid_piece_message");
    public static final close_reason_t invalid_reject_message = new close_reason_t("invalid_reject_message");
    public static final close_reason_t invalid_request_message = new close_reason_t("invalid_request_message");
    public static final close_reason_t invalid_suggest_message = new close_reason_t("invalid_suggest_message");
    public static final close_reason_t invalid_unchoke_message = new close_reason_t("invalid_unchoke_message");
    public static final close_reason_t message_too_big = new close_reason_t("message_too_big");
    public static final close_reason_t metadata_too_big = new close_reason_t("metadata_too_big");
    public static final close_reason_t no_memory = new close_reason_t("no_memory");
    public static final close_reason_t none = new close_reason_t("none", libtorrent_jni.close_reason_t_none_get());
    public static final close_reason_t not_interested_upload_only = new close_reason_t("not_interested_upload_only");
    public static final close_reason_t peer_churn = new close_reason_t("peer_churn");
    public static final close_reason_t pex_message_too_big = new close_reason_t("pex_message_too_big");
    public static final close_reason_t pex_too_frequent = new close_reason_t("pex_too_frequent");
    public static final close_reason_t port_blocked = new close_reason_t("port_blocked");
    public static final close_reason_t protocol_blocked = new close_reason_t("protocol_blocked");
    public static final close_reason_t request_when_choked = new close_reason_t("request_when_choked");
    public static final close_reason_t self_connection = new close_reason_t("self_connection");
    private static int swigNext;
    private static close_reason_t[] swigValues = new close_reason_t[]{none, duplicate_peer_id, torrent_removed, no_memory, port_blocked, blocked, upload_to_upload, not_interested_upload_only, timeout, timed_out_interest, timed_out_activity, timed_out_handshake, timed_out_request, protocol_blocked, peer_churn, too_many_connections, too_many_files, encryption_error, invalid_info_hash, self_connection, invalid_metadata, metadata_too_big, message_too_big, invalid_message_id, invalid_message, invalid_piece_message, invalid_have_message, invalid_bitfield_message, invalid_choke_message, invalid_unchoke_message, invalid_interested_message, invalid_not_interested_message, invalid_request_message, invalid_reject_message, invalid_allow_fast_message, invalid_extended_message, invalid_cancel_message, invalid_dht_port_message, invalid_suggest_message, invalid_have_all_message, invalid_dont_have_message, invalid_have_none_message, invalid_pex_message, invalid_metadata_request_message, invalid_metadata_message, invalid_metadata_offset, request_when_choked, corrupt_pieces, pex_message_too_big, pex_too_frequent};
    public static final close_reason_t timed_out_activity = new close_reason_t("timed_out_activity");
    public static final close_reason_t timed_out_handshake = new close_reason_t("timed_out_handshake");
    public static final close_reason_t timed_out_interest = new close_reason_t("timed_out_interest");
    public static final close_reason_t timed_out_request = new close_reason_t("timed_out_request");
    public static final close_reason_t timeout = new close_reason_t(FetchErrorStrings.CONNECTION_TIMEOUT);
    public static final close_reason_t too_many_connections = new close_reason_t("too_many_connections");
    public static final close_reason_t too_many_files = new close_reason_t("too_many_files");
    public static final close_reason_t torrent_removed = new close_reason_t("torrent_removed");
    public static final close_reason_t upload_to_upload = new close_reason_t("upload_to_upload");
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static close_reason_t swigToEnum(int i) {
        if (i < swigValues.length && i >= 0 && swigValues[i].swigValue == i) {
            return swigValues[i];
        }
        for (int i2 = 0; i2 < swigValues.length; i2++) {
            if (swigValues[i2].swigValue == i) {
                return swigValues[i2];
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No enum ");
        stringBuilder.append(close_reason_t.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private close_reason_t(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private close_reason_t(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private close_reason_t(String str, close_reason_t close_reason_t) {
        this.swigName = str;
        this.swigValue = close_reason_t.swigValue;
        swigNext = this.swigValue + 1;
    }
}
