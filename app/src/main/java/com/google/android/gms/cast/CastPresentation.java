package com.google.android.gms.cast;

import android.annotation.TargetApi;
import android.app.Presentation;
import android.content.Context;
import android.view.Display;
import android.view.Window;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;

@TargetApi(19)
public class CastPresentation extends Presentation {
    public CastPresentation(Context context, Display display) {
        super(context, display);
        zza();
    }

    public CastPresentation(Context context, Display display, int i) {
        super(context, display, i);
        zza();
    }

    private final void zza() {
        Window window = getWindow();
        if (window != null) {
            window.setType(2030);
            window.addFlags(ErrorDialogData.BINDER_CRASH);
            window.addFlags(16777216);
            window.addFlags(1024);
        }
    }
}
