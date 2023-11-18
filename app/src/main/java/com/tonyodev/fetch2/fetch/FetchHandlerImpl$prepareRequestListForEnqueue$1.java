package com.tonyodev.fetch2.fetch;

import com.tonyodev.fetch2.Request;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lcom/tonyodev/fetch2/Request;", "invoke"}, k = 3, mv = {1, 1, 10})
/* compiled from: FetchHandlerImpl.kt */
final class FetchHandlerImpl$prepareRequestListForEnqueue$1 extends Lambda implements Function1<Request, Integer> {
    public static final FetchHandlerImpl$prepareRequestListForEnqueue$1 INSTANCE = new FetchHandlerImpl$prepareRequestListForEnqueue$1();

    FetchHandlerImpl$prepareRequestListForEnqueue$1() {
        super(1);
    }

    public final int invoke(@NotNull Request request) {
        Intrinsics.checkParameterIsNotNull(request, "it");
        return request.getId();
    }
}
