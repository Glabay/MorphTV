package com.tonyodev.fetch2;

import android.util.Log;
import com.tonyodev.fetch2.util.FetchDefaults;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0006H\u0016J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0006H\u0016J\u0018\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0017H\u0016R\u001a\u0010\b\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0014\u0010\u0005\u001a\u00020\u00068BX\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u000e\"\u0004\b\u0011\u0010\u0012¨\u0006\u0019"}, d2 = {"Lcom/tonyodev/fetch2/FetchLogger;", "Lcom/tonyodev/fetch2/Logger;", "()V", "loggingEnabled", "", "loggingTag", "", "(ZLjava/lang/String;)V", "enabled", "getEnabled", "()Z", "setEnabled", "(Z)V", "getLoggingTag", "()Ljava/lang/String;", "tag", "getTag", "setTag", "(Ljava/lang/String;)V", "d", "", "message", "throwable", "", "e", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchLogger.kt */
public class FetchLogger implements Logger {
    private boolean enabled;
    @NotNull
    private String tag;

    public FetchLogger(boolean z, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "loggingTag");
        this.enabled = z;
        this.tag = str;
    }

    public FetchLogger() {
        this(false, FetchDefaults.DEFAULT_TAG);
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    @NotNull
    public final String getTag() {
        return this.tag;
    }

    public final void setTag(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.tag = str;
    }

    private final String getLoggingTag() {
        if (this.tag.length() > 23) {
            return FetchDefaults.DEFAULT_TAG;
        }
        return this.tag;
    }

    /* renamed from: d */
    public void mo4160d(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        if (getEnabled()) {
            Log.d(getLoggingTag(), str);
        }
    }

    /* renamed from: d */
    public void mo4161d(@NotNull String str, @NotNull Throwable th) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "throwable");
        if (getEnabled()) {
            Log.d(getLoggingTag(), str, th);
        }
    }

    /* renamed from: e */
    public void mo4162e(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        if (getEnabled()) {
            Log.e(getLoggingTag(), str);
        }
    }

    /* renamed from: e */
    public void mo4163e(@NotNull String str, @NotNull Throwable th) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "throwable");
        if (getEnabled()) {
            Log.e(getLoggingTag(), str, th);
        }
    }
}
