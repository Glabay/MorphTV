package com.tonyodev.fetch2.provider;

import android.content.Context;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.util.FetchAndroidExtensions;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007¨\u0006\u000b"}, d2 = {"Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "isNetworkAvailable", "", "()Z", "isOnAllowedNetwork", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: NetworkInfoProvider.kt */
public final class NetworkInfoProvider {
    private final Context context;

    public NetworkInfoProvider(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.context = context;
    }

    public final boolean isOnAllowedNetwork(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "networkType");
        if (networkType == NetworkType.WIFI_ONLY && FetchAndroidExtensions.isOnWiFi(this.context)) {
            return true;
        }
        if (networkType != NetworkType.ALL || FetchAndroidExtensions.isNetworkAvailable(this.context) == null) {
            return null;
        }
        return true;
    }

    public final boolean isNetworkAvailable() {
        return FetchAndroidExtensions.isNetworkAvailable(this.context);
    }
}
