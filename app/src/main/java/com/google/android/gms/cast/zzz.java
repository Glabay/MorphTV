package com.google.android.gms.cast;

import android.view.Display;
import com.google.android.gms.tasks.OnCompleteListener;

final class zzz implements OnCompleteListener<Display> {
    private final /* synthetic */ CastRemoteDisplayLocalService zzcg;

    zzz(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
        this.zzcg = castRemoteDisplayLocalService;
    }

    public final void onComplete(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<android.view.Display> r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r5.isSuccessful();
        r1 = 0;
        if (r0 != 0) goto L_0x0018;
    L_0x0007:
        r5 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbd;
        r0 = "Connection was not successful";
        r1 = new java.lang.Object[r1];
        r5.m26e(r0, r1);
        r5 = r4.zzcg;
        r5.zzd();
        return;
    L_0x0018:
        r0 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbd;
        r2 = "startRemoteDisplay successful";
        r3 = new java.lang.Object[r1];
        r0.m25d(r2, r3);
        r0 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbo;
        monitor-enter(r0);
        r2 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzce;	 Catch:{ all -> 0x0098 }
        if (r2 != 0) goto L_0x0040;	 Catch:{ all -> 0x0098 }
    L_0x002e:
        r5 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbd;	 Catch:{ all -> 0x0098 }
        r2 = "Remote Display started but session already cancelled";	 Catch:{ all -> 0x0098 }
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x0098 }
        r5.m25d(r2, r1);	 Catch:{ all -> 0x0098 }
        r5 = r4.zzcg;	 Catch:{ all -> 0x0098 }
        r5.zzd();	 Catch:{ all -> 0x0098 }
        monitor-exit(r0);	 Catch:{ all -> 0x0098 }
        return;	 Catch:{ all -> 0x0098 }
    L_0x0040:
        monitor-exit(r0);	 Catch:{ all -> 0x0098 }
        r5 = r5.getResult();
        r5 = (android.view.Display) r5;
        if (r5 == 0) goto L_0x004f;
    L_0x0049:
        r0 = r4.zzcg;
        r0.zza(r5);
        goto L_0x005a;
    L_0x004f:
        r5 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbd;
        r0 = "Cast Remote Display session created without display";
        r2 = new java.lang.Object[r1];
        r5.m26e(r0, r2);
    L_0x005a:
        r5 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbp;
        r5.set(r1);
        r5 = r4.zzcg;
        r5 = r5.zzby;
        if (r5 == 0) goto L_0x0097;
    L_0x0069:
        r5 = r4.zzcg;
        r5 = r5.zzbz;
        if (r5 == 0) goto L_0x0097;
    L_0x0071:
        r5 = r4.zzcg;	 Catch:{ IllegalArgumentException -> 0x0081 }
        r5 = r5.zzby;	 Catch:{ IllegalArgumentException -> 0x0081 }
        r0 = r4.zzcg;	 Catch:{ IllegalArgumentException -> 0x0081 }
        r0 = r0.zzbz;	 Catch:{ IllegalArgumentException -> 0x0081 }
        r5.unbindService(r0);	 Catch:{ IllegalArgumentException -> 0x0081 }
        goto L_0x008c;
    L_0x0081:
        r5 = com.google.android.gms.cast.CastRemoteDisplayLocalService.zzbd;
        r0 = "No need to unbind service, already unbound";
        r1 = new java.lang.Object[r1];
        r5.m25d(r0, r1);
    L_0x008c:
        r5 = r4.zzcg;
        r0 = 0;
        r5.zzbz = null;
        r5 = r4.zzcg;
        r5.zzby = null;
    L_0x0097:
        return;
    L_0x0098:
        r5 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0098 }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzz.onComplete(com.google.android.gms.tasks.Task):void");
    }
}
