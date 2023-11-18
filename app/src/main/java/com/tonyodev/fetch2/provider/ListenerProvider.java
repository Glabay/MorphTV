package com.tonyodev.fetch2.provider;

import com.tonyodev.fetch2.FetchListener;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lcom/tonyodev/fetch2/provider/ListenerProvider;", "", "()V", "listeners", "", "Lcom/tonyodev/fetch2/FetchListener;", "getListeners", "()Ljava/util/Set;", "mainListener", "getMainListener", "()Lcom/tonyodev/fetch2/FetchListener;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: ListenerProvider.kt */
public final class ListenerProvider {
    @NotNull
    private final Set<FetchListener> listeners;
    @NotNull
    private final FetchListener mainListener = new ListenerProvider$mainListener$1(this);

    public ListenerProvider() {
        Set synchronizedSet = Collections.synchronizedSet(new LinkedHashSet());
        Intrinsics.checkExpressionValueIsNotNull(synchronizedSet, "Collections.synchronized…leSetOf<FetchListener>())");
        this.listeners = synchronizedSet;
    }

    @NotNull
    public final Set<FetchListener> getListeners() {
        return this.listeners;
    }

    @NotNull
    public final FetchListener getMainListener() {
        return this.mainListener;
    }
}
