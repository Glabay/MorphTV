package com.google.android.gms.cast.framework;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.cast.zzch;
import com.google.android.gms.internal.cast.zzdg;
import com.google.android.gms.internal.cast.zze;
import com.google.android.gms.internal.cast.zzf;
import com.google.android.gms.internal.cast.zzw;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CastContext {
    public static final String OPTIONS_PROVIDER_CLASS_NAME_KEY = "com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME";
    private static final zzdg zzbd = new zzdg("CastContext");
    private static CastContext zzgr;
    private final Context zzgs;
    private final zzj zzgt;
    private final SessionManager zzgu;
    private final zze zzgv;
    private final PrecacheManager zzgw;
    private final MediaNotificationManager zzgx;
    private final CastOptions zzgy;
    private zzw zzgz = new zzw(MediaRouter.getInstance(this.zzgs));
    private zzf zzha;
    private final List<SessionProvider> zzhb;

    private CastContext(Context context, CastOptions castOptions, List<SessionProvider> list) {
        zzp zzw;
        zzv zzv;
        this.zzgs = context.getApplicationContext();
        this.zzgy = castOptions;
        this.zzhb = list;
        zzp();
        this.zzgt = zze.zza(this.zzgs, castOptions, this.zzgz, zzo());
        PrecacheManager precacheManager = null;
        try {
            zzw = this.zzgt.zzw();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getDiscoveryManagerImpl", zzj.class.getSimpleName());
            zzw = null;
        }
        this.zzgv = zzw == null ? null : new zze(zzw);
        try {
            zzv = this.zzgt.zzv();
        } catch (Throwable e2) {
            zzbd.zza(e2, "Unable to call %s on %s.", "getSessionManagerImpl", zzj.class.getSimpleName());
            zzv = null;
        }
        this.zzgu = zzv == null ? null : new SessionManager(zzv, this.zzgs);
        this.zzgx = new MediaNotificationManager(this.zzgu);
        if (this.zzgu != null) {
            precacheManager = new PrecacheManager(this.zzgy, this.zzgu, new zzch(this.zzgs));
        }
        this.zzgw = precacheManager;
    }

    @Nullable
    public static CastContext getSharedInstance() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return zzgr;
    }

    public static CastContext getSharedInstance(@NonNull Context context) throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (zzgr == null) {
            OptionsProvider zzc = zzc(context.getApplicationContext());
            zzgr = new CastContext(context, zzc.getCastOptions(context.getApplicationContext()), zzc.getAdditionalSessionProviders(context.getApplicationContext()));
        }
        return zzgr;
    }

    private static boolean zza(CastSession castSession, double d, boolean z) {
        if (z) {
            try {
                double volume = castSession.getVolume() + d;
                d = 1.0d;
                if (volume <= 1.0d) {
                    d = volume;
                }
                castSession.setVolume(d);
                return true;
            } catch (IOException e) {
                zzbd.m26e("Unable to call CastSession.setVolume(double).", e);
            }
        }
        return true;
    }

    @Nullable
    public static CastContext zzb(@NonNull Context context) throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return getSharedInstance(context);
        } catch (RuntimeException e) {
            zzbd.m26e("Failed to load module from Google Play services. Cast will not work properly. Might due to outdated Google Play services. Ignoring this failure silently.", e);
            return null;
        }
    }

    private static OptionsProvider zzc(Context context) throws IllegalStateException {
        try {
            Bundle bundle = Wrappers.packageManager(context).getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle == null) {
                zzbd.m26e("Bundle is null", new Object[0]);
            }
            String string = bundle.getString(OPTIONS_PROVIDER_CLASS_NAME_KEY);
            if (string != null) {
                return (OptionsProvider) Class.forName(string).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            throw new IllegalStateException("The fully qualified name of the implementation of OptionsProvider must be provided as a metadata in the AndroidManifest.xml with key com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME.");
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to initialize CastContext.", e);
        }
    }

    private final Map<String, IBinder> zzo() {
        Map<String, IBinder> hashMap = new HashMap();
        if (this.zzha != null) {
            hashMap.put(this.zzha.getCategory(), this.zzha.zzai());
        }
        if (this.zzhb != null) {
            for (SessionProvider sessionProvider : this.zzhb) {
                Preconditions.checkNotNull(sessionProvider, "Additional SessionProvider must not be null.");
                String checkNotEmpty = Preconditions.checkNotEmpty(sessionProvider.getCategory(), "Category for SessionProvider must not be null or empty string.");
                Preconditions.checkArgument(hashMap.containsKey(checkNotEmpty) ^ true, String.format("SessionProvider for category %s already added", new Object[]{checkNotEmpty}));
                hashMap.put(checkNotEmpty, sessionProvider.zzai());
            }
        }
        return hashMap;
    }

    private final void zzp() {
        this.zzha = !TextUtils.isEmpty(this.zzgy.getReceiverApplicationId()) ? new zzf(this.zzgs, this.zzgy, this.zzgz) : null;
    }

    @Deprecated
    public void addAppVisibilityListener(AppVisibilityListener appVisibilityListener) throws IllegalStateException, NullPointerException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        Preconditions.checkNotNull(appVisibilityListener);
        try {
            this.zzgt.zza(new zza(appVisibilityListener));
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "addVisibilityChangeListener", zzj.class.getSimpleName());
        }
    }

    public void addCastStateListener(CastStateListener castStateListener) throws IllegalStateException, NullPointerException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        Preconditions.checkNotNull(castStateListener);
        this.zzgu.addCastStateListener(castStateListener);
    }

    public CastOptions getCastOptions() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzgy;
    }

    public int getCastState() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzgu.getCastState();
    }

    public MediaNotificationManager getMediaNotificationManager() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzgx;
    }

    public MediaRouteSelector getMergedSelector() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return MediaRouteSelector.fromBundle(this.zzgt.zzu());
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getMergedSelectorAsBundle", zzj.class.getSimpleName());
            return null;
        }
    }

    public PrecacheManager getPrecacheManager() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzgw;
    }

    public SessionManager getSessionManager() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzgu;
    }

    public boolean isAppVisible() throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        try {
            return this.zzgt.isAppVisible();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "isApplicationVisible", zzj.class.getSimpleName());
            return false;
        }
    }

    public boolean onDispatchVolumeKeyEventBeforeJellyBean(KeyEvent keyEvent) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (PlatformVersion.isAtLeastJellyBean()) {
            return false;
        }
        Session currentCastSession = this.zzgu.getCurrentCastSession();
        if (currentCastSession == null || !currentCastSession.isConnected()) {
            return false;
        }
        double volumeDeltaBeforeIceCreamSandwich = getCastOptions().getVolumeDeltaBeforeIceCreamSandwich();
        boolean z = keyEvent.getAction() == 0;
        switch (keyEvent.getKeyCode()) {
            case 24:
                zza(currentCastSession, volumeDeltaBeforeIceCreamSandwich, z);
                return true;
            case 25:
                zza(currentCastSession, -volumeDeltaBeforeIceCreamSandwich, z);
                return true;
            default:
                return false;
        }
    }

    @Deprecated
    public void removeAppVisibilityListener(AppVisibilityListener appVisibilityListener) throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (appVisibilityListener != null) {
            try {
                this.zzgt.zzb(new zza(appVisibilityListener));
            } catch (Throwable e) {
                zzbd.zza(e, "Unable to call %s on %s.", "addVisibilityChangeListener", zzj.class.getSimpleName());
            }
        }
    }

    public void removeCastStateListener(CastStateListener castStateListener) throws IllegalStateException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (castStateListener != null) {
            this.zzgu.removeCastStateListener(castStateListener);
        }
    }

    public void setReceiverApplicationId(String str) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (!TextUtils.equals(str, this.zzgy.getReceiverApplicationId())) {
            this.zzgy.setReceiverApplicationId(str);
            zzp();
            try {
                this.zzgt.zza(str, zzo());
            } catch (Throwable e) {
                zzbd.zza(e, "Unable to call %s on %s.", "setReceiverApplicationId", zzj.class.getSimpleName());
            }
            CastButtonFactory.zza(this.zzgs);
        }
    }

    public final zze zzq() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzgv;
    }

    public final IObjectWrapper zzr() {
        try {
            return this.zzgt.zzx();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getWrappedThis", zzj.class.getSimpleName());
            return null;
        }
    }
}
