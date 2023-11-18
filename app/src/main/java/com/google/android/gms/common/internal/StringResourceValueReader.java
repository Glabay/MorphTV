package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.common.C0783R;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import javax.annotation.Nullable;

public class StringResourceValueReader {
    private final Resources zzvb;
    private final String zzvc = this.zzvb.getResourcePackageName(C0783R.string.common_google_play_services_unknown_issue);

    public StringResourceValueReader(Context context) {
        Preconditions.checkNotNull(context);
        this.zzvb = context.getResources();
    }

    @Nullable
    public String getString(String str) {
        int identifier = this.zzvb.getIdentifier(str, SerializerHandler.TYPE_STRING, this.zzvc);
        return identifier == 0 ? null : this.zzvb.getString(identifier);
    }
}
