package com.google.android.gms.cast.framework.media;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class zzaz implements OnClickListener {
    private final /* synthetic */ TracksChooserDialogFragment zzoq;

    zzaz(TracksChooserDialogFragment tracksChooserDialogFragment) {
        this.zzoq = tracksChooserDialogFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (this.zzoq.zzon != null) {
            this.zzoq.zzon.cancel();
            this.zzoq.zzon = null;
        }
    }
}
