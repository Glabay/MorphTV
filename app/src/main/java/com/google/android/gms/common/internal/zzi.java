package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.HashSet;
import java.util.Set;

final class zzi implements ServiceConnection {
    private ComponentName mComponentName;
    private int mState = 2;
    private IBinder zzry;
    private final Set<ServiceConnection> zztv = new HashSet();
    private boolean zztw;
    private final ConnectionStatusConfig zztx;
    private final /* synthetic */ zzh zzty;

    public zzi(zzh zzh, ConnectionStatusConfig connectionStatusConfig) {
        this.zzty = zzh;
        this.zztx = connectionStatusConfig;
    }

    public final IBinder getBinder() {
        return this.zzry;
    }

    public final ComponentName getComponentName() {
        return this.mComponentName;
    }

    public final int getState() {
        return this.mState;
    }

    public final boolean isBound() {
        return this.zztw;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.zzty.zztr) {
            this.zzty.mHandler.removeMessages(1, this.zztx);
            this.zzry = iBinder;
            this.mComponentName = componentName;
            for (ServiceConnection onServiceConnected : this.zztv) {
                onServiceConnected.onServiceConnected(componentName, iBinder);
            }
            this.mState = 1;
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (this.zzty.zztr) {
            this.zzty.mHandler.removeMessages(1, this.zztx);
            this.zzry = null;
            this.mComponentName = componentName;
            for (ServiceConnection onServiceDisconnected : this.zztv) {
                onServiceDisconnected.onServiceDisconnected(componentName);
            }
            this.mState = 2;
        }
    }

    public final void zza(ServiceConnection serviceConnection, String str) {
        this.zzty.zzts.logConnectService(this.zzty.zzau, serviceConnection, str, this.zztx.getStartServiceIntent(this.zzty.zzau));
        this.zztv.add(serviceConnection);
    }

    public final boolean zza(ServiceConnection serviceConnection) {
        return this.zztv.contains(serviceConnection);
    }

    public final void zzb(ServiceConnection serviceConnection, String str) {
        this.zzty.zzts.logDisconnectService(this.zzty.zzau, serviceConnection);
        this.zztv.remove(serviceConnection);
    }

    public final boolean zzcv() {
        return this.zztv.isEmpty();
    }

    public final void zzj(java.lang.String r8) {
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
        r7 = this;
        r0 = 3;
        r7.mState = r0;
        r0 = r7.zzty;
        r1 = r0.zzts;
        r0 = r7.zzty;
        r2 = r0.zzau;
        r0 = r7.zztx;
        r3 = r7.zzty;
        r3 = r3.zzau;
        r4 = r0.getStartServiceIntent(r3);
        r0 = r7.zztx;
        r6 = r0.getBindFlags();
        r3 = r8;
        r5 = r7;
        r8 = r1.bindService(r2, r3, r4, r5, r6);
        r7.zztw = r8;
        r8 = r7.zztw;
        if (r8 == 0) goto L_0x004a;
    L_0x002d:
        r8 = r7.zzty;
        r8 = r8.mHandler;
        r0 = 1;
        r1 = r7.zztx;
        r8 = r8.obtainMessage(r0, r1);
        r0 = r7.zzty;
        r0 = r0.mHandler;
        r1 = r7.zzty;
        r1 = r1.zztu;
        r0.sendMessageDelayed(r8, r1);
        return;
    L_0x004a:
        r8 = 2;
        r7.mState = r8;
        r8 = r7.zzty;	 Catch:{ IllegalArgumentException -> 0x005c }
        r8 = r8.zzts;	 Catch:{ IllegalArgumentException -> 0x005c }
        r0 = r7.zzty;	 Catch:{ IllegalArgumentException -> 0x005c }
        r0 = r0.zzau;	 Catch:{ IllegalArgumentException -> 0x005c }
        r8.unbindService(r0, r7);	 Catch:{ IllegalArgumentException -> 0x005c }
    L_0x005c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzi.zzj(java.lang.String):void");
    }

    public final void zzk(String str) {
        this.zzty.mHandler.removeMessages(1, this.zztx);
        this.zzty.zzts.unbindService(this.zzty.zzau, this);
        this.zztw = false;
        this.mState = 2;
    }
}
