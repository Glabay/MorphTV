package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.cast.games.PlayerInfo;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.util.JsonUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public final class zzca implements GameManagerState {
    private final String zztv;
    private final int zztw;
    private final int zzuk;
    private final int zzul;
    private final String zzum;
    private final JSONObject zzun;
    private final Map<String, PlayerInfo> zzuo;

    public zzca(int i, int i2, String str, JSONObject jSONObject, Collection<PlayerInfo> collection, String str2, int i3) {
        this.zzuk = i;
        this.zzul = i2;
        this.zzum = str;
        this.zzun = jSONObject;
        this.zztv = str2;
        this.zztw = i3;
        this.zzuo = new HashMap(collection.size());
        for (PlayerInfo playerInfo : collection) {
            this.zzuo.put(playerInfo.getPlayerId(), playerInfo);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GameManagerState)) {
            return false;
        }
        GameManagerState gameManagerState = (GameManagerState) obj;
        if (getPlayers().size() != gameManagerState.getPlayers().size()) {
            return false;
        }
        for (PlayerInfo playerInfo : getPlayers()) {
            Object obj2 = null;
            for (PlayerInfo playerInfo2 : gameManagerState.getPlayers()) {
                if (zzcu.zza(playerInfo.getPlayerId(), playerInfo2.getPlayerId())) {
                    if (!zzcu.zza(playerInfo, playerInfo2)) {
                        return false;
                    }
                    obj2 = 1;
                }
            }
            if (obj2 == null) {
                return false;
            }
        }
        return this.zzuk == gameManagerState.getLobbyState() && this.zzul == gameManagerState.getGameplayState() && this.zztw == gameManagerState.getMaxPlayers() && zzcu.zza(this.zztv, gameManagerState.getApplicationName()) && zzcu.zza(this.zzum, gameManagerState.getGameStatusText()) && JsonUtils.areJsonValuesEquivalent(this.zzun, gameManagerState.getGameData());
    }

    public final CharSequence getApplicationName() {
        return this.zztv;
    }

    public final List<PlayerInfo> getConnectedControllablePlayers() {
        List arrayList = new ArrayList();
        for (PlayerInfo playerInfo : getPlayers()) {
            if (playerInfo.isConnected() && playerInfo.isControllable()) {
                arrayList.add(playerInfo);
            }
        }
        return arrayList;
    }

    public final List<PlayerInfo> getConnectedPlayers() {
        List arrayList = new ArrayList();
        for (PlayerInfo playerInfo : getPlayers()) {
            if (playerInfo.isConnected()) {
                arrayList.add(playerInfo);
            }
        }
        return arrayList;
    }

    public final List<PlayerInfo> getControllablePlayers() {
        List arrayList = new ArrayList();
        for (PlayerInfo playerInfo : getPlayers()) {
            if (playerInfo.isControllable()) {
                arrayList.add(playerInfo);
            }
        }
        return arrayList;
    }

    public final JSONObject getGameData() {
        return this.zzun;
    }

    public final CharSequence getGameStatusText() {
        return this.zzum;
    }

    public final int getGameplayState() {
        return this.zzul;
    }

    public final Collection<String> getListOfChangedPlayers(GameManagerState gameManagerState) {
        Collection hashSet = new HashSet();
        for (PlayerInfo playerInfo : getPlayers()) {
            PlayerInfo player = gameManagerState.getPlayer(playerInfo.getPlayerId());
            if (player == null || !playerInfo.equals(player)) {
                hashSet.add(playerInfo.getPlayerId());
            }
        }
        for (PlayerInfo playerInfo2 : gameManagerState.getPlayers()) {
            if (getPlayer(playerInfo2.getPlayerId()) == null) {
                hashSet.add(playerInfo2.getPlayerId());
            }
        }
        return hashSet;
    }

    public final int getLobbyState() {
        return this.zzuk;
    }

    public final int getMaxPlayers() {
        return this.zztw;
    }

    public final PlayerInfo getPlayer(String str) {
        return str == null ? null : (PlayerInfo) this.zzuo.get(str);
    }

    public final Collection<PlayerInfo> getPlayers() {
        return Collections.unmodifiableCollection(this.zzuo.values());
    }

    public final List<PlayerInfo> getPlayersInState(int i) {
        List arrayList = new ArrayList();
        for (PlayerInfo playerInfo : getPlayers()) {
            if (playerInfo.getPlayerState() == i) {
                arrayList.add(playerInfo);
            }
        }
        return arrayList;
    }

    public final boolean hasGameDataChanged(GameManagerState gameManagerState) {
        return !JsonUtils.areJsonValuesEquivalent(this.zzun, gameManagerState.getGameData());
    }

    public final boolean hasGameStatusTextChanged(GameManagerState gameManagerState) {
        return !zzcu.zza(this.zzum, gameManagerState.getGameStatusText());
    }

    public final boolean hasGameplayStateChanged(GameManagerState gameManagerState) {
        return this.zzul != gameManagerState.getGameplayState();
    }

    public final boolean hasLobbyStateChanged(GameManagerState gameManagerState) {
        return this.zzuk != gameManagerState.getLobbyState();
    }

    public final boolean hasPlayerChanged(String str, GameManagerState gameManagerState) {
        return !zzcu.zza(getPlayer(str), gameManagerState.getPlayer(str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean hasPlayerDataChanged(java.lang.String r3, com.google.android.gms.cast.games.GameManagerState r4) {
        /*
        r2 = this;
        r0 = r2.getPlayer(r3);
        r3 = r4.getPlayer(r3);
        r4 = 0;
        if (r0 != 0) goto L_0x000e;
    L_0x000b:
        if (r3 != 0) goto L_0x000e;
    L_0x000d:
        return r4;
    L_0x000e:
        r1 = 1;
        if (r0 == 0) goto L_0x0023;
    L_0x0011:
        if (r3 == 0) goto L_0x0023;
    L_0x0013:
        r0 = r0.getPlayerData();
        r3 = r3.getPlayerData();
        r3 = com.google.android.gms.common.util.JsonUtils.areJsonValuesEquivalent(r0, r3);
        if (r3 != 0) goto L_0x0022;
    L_0x0021:
        return r1;
    L_0x0022:
        return r4;
    L_0x0023:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzca.hasPlayerDataChanged(java.lang.String, com.google.android.gms.cast.games.GameManagerState):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean hasPlayerStateChanged(java.lang.String r3, com.google.android.gms.cast.games.GameManagerState r4) {
        /*
        r2 = this;
        r0 = r2.getPlayer(r3);
        r3 = r4.getPlayer(r3);
        r4 = 0;
        if (r0 != 0) goto L_0x000e;
    L_0x000b:
        if (r3 != 0) goto L_0x000e;
    L_0x000d:
        return r4;
    L_0x000e:
        r1 = 1;
        if (r0 == 0) goto L_0x001f;
    L_0x0011:
        if (r3 == 0) goto L_0x001f;
    L_0x0013:
        r0 = r0.getPlayerState();
        r3 = r3.getPlayerState();
        if (r0 == r3) goto L_0x001e;
    L_0x001d:
        return r1;
    L_0x001e:
        return r4;
    L_0x001f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzca.hasPlayerStateChanged(java.lang.String, com.google.android.gms.cast.games.GameManagerState):boolean");
    }

    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.zzuk), Integer.valueOf(this.zzul), this.zzuo, this.zzum, this.zzun, this.zztv, Integer.valueOf(this.zztw));
    }
}
