package com.tonyodev.fetch2;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u001c\b\u0001\u0018\u0000  2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001 B\u001b\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001f¨\u0006!"}, d2 = {"Lcom/tonyodev/fetch2/Error;", "", "value", "", "throwable", "", "(Ljava/lang/String;IILjava/lang/Throwable;)V", "getThrowable", "()Ljava/lang/Throwable;", "setThrowable", "(Ljava/lang/Throwable;)V", "getValue", "()I", "UNKNOWN", "NONE", "FILE_NOT_CREATED", "CONNECTION_TIMED_OUT", "UNKNOWN_HOST", "HTTP_NOT_FOUND", "WRITE_PERMISSION_DENIED", "NO_STORAGE_SPACE", "NO_NETWORK_CONNECTION", "EMPTY_RESPONSE_FROM_SERVER", "REQUEST_ALREADY_EXIST", "DOWNLOAD_NOT_FOUND", "FETCH_DATABASE_ERROR", "FETCH_ALREADY_EXIST", "REQUEST_WITH_ID_ALREADY_EXIST", "REQUEST_WITH_FILE_PATH_ALREADY_EXIST", "REQUEST_NOT_SUCCESSFUL", "UNKNOWN_IO_ERROR", "FILE_NOT_FOUND", "Companion", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Error.kt */
public enum Error {
    ;
    
    public static final Companion Companion = null;
    @Nullable
    private Throwable throwable;
    private final int value;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, d2 = {"Lcom/tonyodev/fetch2/Error$Companion;", "", "()V", "valueOf", "Lcom/tonyodev/fetch2/Error;", "value", "", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: Error.kt */
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Error valueOf(int i) {
            switch (i) {
                case -1:
                    return Error.UNKNOWN;
                case 0:
                    return Error.NONE;
                case 1:
                    return Error.FILE_NOT_CREATED;
                case 2:
                    return Error.CONNECTION_TIMED_OUT;
                case 3:
                    return Error.UNKNOWN_HOST;
                case 4:
                    return Error.HTTP_NOT_FOUND;
                case 5:
                    return Error.WRITE_PERMISSION_DENIED;
                case 6:
                    return Error.NO_STORAGE_SPACE;
                case 7:
                    return Error.NO_NETWORK_CONNECTION;
                case 8:
                    return Error.EMPTY_RESPONSE_FROM_SERVER;
                case 9:
                    return Error.REQUEST_ALREADY_EXIST;
                case 10:
                    return Error.DOWNLOAD_NOT_FOUND;
                case 11:
                    return Error.FETCH_DATABASE_ERROR;
                case 12:
                    return Error.FETCH_ALREADY_EXIST;
                case 13:
                    return Error.REQUEST_WITH_ID_ALREADY_EXIST;
                case 14:
                    return Error.REQUEST_WITH_FILE_PATH_ALREADY_EXIST;
                case 15:
                    return Error.REQUEST_NOT_SUCCESSFUL;
                case 16:
                    return Error.UNKNOWN_IO_ERROR;
                case 17:
                    return Error.FILE_NOT_FOUND;
                default:
                    return Error.UNKNOWN;
            }
        }
    }

    @JvmStatic
    @NotNull
    public static final Error valueOf(int i) {
        return Companion.valueOf(i);
    }

    protected Error(int i, @Nullable Throwable th) {
        this.value = i;
        this.throwable = th;
    }

    public final int getValue() {
        return this.value;
    }

    @Nullable
    public final Throwable getThrowable() {
        return this.throwable;
    }

    public final void setThrowable(@Nullable Throwable th) {
        this.throwable = th;
    }

    static {
        Companion = new Companion();
    }
}
