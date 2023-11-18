package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.android.gms.cast.MediaQueueItem.Builder;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.cast.zzcu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "MediaStatusCreator")
@Reserved({1})
@VisibleForTesting
public class MediaStatus extends AbstractSafeParcelable {
    public static final long COMMAND_PAUSE = 1;
    public static final long COMMAND_SEEK = 2;
    public static final long COMMAND_SET_VOLUME = 4;
    public static final long COMMAND_SKIP_BACKWARD = 32;
    public static final long COMMAND_SKIP_FORWARD = 16;
    public static final long COMMAND_TOGGLE_MUTE = 8;
    public static final Creator<MediaStatus> CREATOR = new zzan();
    public static final int IDLE_REASON_CANCELED = 2;
    public static final int IDLE_REASON_ERROR = 4;
    public static final int IDLE_REASON_FINISHED = 1;
    public static final int IDLE_REASON_INTERRUPTED = 3;
    public static final int IDLE_REASON_NONE = 0;
    public static final int PLAYER_STATE_BUFFERING = 4;
    public static final int PLAYER_STATE_IDLE = 1;
    public static final int PLAYER_STATE_PAUSED = 3;
    public static final int PLAYER_STATE_PLAYING = 2;
    public static final int PLAYER_STATE_UNKNOWN = 0;
    public static final int REPEAT_MODE_REPEAT_ALL = 1;
    public static final int REPEAT_MODE_REPEAT_ALL_AND_SHUFFLE = 3;
    public static final int REPEAT_MODE_REPEAT_OFF = 0;
    public static final int REPEAT_MODE_REPEAT_SINGLE = 2;
    @Field(getter = "getMediaInfo", id = 2)
    private MediaInfo zzdi;
    @Field(getter = "getPlaybackRate", id = 5)
    private double zzdl;
    @Field(getter = "getActiveTrackIds", id = 12)
    private long[] zzdm;
    @Field(getter = "getMediaSessionId", id = 3)
    private long zzec;
    @Field(getter = "getCurrentItemId", id = 4)
    private int zzed;
    @Field(getter = "getPlayerState", id = 6)
    private int zzee;
    @Field(getter = "getIdleReason", id = 7)
    private int zzef;
    @Field(getter = "getStreamPosition", id = 8)
    private long zzeg;
    @Field(id = 9)
    private long zzeh;
    @Field(getter = "getStreamVolume", id = 10)
    private double zzei;
    @Field(getter = "isMute", id = 11)
    private boolean zzej;
    @Field(getter = "getLoadingItemId", id = 13)
    private int zzek;
    @Field(getter = "getPreloadedItemId", id = 14)
    private int zzel;
    @Field(id = 16)
    private int zzem;
    @Field(id = 17)
    private final ArrayList<MediaQueueItem> zzen;
    @Field(getter = "isPlayingAd", id = 18)
    private boolean zzeo;
    @Field(getter = "getAdBreakStatus", id = 19)
    private AdBreakStatus zzep;
    @Field(getter = "getVideoInfo", id = 20)
    private VideoInfo zzeq;
    private final SparseArray<Integer> zzer;
    @Field(id = 15)
    private String zzj;
    private JSONObject zzp;

    @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
    MediaStatus(@com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 2) com.google.android.gms.cast.MediaInfo r6, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 3) long r7, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 4) int r9, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 5) double r10, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 6) int r12, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 7) int r13, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 8) long r14, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 9) long r16, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 10) double r18, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 11) boolean r20, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 12) long[] r21, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 13) int r22, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 14) int r23, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 15) java.lang.String r24, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 16) int r25, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 17) java.util.List<com.google.android.gms.cast.MediaQueueItem> r26, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 18) boolean r27, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 19) com.google.android.gms.cast.AdBreakStatus r28, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 20) com.google.android.gms.cast.VideoInfo r29) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = r5;
        r1 = r26;
        r0.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r0.zzen = r2;
        r2 = new android.util.SparseArray;
        r2.<init>();
        r0.zzer = r2;
        r2 = r6;
        r0.zzdi = r2;
        r2 = r7;
        r0.zzec = r2;
        r2 = r9;
        r0.zzed = r2;
        r2 = r10;
        r0.zzdl = r2;
        r2 = r12;
        r0.zzee = r2;
        r2 = r13;
        r0.zzef = r2;
        r2 = r14;
        r0.zzeg = r2;
        r2 = r16;
        r0.zzeh = r2;
        r2 = r18;
        r0.zzei = r2;
        r2 = r20;
        r0.zzej = r2;
        r2 = r21;
        r0.zzdm = r2;
        r2 = r22;
        r0.zzek = r2;
        r2 = r23;
        r0.zzel = r2;
        r2 = r24;
        r0.zzj = r2;
        r2 = r0.zzj;
        r3 = 0;
        if (r2 == 0) goto L_0x0059;
    L_0x004a:
        r2 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0054 }
        r4 = r0.zzj;	 Catch:{ JSONException -> 0x0054 }
        r2.<init>(r4);	 Catch:{ JSONException -> 0x0054 }
        r0.zzp = r2;	 Catch:{ JSONException -> 0x0054 }
        goto L_0x005b;
    L_0x0054:
        r0.zzp = r3;
        r0.zzj = r3;
        goto L_0x005b;
    L_0x0059:
        r0.zzp = r3;
    L_0x005b:
        r2 = r25;
        r0.zzem = r2;
        if (r1 == 0) goto L_0x0076;
    L_0x0061:
        r2 = r26.isEmpty();
        if (r2 != 0) goto L_0x0076;
    L_0x0067:
        r2 = r26.size();
        r2 = new com.google.android.gms.cast.MediaQueueItem[r2];
        r1 = r1.toArray(r2);
        r1 = (com.google.android.gms.cast.MediaQueueItem[]) r1;
        r0.zza(r1);
    L_0x0076:
        r1 = r27;
        r0.zzeo = r1;
        r1 = r28;
        r0.zzep = r1;
        r1 = r29;
        r0.zzeq = r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaStatus.<init>(com.google.android.gms.cast.MediaInfo, long, int, double, int, int, long, long, double, boolean, long[], int, int, java.lang.String, int, java.util.List, boolean, com.google.android.gms.cast.AdBreakStatus, com.google.android.gms.cast.VideoInfo):void");
    }

    public MediaStatus(JSONObject jSONObject) throws JSONException {
        this(null, 0, 0, 0.0d, 0, 0, 0, 0, 0.0d, false, null, 0, 0, null, 0, null, false, null, null);
        zza(jSONObject, 0);
    }

    private final void zza(MediaQueueItem[] mediaQueueItemArr) {
        this.zzen.clear();
        this.zzer.clear();
        for (int i = 0; i < mediaQueueItemArr.length; i++) {
            MediaQueueItem mediaQueueItem = mediaQueueItemArr[i];
            this.zzen.add(mediaQueueItem);
            this.zzer.put(mediaQueueItem.getItemId(), Integer.valueOf(i));
        }
    }

    private static boolean zza(int i, int i2, int i3, int i4) {
        if (i != 1) {
            return false;
        }
        switch (i2) {
            case 1:
            case 3:
                return i3 == 0;
            case 2:
                return i4 != 2;
            default:
                return true;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaStatus)) {
            return false;
        }
        MediaStatus mediaStatus = (MediaStatus) obj;
        if ((this.zzp == null ? 1 : null) == (mediaStatus.zzp == null ? 1 : null) && this.zzec == mediaStatus.zzec && this.zzed == mediaStatus.zzed && this.zzdl == mediaStatus.zzdl && this.zzee == mediaStatus.zzee && this.zzef == mediaStatus.zzef && this.zzeg == mediaStatus.zzeg && this.zzei == mediaStatus.zzei && this.zzej == mediaStatus.zzej && this.zzek == mediaStatus.zzek && this.zzel == mediaStatus.zzel && this.zzem == mediaStatus.zzem && Arrays.equals(this.zzdm, mediaStatus.zzdm) && zzcu.zza(Long.valueOf(this.zzeh), Long.valueOf(mediaStatus.zzeh)) && zzcu.zza(this.zzen, mediaStatus.zzen) && zzcu.zza(this.zzdi, mediaStatus.zzdi)) {
            Object obj2 = (this.zzp == null || mediaStatus.zzp == null || JsonUtils.areJsonValuesEquivalent(this.zzp, mediaStatus.zzp)) ? 1 : null;
            return obj2 != null && this.zzeo == mediaStatus.isPlayingAd();
        }
    }

    public long[] getActiveTrackIds() {
        return this.zzdm;
    }

    public AdBreakStatus getAdBreakStatus() {
        return this.zzep;
    }

    public AdBreakInfo getCurrentAdBreak() {
        if (this.zzep == null || this.zzdi == null) {
            return null;
        }
        Object breakId = this.zzep.getBreakId();
        if (TextUtils.isEmpty(breakId)) {
            return null;
        }
        List<AdBreakInfo> adBreaks = this.zzdi.getAdBreaks();
        if (adBreaks == null || adBreaks.isEmpty()) {
            return null;
        }
        for (AdBreakInfo adBreakInfo : adBreaks) {
            if (breakId.equals(adBreakInfo.getId())) {
                return adBreakInfo;
            }
        }
        return null;
    }

    public AdBreakClipInfo getCurrentAdBreakClip() {
        if (this.zzep == null || this.zzdi == null) {
            return null;
        }
        Object breakClipId = this.zzep.getBreakClipId();
        if (TextUtils.isEmpty(breakClipId)) {
            return null;
        }
        List<AdBreakClipInfo> adBreakClips = this.zzdi.getAdBreakClips();
        if (adBreakClips == null || adBreakClips.isEmpty()) {
            return null;
        }
        for (AdBreakClipInfo adBreakClipInfo : adBreakClips) {
            if (breakClipId.equals(adBreakClipInfo.getId())) {
                return adBreakClipInfo;
            }
        }
        return null;
    }

    public int getCurrentItemId() {
        return this.zzed;
    }

    public JSONObject getCustomData() {
        return this.zzp;
    }

    public int getIdleReason() {
        return this.zzef;
    }

    public Integer getIndexById(int i) {
        return (Integer) this.zzer.get(i);
    }

    public MediaQueueItem getItemById(int i) {
        Integer num = (Integer) this.zzer.get(i);
        return num == null ? null : (MediaQueueItem) this.zzen.get(num.intValue());
    }

    public MediaQueueItem getItemByIndex(int i) {
        if (i >= 0) {
            if (i < this.zzen.size()) {
                return (MediaQueueItem) this.zzen.get(i);
            }
        }
        return null;
    }

    public int getLoadingItemId() {
        return this.zzek;
    }

    public MediaInfo getMediaInfo() {
        return this.zzdi;
    }

    public double getPlaybackRate() {
        return this.zzdl;
    }

    public int getPlayerState() {
        return this.zzee;
    }

    public int getPreloadedItemId() {
        return this.zzel;
    }

    public MediaQueueItem getQueueItem(int i) {
        return getItemByIndex(i);
    }

    public MediaQueueItem getQueueItemById(int i) {
        return getItemById(i);
    }

    public int getQueueItemCount() {
        return this.zzen.size();
    }

    public List<MediaQueueItem> getQueueItems() {
        return this.zzen;
    }

    public int getQueueRepeatMode() {
        return this.zzem;
    }

    public long getStreamPosition() {
        return this.zzeg;
    }

    public double getStreamVolume() {
        return this.zzei;
    }

    public VideoInfo getVideoInfo() {
        return this.zzeq;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzdi, Long.valueOf(this.zzec), Integer.valueOf(this.zzed), Double.valueOf(this.zzdl), Integer.valueOf(this.zzee), Integer.valueOf(this.zzef), Long.valueOf(this.zzeg), Long.valueOf(this.zzeh), Double.valueOf(this.zzei), Boolean.valueOf(this.zzej), Integer.valueOf(Arrays.hashCode(this.zzdm)), Integer.valueOf(this.zzek), Integer.valueOf(this.zzel), String.valueOf(this.zzp), Integer.valueOf(this.zzem), this.zzen, Boolean.valueOf(this.zzeo));
    }

    public boolean isMediaCommandSupported(long j) {
        return (this.zzeh & j) != 0;
    }

    public boolean isMute() {
        return this.zzej;
    }

    public boolean isPlayingAd() {
        return this.zzeo;
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.zzj = this.zzp == null ? null : this.zzp.toString();
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, getMediaInfo(), i, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzec);
        SafeParcelWriter.writeInt(parcel, 4, getCurrentItemId());
        SafeParcelWriter.writeDouble(parcel, 5, getPlaybackRate());
        SafeParcelWriter.writeInt(parcel, 6, getPlayerState());
        SafeParcelWriter.writeInt(parcel, 7, getIdleReason());
        SafeParcelWriter.writeLong(parcel, 8, getStreamPosition());
        SafeParcelWriter.writeLong(parcel, 9, this.zzeh);
        SafeParcelWriter.writeDouble(parcel, 10, getStreamVolume());
        SafeParcelWriter.writeBoolean(parcel, 11, isMute());
        SafeParcelWriter.writeLongArray(parcel, 12, getActiveTrackIds(), false);
        SafeParcelWriter.writeInt(parcel, 13, getLoadingItemId());
        SafeParcelWriter.writeInt(parcel, 14, getPreloadedItemId());
        SafeParcelWriter.writeString(parcel, 15, this.zzj, false);
        SafeParcelWriter.writeInt(parcel, 16, this.zzem);
        SafeParcelWriter.writeTypedList(parcel, 17, this.zzen, false);
        SafeParcelWriter.writeBoolean(parcel, 18, isPlayingAd());
        SafeParcelWriter.writeParcelable(parcel, 19, getAdBreakStatus(), i, false);
        SafeParcelWriter.writeParcelable(parcel, 20, getVideoInfo(), i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final int zza(JSONObject jSONObject, int i) throws JSONException {
        int i2;
        int i3;
        double d;
        long j;
        int length;
        long[] jArr;
        Object obj;
        AdBreakStatus zzc;
        VideoInfo zzg;
        long j2 = jSONObject.getLong("mediaSessionId");
        boolean z = false;
        if (j2 != this.zzec) {
            this.zzec = j2;
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (jSONObject.has("playerState")) {
            String string = jSONObject.getString("playerState");
            i3 = 4;
            int i4 = string.equals("IDLE") ? 1 : string.equals("PLAYING") ? 2 : string.equals("PAUSED") ? 3 : string.equals("BUFFERING") ? 4 : 0;
            if (i4 != this.zzee) {
                this.zzee = i4;
                i2 |= 2;
            }
            if (i4 == 1 && jSONObject.has("idleReason")) {
                string = jSONObject.getString("idleReason");
                if (string.equals("CANCELLED")) {
                    i3 = 2;
                } else if (string.equals("INTERRUPTED")) {
                    i3 = 3;
                } else if (string.equals("FINISHED")) {
                    i3 = 1;
                } else if (!string.equals("ERROR")) {
                    i3 = 0;
                }
                if (i3 != this.zzef) {
                    this.zzef = i3;
                    i2 |= 2;
                }
            }
        }
        if (jSONObject.has("playbackRate")) {
            d = jSONObject.getDouble("playbackRate");
            if (this.zzdl != d) {
                this.zzdl = d;
                i2 |= 2;
            }
        }
        if (jSONObject.has("currentTime") && (i & 2) == 0) {
            j = (long) (jSONObject.getDouble("currentTime") * 1000.0d);
            if (j != this.zzeg) {
                this.zzeg = j;
                i2 |= 2;
            }
        }
        if (jSONObject.has("supportedMediaCommands")) {
            j = jSONObject.getLong("supportedMediaCommands");
            if (j != this.zzeh) {
                this.zzeh = j;
                i2 |= 2;
            }
        }
        if (jSONObject.has(MediaRouteProviderProtocol.CLIENT_DATA_VOLUME) && (i & 1) == 0) {
            JSONObject jSONObject2 = jSONObject.getJSONObject(MediaRouteProviderProtocol.CLIENT_DATA_VOLUME);
            d = jSONObject2.getDouble("level");
            if (d != this.zzei) {
                this.zzei = d;
                i2 |= 2;
            }
            boolean z2 = jSONObject2.getBoolean("muted");
            if (z2 != this.zzej) {
                this.zzej = z2;
                i2 |= 2;
            }
        }
        if (jSONObject.has("activeTrackIds")) {
            JSONArray jSONArray = jSONObject.getJSONArray("activeTrackIds");
            length = jSONArray.length();
            jArr = new long[length];
            for (int i5 = 0; i5 < length; i5++) {
                jArr[i5] = jSONArray.getLong(i5);
            }
            if (this.zzdm != null) {
                if (this.zzdm.length == length) {
                    i = 0;
                    while (i < length) {
                        if (this.zzdm[i] == jArr[i]) {
                            i++;
                        }
                    }
                    obj = null;
                    if (obj != null) {
                        this.zzdm = jArr;
                    }
                }
            }
            obj = 1;
            if (obj != null) {
                this.zzdm = jArr;
            }
        } else if (this.zzdm != null) {
            jArr = null;
            obj = 1;
        } else {
            jArr = null;
            obj = null;
        }
        if (obj != null) {
            this.zzdm = jArr;
            i2 |= 2;
        }
        if (jSONObject.has("customData")) {
            this.zzp = jSONObject.getJSONObject("customData");
            this.zzj = null;
            i2 |= 2;
        }
        if (jSONObject.has("media")) {
            jSONObject2 = jSONObject.getJSONObject("media");
            MediaInfo mediaInfo = new MediaInfo(jSONObject2);
            if (this.zzdi == null || !(this.zzdi == null || this.zzdi.equals(mediaInfo))) {
                this.zzdi = mediaInfo;
                i2 |= 2;
            }
            if (jSONObject2.has(TtmlNode.TAG_METADATA)) {
                i2 |= 4;
            }
        }
        if (jSONObject.has("currentItemId")) {
            i = jSONObject.getInt("currentItemId");
            if (this.zzed != i) {
                this.zzed = i;
                i2 |= 2;
            }
        }
        i = jSONObject.optInt("preloadedItemId", 0);
        if (this.zzel != i) {
            this.zzel = i;
            i2 |= 16;
        }
        i = jSONObject.optInt("loadingItemId", 0);
        if (this.zzek != i) {
            this.zzek = i;
            i2 |= 2;
        }
        Object obj2 = -1;
        if (zza(this.zzee, this.zzef, this.zzek, this.zzdi == null ? -1 : this.zzdi.getStreamType())) {
            this.zzed = 0;
            this.zzek = 0;
            this.zzel = 0;
            if (!this.zzen.isEmpty()) {
                this.zzem = 0;
                this.zzen.clear();
                this.zzer.clear();
            }
            zzc = AdBreakStatus.zzc(jSONObject.optJSONObject("breakStatus"));
            if (zzc != null) {
                z = true;
            }
            this.zzeo = z;
            this.zzep = zzc;
            i2 |= 32;
            zzg = VideoInfo.zzg(jSONObject.optJSONObject("videoInfo"));
            this.zzeq = zzg;
            i2 |= 64;
            if (!jSONObject.has("breakInfo")) {
            }
        }
        JSONArray jSONArray2;
        SparseArray sparseArray;
        MediaQueueItem[] mediaQueueItemArr;
        Integer num;
        JSONObject jSONObject3;
        MediaQueueItem itemById;
        if (jSONObject.has("repeatMode")) {
            i = this.zzem;
            String string2 = jSONObject.getString("repeatMode");
            i3 = string2.hashCode();
            if (i3 != -1118317585) {
                if (i3 != -962896020) {
                    if (i3 != 1645938909) {
                        if (i3 == 1645952171) {
                            if (string2.equals("REPEAT_OFF")) {
                                obj2 = null;
                            }
                        }
                    } else if (string2.equals("REPEAT_ALL")) {
                        obj2 = 1;
                    }
                } else if (string2.equals("REPEAT_SINGLE")) {
                    obj2 = 2;
                }
            } else if (string2.equals("REPEAT_ALL_AND_SHUFFLE")) {
                obj2 = 3;
            }
            switch (obj2) {
                case null:
                    i = 0;
                    break;
                case 1:
                    i = 1;
                    break;
                case 2:
                    i = 2;
                    break;
                case 3:
                    i = 3;
                    break;
                default:
                    break;
            }
            if (this.zzem != i) {
                this.zzem = i;
                i = 1;
                if (jSONObject.has("items")) {
                    int length2;
                    jSONArray2 = jSONObject.getJSONArray("items");
                    length2 = jSONArray2.length();
                    sparseArray = new SparseArray();
                    for (length = 0; length < length2; length++) {
                        sparseArray.put(length, Integer.valueOf(jSONArray2.getJSONObject(length).getInt("itemId")));
                    }
                    mediaQueueItemArr = new MediaQueueItem[length2];
                    i3 = i;
                    for (i = 0; i < length2; i++) {
                        num = (Integer) sparseArray.get(i);
                        jSONObject3 = jSONArray2.getJSONObject(i);
                        itemById = getItemById(num.intValue());
                        if (itemById == null) {
                            i3 |= itemById.zzf(jSONObject3);
                            mediaQueueItemArr[i] = itemById;
                            if (i == getIndexById(num.intValue()).intValue()) {
                            }
                        } else if (num.intValue() == this.zzed || this.zzdi == null) {
                            mediaQueueItemArr[i] = new MediaQueueItem(jSONObject3);
                        } else {
                            mediaQueueItemArr[i] = new Builder(this.zzdi).build();
                            mediaQueueItemArr[i].zzf(jSONObject3);
                        }
                        i3 = 1;
                    }
                    i = this.zzen.size() == length2 ? 1 : i3;
                    zza(mediaQueueItemArr);
                }
                if (i != 0) {
                }
                zzc = AdBreakStatus.zzc(jSONObject.optJSONObject("breakStatus"));
                if ((this.zzep == null && zzc != null) || !(this.zzep == null || this.zzep.equals(zzc))) {
                    if (zzc != null) {
                        z = true;
                    }
                    this.zzeo = z;
                    this.zzep = zzc;
                    i2 |= 32;
                }
                zzg = VideoInfo.zzg(jSONObject.optJSONObject("videoInfo"));
                if ((this.zzeq == null && zzg != null) || !(this.zzeq == null || this.zzeq.equals(zzg))) {
                    this.zzeq = zzg;
                    i2 |= 64;
                }
                if (!jSONObject.has("breakInfo") && this.zzdi != null) {
                    this.zzdi.zzd(jSONObject.getJSONObject("breakInfo"));
                    return i2 | 2;
                }
            }
        }
        i = 0;
        if (jSONObject.has("items")) {
            jSONArray2 = jSONObject.getJSONArray("items");
            length2 = jSONArray2.length();
            sparseArray = new SparseArray();
            for (length = 0; length < length2; length++) {
                sparseArray.put(length, Integer.valueOf(jSONArray2.getJSONObject(length).getInt("itemId")));
            }
            mediaQueueItemArr = new MediaQueueItem[length2];
            i3 = i;
            while (i < length2) {
                num = (Integer) sparseArray.get(i);
                jSONObject3 = jSONArray2.getJSONObject(i);
                itemById = getItemById(num.intValue());
                if (itemById == null) {
                    if (num.intValue() == this.zzed) {
                    }
                    mediaQueueItemArr[i] = new MediaQueueItem(jSONObject3);
                } else {
                    i3 |= itemById.zzf(jSONObject3);
                    mediaQueueItemArr[i] = itemById;
                    if (i == getIndexById(num.intValue()).intValue()) {
                    }
                }
                i3 = 1;
            }
            if (this.zzen.size() == length2) {
            }
            zza(mediaQueueItemArr);
        }
        if (i != 0) {
        }
        zzc = AdBreakStatus.zzc(jSONObject.optJSONObject("breakStatus"));
        if (zzc != null) {
            z = true;
        }
        this.zzeo = z;
        this.zzep = zzc;
        i2 |= 32;
        zzg = VideoInfo.zzg(jSONObject.optJSONObject("videoInfo"));
        this.zzeq = zzg;
        i2 |= 64;
        return !jSONObject.has("breakInfo") ? i2 : i2;
        i2 |= 8;
        zzc = AdBreakStatus.zzc(jSONObject.optJSONObject("breakStatus"));
        if (zzc != null) {
            z = true;
        }
        this.zzeo = z;
        this.zzep = zzc;
        i2 |= 32;
        zzg = VideoInfo.zzg(jSONObject.optJSONObject("videoInfo"));
        this.zzeq = zzg;
        i2 |= 64;
        if (!jSONObject.has("breakInfo")) {
        }
    }

    public final void zzf(boolean z) {
        this.zzeo = z;
    }

    public final long zzj() {
        return this.zzec;
    }

    public final boolean zzk() {
        return zza(this.zzee, this.zzef, this.zzek, this.zzdi == null ? -1 : this.zzdi.getStreamType());
    }
}
