package com.google.android.gms.internal.cast;

import android.content.Context;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionProvider;

public final class zzf extends SessionProvider {
    private final CastOptions zzgy;
    private final zzw zzip;

    public zzf(Context context, CastOptions castOptions, zzw zzw) {
        super(context, castOptions.getSupportedNamespaces().isEmpty() ? CastMediaControlIntent.categoryForCast(castOptions.getReceiverApplicationId()) : CastMediaControlIntent.categoryForCast(castOptions.getReceiverApplicationId(), castOptions.getSupportedNamespaces()));
        this.zzgy = castOptions;
        this.zzip = zzw;
    }

    public final Session createSession(String str) {
        return new CastSession(getContext(), getCategory(), str, this.zzgy, Cast.CastApi, new zzg(), new zzai(getContext(), this.zzgy, this.zzip));
    }

    public final boolean isSessionRecoverable() {
        return this.zzgy.getResumeSavedSession();
    }
}
