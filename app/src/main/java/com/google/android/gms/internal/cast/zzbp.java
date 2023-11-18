package com.google.android.gms.internal.cast;

import org.json.JSONObject;

final class zzbp extends zzbr {
    private final /* synthetic */ zzbl zzth;
    private final /* synthetic */ String zztk;
    private final /* synthetic */ JSONObject zztl;

    zzbp(zzbl zzbl, String str, JSONObject jSONObject) {
        this.zzth = zzbl;
        this.zztk = str;
        this.zztl = jSONObject;
        super(zzbl);
    }

    public final void execute() {
        this.zzth.zza(this.zztk, 6, this.zztl, this.zztp);
    }
}
