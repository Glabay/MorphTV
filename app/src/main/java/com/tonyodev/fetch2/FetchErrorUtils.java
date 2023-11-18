package com.tonyodev.fetch2;

import com.tonyodev.fetch2.util.FetchErrorStrings;
import java.io.IOException;
import java.net.SocketTimeoutException;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u001a\u000e\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"getErrorFromMessage", "Lcom/tonyodev/fetch2/Error;", "message", "", "getErrorFromThrowable", "throwable", "", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchErrorUtils")
/* compiled from: FetchErrorUtils.kt */
public final class FetchErrorUtils {
    @NotNull
    public static final Error getErrorFromThrowable(@NotNull Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "throwable");
        String message = th.getMessage();
        if (message == null) {
            message = "";
        }
        boolean z = th instanceof SocketTimeoutException;
        if (z) {
            if ((((CharSequence) message).length() == 0 ? 1 : null) != null) {
                message = FetchErrorStrings.CONNECTION_TIMEOUT;
            }
        }
        Error errorFromMessage = getErrorFromMessage(message);
        if (errorFromMessage == Error.UNKNOWN && z) {
            return Error.CONNECTION_TIMED_OUT;
        }
        return (errorFromMessage != Error.UNKNOWN || (th instanceof IOException) == null) ? errorFromMessage : Error.UNKNOWN_IO_ERROR;
    }

    @NotNull
    public static final Error getErrorFromMessage(@Nullable String str) {
        if (str != null) {
            CharSequence charSequence = str;
            if ((charSequence.length() == 0 ? 1 : null) == null) {
                if (StringsKt.contains$default(charSequence, FetchErrorStrings.UNIQUE_ID_DATABASE, false, 2, null)) {
                    return Error.REQUEST_WITH_ID_ALREADY_EXIST;
                }
                if (StringsKt.contains$default(charSequence, FetchErrorStrings.UNIQUE_FILE_PATH_DATABASE, false, 2, null)) {
                    return Error.REQUEST_WITH_FILE_PATH_ALREADY_EXIST;
                }
                if (StringsKt.equals(str, FetchErrorStrings.EMPTY_RESPONSE_BODY, true)) {
                    return Error.EMPTY_RESPONSE_FROM_SERVER;
                }
                if (!StringsKt.equals(str, FetchErrorStrings.FNC, true)) {
                    if (!StringsKt.equals(str, FetchErrorStrings.ENOENT, true)) {
                        if (!(StringsKt.contains(charSequence, FetchErrorStrings.ETIMEDOUT, true) || StringsKt.contains(charSequence, FetchErrorStrings.CONNECTION_TIMEOUT, true) || StringsKt.contains(charSequence, FetchErrorStrings.SOFTWARE_ABORT_CONNECTION, true))) {
                            if (!StringsKt.contains(charSequence, FetchErrorStrings.READ_TIME_OUT, true)) {
                                if (!StringsKt.equals(str, FetchErrorStrings.IO404, true)) {
                                    if (!StringsKt.contains$default(charSequence, FetchErrorStrings.NO_ADDRESS_HOSTNAME, false, 2, null)) {
                                        if (StringsKt.contains$default(charSequence, FetchErrorStrings.HOST_RESOLVE_ISSUE, false, 2, null)) {
                                            return Error.UNKNOWN_HOST;
                                        }
                                        if (StringsKt.equals(str, FetchErrorStrings.EACCES, true)) {
                                            return Error.WRITE_PERMISSION_DENIED;
                                        }
                                        if (!StringsKt.equals(str, FetchErrorStrings.ENOSPC, true)) {
                                            if (!StringsKt.equals(str, FetchErrorStrings.DATABASE_DISK_FULL, true)) {
                                                if (StringsKt.equals(str, FetchErrorStrings.FAILED_TO_ENQUEUE_REQUEST, true)) {
                                                    return Error.REQUEST_ALREADY_EXIST;
                                                }
                                                if (StringsKt.equals(str, FetchErrorStrings.DOWNLOAD_NOT_FOUND, true)) {
                                                    return Error.DOWNLOAD_NOT_FOUND;
                                                }
                                                if (StringsKt.equals(str, FetchErrorStrings.FETCH_DATABASE_ERROR, true) != null) {
                                                    return Error.FETCH_DATABASE_ERROR;
                                                }
                                                if (StringsKt.contains(charSequence, FetchErrorStrings.FETCH_ALREADY_EXIST, true) != null) {
                                                    return Error.FETCH_ALREADY_EXIST;
                                                }
                                                if (StringsKt.contains(charSequence, FetchErrorStrings.RESPONSE_NOT_SUCCESSFUL, true) == null) {
                                                    if (StringsKt.contains(charSequence, FetchErrorStrings.FAILED_TO_CONNECT, true) == null) {
                                                        return Error.UNKNOWN;
                                                    }
                                                }
                                                return Error.REQUEST_NOT_SUCCESSFUL;
                                            }
                                        }
                                        return Error.NO_STORAGE_SPACE;
                                    }
                                }
                                return Error.HTTP_NOT_FOUND;
                            }
                        }
                        return Error.CONNECTION_TIMED_OUT;
                    }
                }
                return Error.FILE_NOT_CREATED;
            }
        }
        return Error.UNKNOWN;
    }
}
