package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zzw implements zzbp {
    private final Looper zzcn;
    private final GoogleApiManager zzcq;
    private final Lock zzga;
    private final ClientSettings zzgf;
    private final Map<AnyClientKey<?>, zzv<?>> zzgg = new HashMap();
    private final Map<AnyClientKey<?>, zzv<?>> zzgh = new HashMap();
    private final Map<Api<?>, Boolean> zzgi;
    private final zzav zzgj;
    private final GoogleApiAvailabilityLight zzgk;
    private final Condition zzgl;
    private final boolean zzgm;
    private final boolean zzgn;
    private final Queue<ApiMethodImpl<?, ?>> zzgo = new LinkedList();
    @GuardedBy("mLock")
    private boolean zzgp;
    @GuardedBy("mLock")
    private Map<zzh<?>, ConnectionResult> zzgq;
    @GuardedBy("mLock")
    private Map<zzh<?>, ConnectionResult> zzgr;
    @GuardedBy("mLock")
    private zzz zzgs;
    @GuardedBy("mLock")
    private ConnectionResult zzgt;

    public zzw(Context context, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, ArrayList<zzp> arrayList, zzav zzav, boolean z) {
        this.zzga = lock;
        Looper looper2 = looper;
        this.zzcn = looper2;
        this.zzgl = lock.newCondition();
        this.zzgk = googleApiAvailabilityLight;
        this.zzgj = zzav;
        this.zzgi = map2;
        ClientSettings clientSettings2 = clientSettings;
        this.zzgf = clientSettings2;
        this.zzgm = z;
        Map hashMap = new HashMap();
        for (Api api : map2.keySet()) {
            hashMap.put(api.getClientKey(), api);
        }
        Map hashMap2 = new HashMap();
        ArrayList arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            zzp zzp = (zzp) obj;
            hashMap2.put(zzp.mApi, zzp);
        }
        Object obj2 = null;
        Object obj3 = 1;
        Object obj4 = null;
        for (Entry entry : map.entrySet()) {
            Object obj5;
            Object obj6;
            Object obj7;
            Api api2 = (Api) hashMap.get(entry.getKey());
            Client client = (Client) entry.getValue();
            if (client.requiresGooglePlayServices()) {
                if (((Boolean) r0.zzgi.get(api2)).booleanValue()) {
                    obj5 = obj3;
                    obj6 = obj4;
                } else {
                    obj5 = obj3;
                    obj6 = 1;
                }
                obj7 = 1;
            } else {
                obj7 = obj2;
                obj6 = obj4;
                obj5 = null;
            }
            zzv zzv = r1;
            Client client2 = client;
            Entry entry2 = entry;
            zzv zzv2 = new zzv(context, api2, looper2, client, (zzp) hashMap2.get(api2), clientSettings2, abstractClientBuilder);
            r0.zzgg.put((AnyClientKey) entry2.getKey(), zzv);
            if (client2.requiresSignIn()) {
                r0.zzgh.put((AnyClientKey) entry2.getKey(), zzv);
            }
            obj4 = obj6;
            obj3 = obj5;
            obj2 = obj7;
            looper2 = looper;
        }
        boolean z2 = obj2 != null && obj3 == null && obj4 == null;
        r0.zzgn = z2;
        r0.zzcq = GoogleApiManager.zzbf();
    }

    @Nullable
    private final ConnectionResult zza(@NonNull AnyClientKey<?> anyClientKey) {
        this.zzga.lock();
        try {
            zzv zzv = (zzv) this.zzgg.get(anyClientKey);
            if (this.zzgq == null || zzv == null) {
                this.zzga.unlock();
                return null;
            }
            ConnectionResult connectionResult = (ConnectionResult) this.zzgq.get(zzv.zzm());
            return connectionResult;
        } finally {
            this.zzga.unlock();
        }
    }

    private final boolean zza(zzv<?> zzv, ConnectionResult connectionResult) {
        return !connectionResult.isSuccess() && !connectionResult.hasResolution() && ((Boolean) this.zzgi.get(zzv.getApi())).booleanValue() && zzv.zzae().requiresGooglePlayServices() && this.zzgk.isUserResolvableError(connectionResult.getErrorCode());
    }

    private final boolean zzaf() {
        this.zzga.lock();
        try {
            if (this.zzgp) {
                if (this.zzgm) {
                    for (AnyClientKey zza : this.zzgh.keySet()) {
                        ConnectionResult zza2 = zza(zza);
                        if (zza2 != null) {
                            if (!zza2.isSuccess()) {
                            }
                        }
                    }
                    this.zzga.unlock();
                    return true;
                }
            }
            this.zzga.unlock();
            return false;
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    @GuardedBy("mLock")
    private final void zzag() {
        if (this.zzgf == null) {
            this.zzgj.zzim = Collections.emptySet();
            return;
        }
        Set hashSet = new HashSet(this.zzgf.getRequiredScopes());
        Map optionalApiSettings = this.zzgf.getOptionalApiSettings();
        for (Api api : optionalApiSettings.keySet()) {
            ConnectionResult connectionResult = getConnectionResult(api);
            if (connectionResult != null && connectionResult.isSuccess()) {
                hashSet.addAll(((OptionalApiSettings) optionalApiSettings.get(api)).mScopes);
            }
        }
        this.zzgj.zzim = hashSet;
    }

    @GuardedBy("mLock")
    private final void zzah() {
        while (!this.zzgo.isEmpty()) {
            execute((ApiMethodImpl) this.zzgo.remove());
        }
        this.zzgj.zzb(null);
    }

    @Nullable
    @GuardedBy("mLock")
    private final ConnectionResult zzai() {
        ConnectionResult connectionResult = null;
        ConnectionResult connectionResult2 = null;
        int i = 0;
        int i2 = 0;
        for (zzv zzv : this.zzgg.values()) {
            Api api = zzv.getApi();
            ConnectionResult connectionResult3 = (ConnectionResult) this.zzgq.get(zzv.zzm());
            if (!connectionResult3.isSuccess() && (!((Boolean) this.zzgi.get(api)).booleanValue() || connectionResult3.hasResolution() || this.zzgk.isUserResolvableError(connectionResult3.getErrorCode()))) {
                int priority;
                if (connectionResult3.getErrorCode() == 4 && this.zzgm) {
                    priority = api.zzj().getPriority();
                    if (connectionResult2 == null || i2 > priority) {
                        connectionResult2 = connectionResult3;
                        i2 = priority;
                    }
                } else {
                    priority = api.zzj().getPriority();
                    if (connectionResult == null || i > priority) {
                        connectionResult = connectionResult3;
                        i = priority;
                    }
                }
            }
        }
        return (connectionResult == null || connectionResult2 == null || i <= i2) ? connectionResult : connectionResult2;
    }

    private final <T extends ApiMethodImpl<? extends Result, ? extends AnyClient>> boolean zzb(@NonNull T t) {
        AnyClientKey clientKey = t.getClientKey();
        ConnectionResult zza = zza(clientKey);
        if (zza == null || zza.getErrorCode() != 4) {
            return false;
        }
        t.setFailedResult(new Status(4, null, this.zzcq.zza(((zzv) this.zzgg.get(clientKey)).zzm(), System.identityHashCode(this.zzgj))));
        return true;
    }

    @javax.annotation.concurrent.GuardedBy("mLock")
    public final com.google.android.gms.common.ConnectionResult blockingConnect() {
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
        r3 = this;
        r3.connect();
    L_0x0003:
        r0 = r3.isConnecting();
        r1 = 0;
        if (r0 == 0) goto L_0x001f;
    L_0x000a:
        r0 = r3.zzgl;	 Catch:{ InterruptedException -> 0x0010 }
        r0.await();	 Catch:{ InterruptedException -> 0x0010 }
        goto L_0x0003;
    L_0x0010:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
        r0 = new com.google.android.gms.common.ConnectionResult;
        r2 = 15;
        r0.<init>(r2, r1);
        return r0;
    L_0x001f:
        r0 = r3.isConnected();
        if (r0 == 0) goto L_0x0028;
    L_0x0025:
        r0 = com.google.android.gms.common.ConnectionResult.RESULT_SUCCESS;
        return r0;
    L_0x0028:
        r0 = r3.zzgt;
        if (r0 == 0) goto L_0x002f;
    L_0x002c:
        r0 = r3.zzgt;
        return r0;
    L_0x002f:
        r0 = new com.google.android.gms.common.ConnectionResult;
        r2 = 13;
        r0.<init>(r2, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzw.blockingConnect():com.google.android.gms.common.ConnectionResult");
    }

    @javax.annotation.concurrent.GuardedBy("mLock")
    public final com.google.android.gms.common.ConnectionResult blockingConnect(long r4, java.util.concurrent.TimeUnit r6) {
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
        r3 = this;
        r3.connect();
        r4 = r6.toNanos(r4);
    L_0x0007:
        r6 = r3.isConnecting();
        r0 = 0;
        if (r6 == 0) goto L_0x0035;
    L_0x000e:
        r1 = 0;
        r6 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r6 > 0) goto L_0x001f;
    L_0x0014:
        r3.disconnect();	 Catch:{ InterruptedException -> 0x0026 }
        r4 = new com.google.android.gms.common.ConnectionResult;	 Catch:{ InterruptedException -> 0x0026 }
        r5 = 14;	 Catch:{ InterruptedException -> 0x0026 }
        r4.<init>(r5, r0);	 Catch:{ InterruptedException -> 0x0026 }
        return r4;	 Catch:{ InterruptedException -> 0x0026 }
    L_0x001f:
        r6 = r3.zzgl;	 Catch:{ InterruptedException -> 0x0026 }
        r4 = r6.awaitNanos(r4);	 Catch:{ InterruptedException -> 0x0026 }
        goto L_0x0007;
    L_0x0026:
        r4 = java.lang.Thread.currentThread();
        r4.interrupt();
        r4 = new com.google.android.gms.common.ConnectionResult;
        r5 = 15;
        r4.<init>(r5, r0);
        return r4;
    L_0x0035:
        r4 = r3.isConnected();
        if (r4 == 0) goto L_0x003e;
    L_0x003b:
        r4 = com.google.android.gms.common.ConnectionResult.RESULT_SUCCESS;
        return r4;
    L_0x003e:
        r4 = r3.zzgt;
        if (r4 == 0) goto L_0x0045;
    L_0x0042:
        r4 = r3.zzgt;
        return r4;
    L_0x0045:
        r4 = new com.google.android.gms.common.ConnectionResult;
        r5 = 13;
        r4.<init>(r5, r0);
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzw.blockingConnect(long, java.util.concurrent.TimeUnit):com.google.android.gms.common.ConnectionResult");
    }

    public final void connect() {
        this.zzga.lock();
        try {
            if (!this.zzgp) {
                this.zzgp = true;
                this.zzgq = null;
                this.zzgr = null;
                this.zzgs = null;
                this.zzgt = null;
                this.zzcq.zzr();
                this.zzcq.zza(this.zzgg.values()).addOnCompleteListener(new HandlerExecutor(this.zzcn), new zzy());
            }
            this.zzga.unlock();
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final void disconnect() {
        this.zzga.lock();
        try {
            this.zzgp = false;
            this.zzgq = null;
            this.zzgr = null;
            if (this.zzgs != null) {
                this.zzgs.cancel();
                this.zzgs = null;
            }
            this.zzgt = null;
            while (!this.zzgo.isEmpty()) {
                ApiMethodImpl apiMethodImpl = (ApiMethodImpl) this.zzgo.remove();
                apiMethodImpl.zza(null);
                apiMethodImpl.cancel();
            }
            this.zzgl.signalAll();
        } finally {
            this.zzga.unlock();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        if (this.zzgm && zzb((ApiMethodImpl) t)) {
            return t;
        }
        if (isConnected()) {
            this.zzgj.zzir.zzb(t);
            return ((zzv) this.zzgg.get(t.getClientKey())).doRead((ApiMethodImpl) t);
        }
        this.zzgo.add(t);
        return t;
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        AnyClientKey clientKey = t.getClientKey();
        if (this.zzgm && zzb((ApiMethodImpl) t)) {
            return t;
        }
        this.zzgj.zzir.zzb(t);
        return ((zzv) this.zzgg.get(clientKey)).doWrite((ApiMethodImpl) t);
    }

    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return zza(api.getClientKey());
    }

    public final boolean isConnected() {
        this.zzga.lock();
        try {
            boolean z = this.zzgq != null && this.zzgt == null;
            this.zzga.unlock();
            return z;
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final boolean isConnecting() {
        this.zzga.lock();
        try {
            boolean z = this.zzgq == null && this.zzgp;
            this.zzga.unlock();
            return z;
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        this.zzga.lock();
        try {
            if (!this.zzgp || zzaf()) {
                this.zzga.unlock();
                return false;
            }
            this.zzcq.zzr();
            this.zzgs = new zzz(this, signInConnectionListener);
            this.zzcq.zza(this.zzgh.values()).addOnCompleteListener(new HandlerExecutor(this.zzcn), this.zzgs);
            return true;
        } finally {
            this.zzga.unlock();
        }
    }

    public final void maybeSignOut() {
        this.zzga.lock();
        try {
            this.zzcq.maybeSignOut();
            if (this.zzgs != null) {
                this.zzgs.cancel();
                this.zzgs = null;
            }
            if (this.zzgr == null) {
                this.zzgr = new ArrayMap(this.zzgh.size());
            }
            ConnectionResult connectionResult = new ConnectionResult(4);
            for (zzv zzm : this.zzgh.values()) {
                this.zzgr.put(zzm.zzm(), connectionResult);
            }
            if (this.zzgq != null) {
                this.zzgq.putAll(this.zzgr);
            }
            this.zzga.unlock();
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final void zzz() {
    }
}
