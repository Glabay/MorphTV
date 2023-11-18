package com.google.android.gms.internal.cast;

import android.annotation.TargetApi;
import android.os.RemoteException;
import android.view.Display;
import com.google.android.gms.common.api.Status;

@TargetApi(19)
public final class zzdw extends zzdu {
    private final zzea zzxw;
    private final /* synthetic */ zzdv zzxx;

    public zzdw(zzdv zzdv, zzea zzea) {
        this.zzxx = zzdv;
        this.zzxw = zzea;
    }

    public final void onError(int i) throws RemoteException {
        zzdq.zzbd.m25d("onError: %d", Integer.valueOf(i));
        this.zzxx.zzxu.a_();
        this.zzxx.setResult(new zzdy(Status.RESULT_INTERNAL_ERROR));
    }

    public final void zza(int r11, int r12, android.view.Surface r13) {
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
        r10 = this;
        r0 = com.google.android.gms.internal.cast.zzdq.zzbd;
        r1 = "onConnected";
        r2 = 0;
        r3 = new java.lang.Object[r2];
        r0.m25d(r1, r3);
        r0 = r10.zzxw;
        r0 = r0.getContext();
        r1 = "display";
        r0 = r0.getSystemService(r1);
        r3 = r0;
        r3 = (android.hardware.display.DisplayManager) r3;
        if (r3 != 0) goto L_0x0035;
    L_0x001d:
        r11 = com.google.android.gms.internal.cast.zzdq.zzbd;
        r12 = "Unable to get the display manager";
        r13 = new java.lang.Object[r2];
        r11.m26e(r12, r13);
        r11 = r10.zzxx;
        r12 = new com.google.android.gms.internal.cast.zzdy;
        r13 = com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR;
        r12.<init>(r13);
    L_0x0031:
        r11.setResult(r12);
        return;
    L_0x0035:
        r0 = r10.zzxx;
        r0 = r0.zzxu;
        r0.a_();
        if (r11 >= r12) goto L_0x0040;
    L_0x003e:
        r0 = r11;
        goto L_0x0041;
    L_0x0040:
        r0 = r12;
    L_0x0041:
        r0 = r0 * 320;
        r7 = r0 / 1080;
        r0 = r10.zzxx;
        r0 = r0.zzxu;
        r4 = "private_display";
        r9 = 2;
        r5 = r11;
        r6 = r12;
        r8 = r13;
        r11 = r3.createVirtualDisplay(r4, r5, r6, r7, r8, r9);
        r0.zzbe = r11;
        r11 = r10.zzxx;
        r11 = r11.zzxu;
        r11 = r11.zzbe;
        if (r11 != 0) goto L_0x0075;
    L_0x0060:
        r11 = com.google.android.gms.internal.cast.zzdq.zzbd;
        r12 = "Unable to create virtual display";
        r13 = new java.lang.Object[r2];
        r11.m26e(r12, r13);
        r11 = r10.zzxx;
        r12 = new com.google.android.gms.internal.cast.zzdy;
        r13 = com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR;
        r12.<init>(r13);
        goto L_0x0031;
    L_0x0075:
        r11 = r10.zzxx;
        r11 = r11.zzxu;
        r11 = r11.zzbe;
        r11 = r11.getDisplay();
        if (r11 != 0) goto L_0x0098;
    L_0x0083:
        r11 = com.google.android.gms.internal.cast.zzdq.zzbd;
        r12 = "Virtual display does not have a display";
        r13 = new java.lang.Object[r2];
        r11.m26e(r12, r13);
        r11 = r10.zzxx;
        r12 = new com.google.android.gms.internal.cast.zzdy;
        r13 = com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR;
        r12.<init>(r13);
        goto L_0x0031;
    L_0x0098:
        r11 = r10.zzxw;	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r12 = r10.zzxx;	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r12 = r12.zzxu;	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r12 = r12.zzbe;	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r12 = r12.getDisplay();	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r12 = r12.getDisplayId();	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r11 = r11.getService();	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r11 = (com.google.android.gms.internal.cast.zzee) r11;	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        r11.zza(r10, r12);	 Catch:{ RemoteException -> 0x00b4, RemoteException -> 0x00b4 }
        return;
    L_0x00b4:
        r11 = com.google.android.gms.internal.cast.zzdq.zzbd;
        r12 = "Unable to provision the route's new virtual Display";
        r13 = new java.lang.Object[r2];
        r11.m26e(r12, r13);
        r11 = r10.zzxx;
        r12 = new com.google.android.gms.internal.cast.zzdy;
        r13 = com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR;
        r12.<init>(r13);
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdw.zza(int, int, android.view.Surface):void");
    }

    public final void zzc() {
        zzdq.zzbd.m25d("onConnectedWithDisplay", new Object[0]);
        if (this.zzxx.zzxu.zzbe == null) {
            zzdq.zzbd.m26e("There is no virtual display", new Object[0]);
            this.zzxx.setResult(new zzdy(Status.RESULT_INTERNAL_ERROR));
            return;
        }
        Display display = this.zzxx.zzxu.zzbe.getDisplay();
        if (display != null) {
            this.zzxx.setResult(new zzdy(display));
            return;
        }
        zzdq.zzbd.m26e("Virtual display no longer has a display", new Object[0]);
        this.zzxx.setResult(new zzdy(Status.RESULT_INTERNAL_ERROR));
    }
}
