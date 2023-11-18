package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.games.PlayerInfo;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.util.JsonUtils;
import org.json.JSONObject;

public final class zzcb implements PlayerInfo {
    private final int zzee;
    private final String zzts;
    private final JSONObject zzup;
    private final boolean zzuq;

    public zzcb(String str, int i, JSONObject jSONObject, boolean z) {
        this.zzts = str;
        this.zzee = i;
        this.zzup = jSONObject;
        this.zzuq = z;
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PlayerInfo)) {
            return false;
        }
        PlayerInfo playerInfo = (PlayerInfo) obj;
        if (this.zzuq == playerInfo.isControllable() && this.zzee == playerInfo.getPlayerState() && zzcu.zza(this.zzts, playerInfo.getPlayerId()) && JsonUtils.areJsonValuesEquivalent(this.zzup, playerInfo.getPlayerData())) {
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

    public final int hashCode() {
        return Objects.hashCode(this.zzts, Integer.valueOf(this.zzee), this.zzup, Boolean.valueOf(this.zzuq));
    }

    public final boolean isConnected() {
        switch (this.zzee) {
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public final boolean isControllable() {
        return this.zzuq;
    }
}
