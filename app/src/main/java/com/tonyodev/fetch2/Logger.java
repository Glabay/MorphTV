package com.tonyodev.fetch2;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH&J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH&J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH&J\u0018\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH&R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\u000f"}, d2 = {"Lcom/tonyodev/fetch2/Logger;", "", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "d", "", "message", "", "throwable", "", "e", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Logger.kt */
public interface Logger {
    /* renamed from: d */
    void mo4160d(@NotNull String str);

    /* renamed from: d */
    void mo4161d(@NotNull String str, @NotNull Throwable th);

    /* renamed from: e */
    void mo4162e(@NotNull String str);

    /* renamed from: e */
    void mo4163e(@NotNull String str, @NotNull Throwable th);

    boolean getEnabled();

    void setEnabled(boolean z);
}
