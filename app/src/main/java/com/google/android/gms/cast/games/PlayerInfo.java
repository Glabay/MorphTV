package com.google.android.gms.cast.games;

import org.json.JSONObject;

@Deprecated
public interface PlayerInfo {
    JSONObject getPlayerData();

    String getPlayerId();

    int getPlayerState();

    boolean isConnected();

    boolean isControllable();
}
