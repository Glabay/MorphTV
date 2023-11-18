package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.CastStatusCodes;
import org.json.JSONObject;

final class zzbo extends zzbr {
    private final /* synthetic */ zzbl zzth;
    private final /* synthetic */ int zztj;
    private final /* synthetic */ String zztk;
    private final /* synthetic */ JSONObject zztl;

    zzbo(zzbl zzbl, int i, String str, JSONObject jSONObject) {
        this.zzth = zzbl;
        this.zztj = i;
        this.zztk = str;
        this.zztl = jSONObject;
        super(zzbl);
    }

    public final void execute() {
        int i;
        switch (this.zztj) {
            case 2:
                i = 5;
                break;
            case 3:
                i = 1;
                break;
            case 4:
                i = 2;
                break;
            case 5:
                i = 3;
                break;
            case 6:
                i = 4;
                break;
            default:
                i = 0;
                break;
        }
        if (i == 0) {
            this.zztp.zza(-1, CastStatusCodes.INVALID_REQUEST, null);
            zzbl.zzbd.m28w("sendPlayerRequest for unsupported playerState: %d", Integer.valueOf(this.zztj));
            return;
        }
        this.zzth.zza(this.zztk, i, this.zztl, this.zztp);
    }
}
