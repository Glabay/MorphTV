package com.tonyodev.fetch2.util;

import android.content.Context;
import android.net.ConnectivityManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0004"}, d2 = {"isNetworkAvailable", "", "Landroid/content/Context;", "isOnWiFi", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchAndroidExtensions")
/* compiled from: AndroidExtentions.kt */
public final class FetchAndroidExtensions {
    public static final boolean isOnWiFi(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "$receiver");
        context = context.getSystemService("connectivity");
        if (context == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.net.ConnectivityManager");
        }
        context = ((ConnectivityManager) context).getActiveNetworkInfo();
        if (context != null && context.isConnected() && context.getType() == 1) {
            return true;
        }
        return false;
    }

    public static final boolean isNetworkAvailable(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "$receiver");
        context = context.getSystemService("connectivity");
        if (context == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.net.ConnectivityManager");
        }
        context = ((ConnectivityManager) context).getActiveNetworkInfo();
        return (context == null || context.isConnected() == null) ? null : true;
    }
}
