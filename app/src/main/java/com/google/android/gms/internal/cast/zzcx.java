package com.google.android.gms.internal.cast;

import android.content.Context;
import com.google.android.gms.flags.Flag;
import com.google.android.gms.flags.FlagRegistry;
import com.google.android.gms.flags.Singletons;

public final class zzcx {
    public static final Flag<Boolean> zzwc = Flag.define(0, "gms:cast:remote_display_enabled", Boolean.valueOf(false));

    public static final void initialize(Context context) {
        Singletons.flagRegistry();
        FlagRegistry.initialize(context);
    }
}
