package com.tonyodev.fetch2.exception;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u00002\u00060\u0001j\u0002`\u0002:\u0001\nB\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u000b"}, d2 = {"Lcom/tonyodev/fetch2/exception/FetchException;", "Ljava/lang/RuntimeException;", "Lkotlin/RuntimeException;", "message", "", "code", "Lcom/tonyodev/fetch2/exception/FetchException$Code;", "(Ljava/lang/String;Lcom/tonyodev/fetch2/exception/FetchException$Code;)V", "getCode", "()Lcom/tonyodev/fetch2/exception/FetchException$Code;", "Code", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchException.kt */
public class FetchException extends RuntimeException {
    @NotNull
    private final Code code;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000f\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f¨\u0006\u0010"}, d2 = {"Lcom/tonyodev/fetch2/exception/FetchException$Code;", "", "(Ljava/lang/String;I)V", "NONE", "INITIALIZATION", "INCOMPLETE_INITIALIZATION", "ILLEGAL_ARGUMENT", "CLOSED", "EMPTY_RESPONSE_BODY", "UNKNOWN", "REQUEST_NOT_SUCCESSFUL", "FETCH_INSTANCE_WITH_NAMESPACE_ALREADY_EXIST", "LOGGER", "ILLEGAL_CONCURRENT_INSERT", "INVALID_STATUS", "DOWNLOAD_NOT_FOUND", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: FetchException.kt */
    public enum Code {
    }

    public FetchException(@NotNull String str, @NotNull Code code) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(code, "code");
        super(str);
        this.code = code;
    }

    public /* synthetic */ FetchException(String str, Code code, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 2) != 0) {
            code = Code.NONE;
        }
        this(str, code);
    }

    @NotNull
    public final Code getCode() {
        return this.code;
    }
}
