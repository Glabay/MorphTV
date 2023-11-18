package com.google.android.gms.internal.cast;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.CastApi;
import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerClient.GameManagerInstanceResult;
import com.google.android.gms.cast.games.GameManagerClient.GameManagerResult;
import com.google.android.gms.cast.games.GameManagerClient.Listener;
import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public final class zzbl extends zzcg {
    private static final String NAMESPACE = zzcu.zzp("com.google.cast.games");
    private static final zzdg zzbd = new zzdg("GameManagerChannel");
    private final CastApi zzhl;
    private final GoogleApiClient zznm;
    private final Map<String, String> zzsu = new ConcurrentHashMap();
    private final SharedPreferences zzsv;
    private final String zzsw;
    private zzby zzsx;
    private boolean zzsy = false;
    private GameManagerState zzsz;
    private GameManagerState zzta;
    private String zztb;
    private JSONObject zztc;
    private long zztd = 0;
    private Listener zzte;
    private final Clock zztf;
    private String zztg;

    public zzbl(GoogleApiClient googleApiClient, String str, CastApi castApi) throws IllegalArgumentException, IllegalStateException {
        super(NAMESPACE, "CastGameManagerChannel", null);
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("castSessionId cannot be null.");
        }
        if (googleApiClient != null && googleApiClient.isConnected()) {
            if (googleApiClient.hasConnectedApi(Cast.API)) {
                this.zztf = DefaultClock.getInstance();
                this.zzsw = str;
                this.zzhl = castApi;
                this.zznm = googleApiClient;
                this.zzsv = r12.getApplicationContext().getSharedPreferences(String.format(Locale.ROOT, "%s.%s", new Object[]{googleApiClient.getContext().getApplicationContext().getPackageName(), "game_manager_channel_data"}), 0);
                this.zzta = null;
                this.zzsz = new zzca(0, 0, "", null, new ArrayList(), "", -1);
                return;
            }
        }
        throw new IllegalArgumentException("googleApiClient needs to be connected and contain the Cast.API API.");
    }

    private final synchronized boolean isInitialized() {
        return this.zzsx != null;
    }

    private final JSONObject zza(long j, String str, int i, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("requestId", j);
            jSONObject2.put("type", i);
            jSONObject2.put("extraMessageData", jSONObject);
            jSONObject2.put("playerId", str);
            jSONObject2.put("playerToken", zzm(str));
            return jSONObject2;
        } catch (JSONException e) {
            zzbd.m28w("JSONException when trying to create a message: %s", e.getMessage());
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final synchronized void zza(com.google.android.gms.internal.cast.zzbz r10) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = r10.zztx;	 Catch:{ all -> 0x008b }
        r1 = 1;
        if (r0 != r1) goto L_0x0007;
    L_0x0006:
        goto L_0x0008;
    L_0x0007:
        r1 = 0;
    L_0x0008:
        r0 = r9.zzsz;	 Catch:{ all -> 0x008b }
        r9.zzta = r0;	 Catch:{ all -> 0x008b }
        if (r1 == 0) goto L_0x0016;
    L_0x000e:
        r0 = r10.zzuj;	 Catch:{ all -> 0x008b }
        if (r0 == 0) goto L_0x0016;
    L_0x0012:
        r0 = r10.zzuj;	 Catch:{ all -> 0x008b }
        r9.zzsx = r0;	 Catch:{ all -> 0x008b }
    L_0x0016:
        r0 = r9.isInitialized();	 Catch:{ all -> 0x008b }
        if (r0 != 0) goto L_0x001e;
    L_0x001c:
        monitor-exit(r9);
        return;
    L_0x001e:
        r6 = new java.util.ArrayList;	 Catch:{ all -> 0x008b }
        r6.<init>();	 Catch:{ all -> 0x008b }
        r0 = r10.zzud;	 Catch:{ all -> 0x008b }
        r0 = r0.iterator();	 Catch:{ all -> 0x008b }
    L_0x0029:
        r1 = r0.hasNext();	 Catch:{ all -> 0x008b }
        if (r1 == 0) goto L_0x0050;
    L_0x002f:
        r1 = r0.next();	 Catch:{ all -> 0x008b }
        r1 = (com.google.android.gms.internal.cast.zzcc) r1;	 Catch:{ all -> 0x008b }
        r2 = r1.getPlayerId();	 Catch:{ all -> 0x008b }
        r3 = new com.google.android.gms.internal.cast.zzcb;	 Catch:{ all -> 0x008b }
        r4 = r1.getPlayerState();	 Catch:{ all -> 0x008b }
        r1 = r1.getPlayerData();	 Catch:{ all -> 0x008b }
        r5 = r9.zzsu;	 Catch:{ all -> 0x008b }
        r5 = r5.containsKey(r2);	 Catch:{ all -> 0x008b }
        r3.<init>(r2, r4, r1, r5);	 Catch:{ all -> 0x008b }
        r6.add(r3);	 Catch:{ all -> 0x008b }
        goto L_0x0029;
    L_0x0050:
        r0 = new com.google.android.gms.internal.cast.zzca;	 Catch:{ all -> 0x008b }
        r2 = r10.zzuc;	 Catch:{ all -> 0x008b }
        r3 = r10.zzub;	 Catch:{ all -> 0x008b }
        r4 = r10.zzuf;	 Catch:{ all -> 0x008b }
        r5 = r10.zzue;	 Catch:{ all -> 0x008b }
        r1 = r9.zzsx;	 Catch:{ all -> 0x008b }
        r7 = r1.zzck();	 Catch:{ all -> 0x008b }
        r1 = r9.zzsx;	 Catch:{ all -> 0x008b }
        r8 = r1.getMaxPlayers();	 Catch:{ all -> 0x008b }
        r1 = r0;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x008b }
        r9.zzsz = r0;	 Catch:{ all -> 0x008b }
        r0 = r9.zzsz;	 Catch:{ all -> 0x008b }
        r1 = r10.zzug;	 Catch:{ all -> 0x008b }
        r0 = r0.getPlayer(r1);	 Catch:{ all -> 0x008b }
        if (r0 == 0) goto L_0x0089;
    L_0x0076:
        r0 = r0.isControllable();	 Catch:{ all -> 0x008b }
        if (r0 == 0) goto L_0x0089;
    L_0x007c:
        r0 = r10.zztx;	 Catch:{ all -> 0x008b }
        r1 = 2;
        if (r0 != r1) goto L_0x0089;
    L_0x0081:
        r0 = r10.zzug;	 Catch:{ all -> 0x008b }
        r9.zztb = r0;	 Catch:{ all -> 0x008b }
        r10 = r10.zzua;	 Catch:{ all -> 0x008b }
        r9.zztc = r10;	 Catch:{ all -> 0x008b }
    L_0x0089:
        monitor-exit(r9);
        return;
    L_0x008b:
        r10 = move-exception;
        monitor-exit(r9);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzbl.zza(com.google.android.gms.internal.cast.zzbz):void");
    }

    private final void zza(String str, int i, JSONObject jSONObject, zzdm zzdm) {
        long j = this.zztd + 1;
        this.zztd = j;
        JSONObject zza = zza(j, str, i, jSONObject);
        if (zza == null) {
            zzdm.zza(-1, CastStatusCodes.INVALID_REQUEST, null);
            zzbd.m28w("Not sending request because it was invalid.", new Object[0]);
            return;
        }
        zzdn zzdn = new zzdn(30000);
        zzdn.zza(j, zzdm);
        zza(zzdn);
        this.zzhl.sendMessage(this.zznm, getNamespace(), zza.toString()).setResultCallback(new zzbq(this, j));
    }

    private final void zzb(long j, int i, Object obj) {
        List zzcn = zzcn();
        synchronized (zzcn) {
            Iterator it = zzcn.iterator();
            while (it.hasNext()) {
                if (((zzdn) it.next()).zzc(j, i, obj)) {
                    it.remove();
                }
            }
        }
    }

    private final synchronized void zzcg() throws IllegalStateException {
        if (!isInitialized()) {
            throw new IllegalStateException("Attempted to perform an operation on the GameManagerChannel before it is initialized.");
        } else if (isDisposed()) {
            throw new IllegalStateException("Attempted to perform an operation on the GameManagerChannel after it has been disposed.");
        }
    }

    private final synchronized void zzch() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("castSessionId", this.zzsw);
            jSONObject.put("playerTokenMap", new JSONObject(this.zzsu));
            this.zzsv.edit().putString("save_data", jSONObject.toString()).commit();
        } catch (JSONException e) {
            zzbd.m28w("Error while saving data: %s", e.getMessage());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final synchronized void zzci() {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = r5.zzsv;	 Catch:{ all -> 0x005b }
        r1 = "save_data";
        r2 = 0;
        r0 = r0.getString(r1, r2);	 Catch:{ all -> 0x005b }
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r5);
        return;
    L_0x000e:
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0047 }
        r1.<init>(r0);	 Catch:{ JSONException -> 0x0047 }
        r0 = "castSessionId";
        r0 = r1.getString(r0);	 Catch:{ JSONException -> 0x0047 }
        r2 = r5.zzsw;	 Catch:{ JSONException -> 0x0047 }
        r0 = r2.equals(r0);	 Catch:{ JSONException -> 0x0047 }
        if (r0 == 0) goto L_0x0045;
    L_0x0021:
        r0 = "playerTokenMap";
        r0 = r1.getJSONObject(r0);	 Catch:{ JSONException -> 0x0047 }
        r1 = r0.keys();	 Catch:{ JSONException -> 0x0047 }
    L_0x002b:
        r2 = r1.hasNext();	 Catch:{ JSONException -> 0x0047 }
        if (r2 == 0) goto L_0x0041;
    L_0x0031:
        r2 = r1.next();	 Catch:{ JSONException -> 0x0047 }
        r2 = (java.lang.String) r2;	 Catch:{ JSONException -> 0x0047 }
        r3 = r5.zzsu;	 Catch:{ JSONException -> 0x0047 }
        r4 = r0.getString(r2);	 Catch:{ JSONException -> 0x0047 }
        r3.put(r2, r4);	 Catch:{ JSONException -> 0x0047 }
        goto L_0x002b;
    L_0x0041:
        r0 = 0;
        r5.zztd = r0;	 Catch:{ JSONException -> 0x0047 }
    L_0x0045:
        monitor-exit(r5);
        return;
    L_0x0047:
        r0 = move-exception;
        r1 = zzbd;	 Catch:{ all -> 0x005b }
        r2 = "Error while loading data: %s";
        r3 = 1;
        r3 = new java.lang.Object[r3];	 Catch:{ all -> 0x005b }
        r4 = 0;
        r0 = r0.getMessage();	 Catch:{ all -> 0x005b }
        r3[r4] = r0;	 Catch:{ all -> 0x005b }
        r1.m28w(r2, r3);	 Catch:{ all -> 0x005b }
        monitor-exit(r5);
        return;
    L_0x005b:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzbl.zzci():void");
    }

    private final synchronized String zzm(String str) throws IllegalStateException {
        if (str == null) {
            return null;
        }
        return (String) this.zzsu.get(str);
    }

    public final synchronized void dispose() throws IllegalStateException {
        if (!this.zzsy) {
            this.zzsz = null;
            this.zzta = null;
            this.zztb = null;
            this.zztc = null;
            this.zzsy = true;
            try {
                this.zzhl.removeMessageReceivedCallbacks(this.zznm, getNamespace());
            } catch (IOException e) {
                zzbd.m28w("Exception while detaching game manager channel.", e);
            }
        }
    }

    public final synchronized GameManagerState getCurrentState() throws IllegalStateException {
        zzcg();
        return this.zzsz;
    }

    public final synchronized String getLastUsedPlayerId() throws IllegalStateException {
        zzcg();
        return this.zztg;
    }

    public final synchronized boolean isDisposed() {
        return this.zzsy;
    }

    public final synchronized void sendGameMessage(String str, JSONObject jSONObject) throws IllegalStateException {
        zzcg();
        long j = this.zztd + 1;
        this.zztd = j;
        JSONObject zza = zza(j, str, 7, jSONObject);
        if (zza != null) {
            this.zzhl.sendMessage(this.zznm, getNamespace(), zza.toString());
        }
    }

    public final synchronized PendingResult<GameManagerResult> sendGameRequest(String str, JSONObject jSONObject) throws IllegalStateException {
        zzcg();
        return this.zznm.execute(new zzbp(this, str, jSONObject));
    }

    public final synchronized void setListener(Listener listener) {
        this.zzte = listener;
    }

    public final synchronized PendingResult<GameManagerInstanceResult> zza(GameManagerClient gameManagerClient) throws IllegalArgumentException {
        return this.zznm.execute(new zzbm(this, gameManagerClient));
    }

    public final synchronized PendingResult<GameManagerResult> zza(String str, int i, JSONObject jSONObject) throws IllegalStateException {
        zzcg();
        return this.zznm.execute(new zzbo(this, i, str, jSONObject));
    }

    public final void zza(long j, int i) {
        zzb(j, i, null);
    }

    public final void zzn(String str) {
        Object[] objArr = new Object[1];
        int i = 0;
        objArr[0] = str;
        zzbd.m25d("message received: %s", objArr);
        try {
            zzbz zzh = zzbz.zzh(new JSONObject(str));
            if (zzh == null) {
                zzbd.m28w("Could not parse game manager message from string: %s", str);
            } else if ((isInitialized() || zzh.zzuj != null) && !isDisposed()) {
                Object obj = zzh.zztx == 1 ? 1 : null;
                if (!(obj == null || TextUtils.isEmpty(zzh.zzui))) {
                    this.zzsu.put(zzh.zzug, zzh.zzui);
                    zzch();
                }
                if (zzh.zzty == 0) {
                    zza(zzh);
                } else {
                    zzbd.m28w("Not updating from game message because the message contains error code: %d", Integer.valueOf(zzh.zzty));
                }
                int i2 = zzh.zzty;
                switch (i2) {
                    case 0:
                        break;
                    case 1:
                        i = CastStatusCodes.INVALID_REQUEST;
                        break;
                    case 2:
                        i = CastStatusCodes.NOT_ALLOWED;
                        break;
                    case 3:
                        i = GameManagerClient.STATUS_INCORRECT_VERSION;
                        break;
                    case 4:
                        i = GameManagerClient.STATUS_TOO_MANY_PLAYERS;
                        break;
                    default:
                        zzdg zzdg = zzbd;
                        StringBuilder stringBuilder = new StringBuilder(53);
                        stringBuilder.append("Unknown GameManager protocol status code: ");
                        stringBuilder.append(i2);
                        zzdg.m28w(stringBuilder.toString(), new Object[0]);
                        i = 13;
                        break;
                }
                if (obj != null) {
                    zzb(zzh.zzuh, i, zzh);
                }
                if (isInitialized() && i == 0) {
                    if (this.zzte != null) {
                        if (!(this.zzta == null || this.zzsz.equals(this.zzta))) {
                            this.zzte.onStateChanged(this.zzsz, this.zzta);
                        }
                        if (!(this.zztc == null || this.zztb == null)) {
                            this.zzte.onGameMessageReceived(this.zztb, this.zztc);
                        }
                    }
                    this.zzta = null;
                    this.zztb = null;
                    this.zztc = null;
                }
            }
        } catch (JSONException e) {
            zzbd.m28w("Message is malformed (%s); ignoring: %s", e.getMessage(), str);
        }
    }
}
