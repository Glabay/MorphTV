package com.google.android.gms.cast.framework.media;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class zzba implements OnClickListener {
    private final /* synthetic */ TracksChooserDialogFragment zzoq;
    private final /* synthetic */ zzbb zzor;
    private final /* synthetic */ zzbb zzos;

    zzba(TracksChooserDialogFragment tracksChooserDialogFragment, zzbb zzbb, zzbb zzbb2) {
        this.zzoq = tracksChooserDialogFragment;
        this.zzor = zzbb;
        this.zzos = zzbb2;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.zzoq.zza(this.zzor, this.zzos);
    }
}
