package com.google.android.gms.internal.cast;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastRemoteDisplay.CastRemoteDisplaySessionCallbacks;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;

@Deprecated
public final class zzea extends GmsClient<zzee> implements DeathRecipient {
    private static final zzdg zzbd = new zzdg("CastRemoteDisplayClientImpl");
    private CastDevice zzai;
    private CastRemoteDisplaySessionCallbacks zzxy;
    private Bundle zzxz;

    public zzea(Context context, Looper looper, ClientSettings clientSettings, CastDevice castDevice, Bundle bundle, CastRemoteDisplaySessionCallbacks castRemoteDisplaySessionCallbacks, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 83, clientSettings, connectionCallbacks, onConnectionFailedListener);
        zzbd.m25d("instance created", new Object[0]);
        this.zzxy = castRemoteDisplaySessionCallbacks;
        this.zzai = castDevice;
        this.zzxz = bundle;
    }

    public final void binderDied() {
    }

    public final /* synthetic */ IInterface createServiceInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.remote_display.ICastRemoteDisplayService");
        return queryLocalInterface instanceof zzee ? (zzee) queryLocalInterface : new zzef(iBinder);
    }

    public final void disconnect() {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = zzbd;
        r1 = "disconnect";
        r2 = 0;
        r2 = new java.lang.Object[r2];
        r0.m25d(r1, r2);
        r0 = 0;
        r3.zzxy = r0;
        r3.zzai = r0;
        r0 = r3.getService();	 Catch:{ RemoteException -> 0x0021, RemoteException -> 0x0021, all -> 0x001c }
        r0 = (com.google.android.gms.internal.cast.zzee) r0;	 Catch:{ RemoteException -> 0x0021, RemoteException -> 0x0021, all -> 0x001c }
        r0.disconnect();	 Catch:{ RemoteException -> 0x0021, RemoteException -> 0x0021, all -> 0x001c }
        super.disconnect();
        return;
    L_0x001c:
        r0 = move-exception;
        super.disconnect();
        throw r0;
    L_0x0021:
        super.disconnect();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzea.disconnect():void");
    }

    public final int getMinApkVersion() {
        return GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }

    protected final String getServiceDescriptor() {
        return "com.google.android.gms.cast.remote_display.ICastRemoteDisplayService";
    }

    protected final String getStartServiceAction() {
        return "com.google.android.gms.cast.remote_display.service.START";
    }

    public final void zza(zzec zzec) throws RemoteException {
        zzbd.m25d("stopRemoteDisplay", new Object[0]);
        ((zzee) getService()).zza(zzec);
    }

    public final void zza(zzec zzec, zzeg zzeg, String str) throws RemoteException {
        zzbd.m25d("startRemoteDisplay", new Object[0]);
        zzec zzec2 = zzec;
        ((zzee) getService()).zza(zzec2, new zzeb(this, zzeg), this.zzai.getDeviceId(), str, this.zzxz);
    }
}
