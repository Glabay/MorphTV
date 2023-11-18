package com.google.android.gms.cast;

import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.RouteInfo;

final class zzu extends Callback {
    private final /* synthetic */ CastRemoteDisplayLocalService zzcg;

    zzu(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
        this.zzcg = castRemoteDisplayLocalService;
    }

    public final void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo) {
        CastRemoteDisplayLocalService castRemoteDisplayLocalService;
        String str;
        this.zzcg.zzb("onRouteUnselected");
        if (this.zzcg.zzbw == null) {
            castRemoteDisplayLocalService = this.zzcg;
            str = "onRouteUnselected, no device was selected";
        } else if (CastDevice.getFromBundle(routeInfo.getExtras()).getDeviceId().equals(this.zzcg.zzbw.getDeviceId())) {
            CastRemoteDisplayLocalService.stopService();
            return;
        } else {
            castRemoteDisplayLocalService = this.zzcg;
            str = "onRouteUnselected, device does not match";
        }
        castRemoteDisplayLocalService.zzb(str);
    }
}
