package com.google.android.gms.internal.cast;

import com.google.android.gms.common.util.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public final class zzcc {
    private final int zzee;
    private final String zzts;
    private final JSONObject zzup;

    private zzcc(String str, int i, JSONObject jSONObject) {
        this.zzts = str;
        this.zzee = i;
        this.zzup = jSONObject;
    }

    public zzcc(JSONObject jSONObject) throws JSONException {
        this(jSONObject.optString("playerId"), jSONObject.optInt("playerState"), jSONObject.optJSONObject("playerData"));
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof zzcc)) {
            return false;
        }
        zzcc zzcc = (zzcc) obj;
        if (this.zzee == zzcc.zzee && zzcu.zza(this.zzts, zzcc.zzts) && JsonUtils.areJsonValuesEquivalent(this.zzup, zzcc.zzup)) {
            return true;
        }
        return false;
    }

    public final JSONObject getPlayerData() {
        return this.zzup;
    }

    public final String getPlayerId() {
        return this.zzts;
    }

    public final int getPlayerState() {
        return this.zzee;
    }
}
