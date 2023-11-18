package com.google.android.gms.internal.cast;

import com.google.android.gms.dynamite.ProviderConstants;
import org.json.JSONException;
import org.json.JSONObject;

public final class zzby {
    private final String version;
    private final String zztv;
    private final int zztw;

    private zzby(String str, int i, String str2) {
        this.zztv = str;
        this.zztw = i;
        this.version = str2;
    }

    public zzby(JSONObject jSONObject) throws JSONException {
        this(jSONObject.optString("applicationName"), jSONObject.optInt("maxPlayers"), jSONObject.optString(ProviderConstants.API_COLNAME_FEATURE_VERSION));
    }

    public final int getMaxPlayers() {
        return this.zztw;
    }

    public final String getVersion() {
        return this.version;
    }

    public final String zzck() {
        return this.zztv;
    }
}
