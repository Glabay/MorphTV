package com.google.android.gms.cast.framework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.app.MediaRouteDialogFactory;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.cast.zzdg;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class CastButtonFactory {
    private static final zzdg zzbd = new zzdg("CastButtonFactory");
    private static final List<WeakReference<MenuItem>> zzgp = new ArrayList();
    private static final List<WeakReference<MediaRouteButton>> zzgq = new ArrayList();

    private CastButtonFactory() {
    }

    public static MenuItem setUpMediaRouteButton(Context context, Menu menu, int i) {
        return zza(context, menu, i, null);
    }

    public static void setUpMediaRouteButton(Context context, MediaRouteButton mediaRouteButton) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (mediaRouteButton != null) {
            zza(context, mediaRouteButton, null);
            zzgq.add(new WeakReference(mediaRouteButton));
        }
    }

    private static android.view.MenuItem zza(android.content.Context r2, android.view.Menu r3, int r4, android.support.v7.app.MediaRouteDialogFactory r5) {
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
        r5 = "Must be called from the main thread.";
        com.google.android.gms.common.internal.Preconditions.checkMainThread(r5);
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r3);
        r3 = r3.findItem(r4);
        r5 = 0;
        r0 = 1;
        if (r3 != 0) goto L_0x0026;
    L_0x0010:
        r2 = new java.lang.IllegalArgumentException;
        r3 = java.util.Locale.ROOT;
        r1 = "menu doesn't contain a menu item whose ID is %d.";
        r0 = new java.lang.Object[r0];
        r4 = java.lang.Integer.valueOf(r4);
        r0[r5] = r4;
        r3 = java.lang.String.format(r3, r1, r0);
        r2.<init>(r3);
        throw r2;
    L_0x0026:
        r1 = 0;
        zza(r2, r3, r1);	 Catch:{ IllegalArgumentException -> 0x0035 }
        r2 = zzgp;	 Catch:{ IllegalArgumentException -> 0x0035 }
        r1 = new java.lang.ref.WeakReference;	 Catch:{ IllegalArgumentException -> 0x0035 }
        r1.<init>(r3);	 Catch:{ IllegalArgumentException -> 0x0035 }
        r2.add(r1);	 Catch:{ IllegalArgumentException -> 0x0035 }
        return r3;
    L_0x0035:
        r2 = new java.lang.IllegalArgumentException;
        r3 = java.util.Locale.ROOT;
        r1 = "menu item with ID %d doesn't have a MediaRouteActionProvider.";
        r0 = new java.lang.Object[r0];
        r4 = java.lang.Integer.valueOf(r4);
        r0[r5] = r4;
        r3 = java.lang.String.format(r3, r1, r0);
        r2.<init>(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.CastButtonFactory.zza(android.content.Context, android.view.Menu, int, android.support.v7.app.MediaRouteDialogFactory):android.view.MenuItem");
    }

    public static void zza(Context context) {
        for (WeakReference weakReference : zzgp) {
            try {
                if (weakReference.get() != null) {
                    zza(context, (MenuItem) weakReference.get(), null);
                }
            } catch (IllegalArgumentException e) {
                zzbd.m28w("Unexpected exception when refreshing MediaRouteSelectors for Cast buttons", e);
            }
        }
        for (WeakReference weakReference2 : zzgq) {
            if (weakReference2.get() != null) {
                zza(context, (MediaRouteButton) weakReference2.get(), null);
            }
        }
    }

    private static void zza(Context context, @NonNull MediaRouteButton mediaRouteButton, MediaRouteDialogFactory mediaRouteDialogFactory) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        CastContext zzb = CastContext.zzb(context);
        if (zzb != null) {
            mediaRouteButton.setRouteSelector(zzb.getMergedSelector());
        }
    }

    private static void zza(Context context, @NonNull MenuItem menuItem, MediaRouteDialogFactory mediaRouteDialogFactory) throws IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mediaRouteActionProvider == null) {
            throw new IllegalArgumentException();
        }
        CastContext zzb = CastContext.zzb(context);
        if (zzb != null) {
            mediaRouteActionProvider.setRouteSelector(zzb.getMergedSelector());
        }
    }
}
