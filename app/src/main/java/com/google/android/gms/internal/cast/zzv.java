package com.google.android.gms.internal.cast;

import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.RouteInfo;
import com.google.android.gms.common.internal.Preconditions;

public final class zzv extends Callback {
    private static final zzdg zzbd = new zzdg("MediaRouterCallback");
    private final zzl zzjb;

    public zzv(zzl zzl) {
        this.zzjb = (zzl) Preconditions.checkNotNull(zzl);
    }

    public final void onRouteAdded(MediaRouter mediaRouter, RouteInfo routeInfo) {
        try {
            this.zzjb.zza(routeInfo.getId(), routeInfo.getExtras());
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onRouteAdded", zzl.class.getSimpleName());
        }
    }

    public final void onRouteChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
        try {
            this.zzjb.zzb(routeInfo.getId(), routeInfo.getExtras());
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onRouteChanged", zzl.class.getSimpleName());
        }
    }

    public final void onRouteRemoved(MediaRouter mediaRouter, RouteInfo routeInfo) {
        try {
            this.zzjb.zzc(routeInfo.getId(), routeInfo.getExtras());
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onRouteRemoved", zzl.class.getSimpleName());
        }
    }

    public final void onRouteSelected(MediaRouter mediaRouter, RouteInfo routeInfo) {
        try {
            this.zzjb.zzd(routeInfo.getId(), routeInfo.getExtras());
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onRouteSelected", zzl.class.getSimpleName());
        }
    }

    public final void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo, int i) {
        try {
            this.zzjb.zza(routeInfo.getId(), routeInfo.getExtras(), i);
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "onRouteUnselected", zzl.class.getSimpleName());
        }
    }
}
