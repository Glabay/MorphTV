package com.google.android.gms.cast;

import android.os.RemoteException;
import android.view.Display;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.internal.cast.zzdz;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzr extends zza {
    private final /* synthetic */ TaskCompletionSource zzbj;
    private final /* synthetic */ zzdz zzbk;
    private final /* synthetic */ zzq zzbl;

    zzr(zzq zzq, TaskCompletionSource taskCompletionSource, zzdz zzdz) {
        this.zzbl = zzq;
        this.zzbj = taskCompletionSource;
        this.zzbk = zzdz;
        super();
    }

    public final void onError(int i) throws RemoteException {
        this.zzbl.zzbi.zzbd.m25d("onError: %d", Integer.valueOf(i));
        this.zzbl.zzbi.a_();
        TaskUtil.setResultOrApiException(Status.RESULT_INTERNAL_ERROR, null, this.zzbj);
    }

    public final void zza(int r11, int r12, android.view.Surface r13) throws android.os.RemoteException {
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
        r0 = r10.zzbl;
        r0 = r0.zzbi;
        r0 = r0.zzbd;
        r1 = "onConnected";
        r2 = 0;
        r3 = new java.lang.Object[r2];
        r0.m25d(r1, r3);
        r0 = r10.zzbl;
        r0 = r0.zzbi;
        r0 = r0.getApplicationContext();
        r1 = "display";
        r0 = r0.getSystemService(r1);
        r3 = r0;
        r3 = (android.hardware.display.DisplayManager) r3;
        r0 = 0;
        if (r3 != 0) goto L_0x003b;
    L_0x0024:
        r11 = r10.zzbl;
        r11 = r11.zzbi;
        r11 = r11.zzbd;
        r12 = "Unable to get the display manager";
    L_0x002e:
        r13 = new java.lang.Object[r2];
        r11.m26e(r12, r13);
        r11 = com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR;
        r12 = r10.zzbj;
        com.google.android.gms.common.api.internal.TaskUtil.setResultOrApiException(r11, r0, r12);
        return;
    L_0x003b:
        r1 = r10.zzbl;
        r1 = r1.zzbi;
        r1.a_();
        r1 = r10.zzbl;
        r1 = r1.zzbi;
        r7 = com.google.android.gms.cast.CastRemoteDisplayClient.zza(r11, r12);
        r1 = r10.zzbl;
        r1 = r1.zzbi;
        r4 = "private_display";
        r9 = 2;
        r5 = r11;
        r6 = r12;
        r8 = r13;
        r11 = r3.createVirtualDisplay(r4, r5, r6, r7, r8, r9);
        r1.zzbe = r11;
        r11 = r10.zzbl;
        r11 = r11.zzbi;
        r11 = r11.zzbe;
        if (r11 != 0) goto L_0x0070;
    L_0x0065:
        r11 = r10.zzbl;
        r11 = r11.zzbi;
        r11 = r11.zzbd;
        r12 = "Unable to create virtual display";
        goto L_0x002e;
    L_0x0070:
        r11 = r10.zzbl;
        r11 = r11.zzbi;
        r11 = r11.zzbe;
        r11 = r11.getDisplay();
        if (r11 != 0) goto L_0x0089;
    L_0x007e:
        r11 = r10.zzbl;
        r11 = r11.zzbi;
        r11 = r11.zzbd;
        r12 = "Virtual display does not have a display";
        goto L_0x002e;
    L_0x0089:
        r12 = r10.zzbk;	 Catch:{ RemoteException -> 0x0099, RemoteException -> 0x0099 }
        r12 = r12.getService();	 Catch:{ RemoteException -> 0x0099, RemoteException -> 0x0099 }
        r12 = (com.google.android.gms.internal.cast.zzee) r12;	 Catch:{ RemoteException -> 0x0099, RemoteException -> 0x0099 }
        r11 = r11.getDisplayId();	 Catch:{ RemoteException -> 0x0099, RemoteException -> 0x0099 }
        r12.zza(r10, r11);	 Catch:{ RemoteException -> 0x0099, RemoteException -> 0x0099 }
        return;
    L_0x0099:
        r11 = r10.zzbl;
        r11 = r11.zzbi;
        r11 = r11.zzbd;
        r12 = "Unable to provision the route's new virtual Display";
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.zzr.zza(int, int, android.view.Surface):void");
    }

    public final void zzc() {
        this.zzbl.zzbi.zzbd.m25d("onConnectedWithDisplay", new Object[0]);
        if (this.zzbl.zzbi.zzbe == null) {
            this.zzbl.zzbi.zzbd.m26e("There is no virtual display", new Object[0]);
            TaskUtil.setResultOrApiException(Status.RESULT_INTERNAL_ERROR, null, this.zzbj);
            return;
        }
        Display display = this.zzbl.zzbi.zzbe.getDisplay();
        if (display != null) {
            TaskUtil.setResultOrApiException(Status.RESULT_SUCCESS, display, this.zzbj);
            return;
        }
        this.zzbl.zzbi.zzbd.m26e("Virtual display no longer has a display", new Object[0]);
        TaskUtil.setResultOrApiException(Status.RESULT_INTERNAL_ERROR, null, this.zzbj);
    }
}
