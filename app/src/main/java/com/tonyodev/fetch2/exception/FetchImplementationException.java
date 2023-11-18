package com.tonyodev.fetch2.exception;

import com.tonyodev.fetch2.exception.FetchException.Code;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"Lcom/tonyodev/fetch2/exception/FetchImplementationException;", "Lcom/tonyodev/fetch2/exception/FetchException;", "message", "", "code", "Lcom/tonyodev/fetch2/exception/FetchException$Code;", "(Ljava/lang/String;Lcom/tonyodev/fetch2/exception/FetchException$Code;)V", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchImplementationException.kt */
public final class FetchImplementationException extends FetchException {
    public FetchImplementationException(@NotNull String str, @NotNull Code code) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(code, "code");
        super(str, code);
    }

    public /* synthetic */ FetchImplementationException(String str, Code code, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 2) != 0) {
            code = Code.NONE;
        }
        this(str, code);
    }
}
