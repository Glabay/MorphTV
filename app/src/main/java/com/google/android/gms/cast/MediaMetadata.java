package com.google.android.gms.cast;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.internal.cast.zzdp;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Class(creator = "MediaMetadataCreator")
@Reserved({1})
public class MediaMetadata extends AbstractSafeParcelable {
    public static final Creator<MediaMetadata> CREATOR = new zzak();
    public static final String KEY_ALBUM_ARTIST = "com.google.android.gms.cast.metadata.ALBUM_ARTIST";
    public static final String KEY_ALBUM_TITLE = "com.google.android.gms.cast.metadata.ALBUM_TITLE";
    public static final String KEY_ARTIST = "com.google.android.gms.cast.metadata.ARTIST";
    public static final String KEY_BROADCAST_DATE = "com.google.android.gms.cast.metadata.BROADCAST_DATE";
    public static final String KEY_COMPOSER = "com.google.android.gms.cast.metadata.COMPOSER";
    public static final String KEY_CREATION_DATE = "com.google.android.gms.cast.metadata.CREATION_DATE";
    public static final String KEY_DISC_NUMBER = "com.google.android.gms.cast.metadata.DISC_NUMBER";
    public static final String KEY_EPISODE_NUMBER = "com.google.android.gms.cast.metadata.EPISODE_NUMBER";
    public static final String KEY_HEIGHT = "com.google.android.gms.cast.metadata.HEIGHT";
    public static final String KEY_LOCATION_LATITUDE = "com.google.android.gms.cast.metadata.LOCATION_LATITUDE";
    public static final String KEY_LOCATION_LONGITUDE = "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE";
    public static final String KEY_LOCATION_NAME = "com.google.android.gms.cast.metadata.LOCATION_NAME";
    public static final String KEY_RELEASE_DATE = "com.google.android.gms.cast.metadata.RELEASE_DATE";
    public static final String KEY_SEASON_NUMBER = "com.google.android.gms.cast.metadata.SEASON_NUMBER";
    public static final String KEY_SERIES_TITLE = "com.google.android.gms.cast.metadata.SERIES_TITLE";
    public static final String KEY_STUDIO = "com.google.android.gms.cast.metadata.STUDIO";
    public static final String KEY_SUBTITLE = "com.google.android.gms.cast.metadata.SUBTITLE";
    public static final String KEY_TITLE = "com.google.android.gms.cast.metadata.TITLE";
    public static final String KEY_TRACK_NUMBER = "com.google.android.gms.cast.metadata.TRACK_NUMBER";
    public static final String KEY_WIDTH = "com.google.android.gms.cast.metadata.WIDTH";
    public static final int MEDIA_TYPE_GENERIC = 0;
    public static final int MEDIA_TYPE_MOVIE = 1;
    public static final int MEDIA_TYPE_MUSIC_TRACK = 3;
    public static final int MEDIA_TYPE_PHOTO = 4;
    public static final int MEDIA_TYPE_TV_SHOW = 2;
    public static final int MEDIA_TYPE_USER = 100;
    private static final String[] zzdp = new String[]{null, "String", SerializerHandler.TYPE_INT, SerializerHandler.TYPE_DOUBLE, "ISO-8601 date String"};
    private static final zza zzdq = new zza().zza(KEY_CREATION_DATE, "creationDateTime", 4).zza(KEY_RELEASE_DATE, "releaseDate", 4).zza(KEY_BROADCAST_DATE, "originalAirdate", 4).zza(KEY_TITLE, "title", 1).zza(KEY_SUBTITLE, "subtitle", 1).zza(KEY_ARTIST, "artist", 1).zza(KEY_ALBUM_ARTIST, "albumArtist", 1).zza(KEY_ALBUM_TITLE, "albumName", 1).zza(KEY_COMPOSER, "composer", 1).zza(KEY_DISC_NUMBER, "discNumber", 2).zza(KEY_TRACK_NUMBER, "trackNumber", 2).zza(KEY_SEASON_NUMBER, "season", 2).zza(KEY_EPISODE_NUMBER, "episode", 2).zza(KEY_SERIES_TITLE, "seriesTitle", 1).zza(KEY_STUDIO, "studio", 1).zza(KEY_WIDTH, "width", 2).zza(KEY_HEIGHT, "height", 2).zza(KEY_LOCATION_NAME, "location", 1).zza(KEY_LOCATION_LATITUDE, "latitude", 3).zza(KEY_LOCATION_LONGITUDE, "longitude", 3);
    @Field(id = 3)
    private final Bundle zzdr;
    @Field(getter = "getMediaType", id = 4)
    private int zzds;
    @Field(getter = "getImages", id = 2)
    private final List<WebImage> zzz;

    private static class zza {
        private final Map<String, String> zzdt = new HashMap();
        private final Map<String, String> zzdu = new HashMap();
        private final Map<String, Integer> zzdv = new HashMap();

        public final zza zza(String str, String str2, int i) {
            this.zzdt.put(str, str2);
            this.zzdu.put(str2, str);
            this.zzdv.put(str, Integer.valueOf(i));
            return this;
        }

        public final String zze(String str) {
            return (String) this.zzdt.get(str);
        }

        public final String zzf(String str) {
            return (String) this.zzdu.get(str);
        }

        public final int zzg(String str) {
            Integer num = (Integer) this.zzdv.get(str);
            return num != null ? num.intValue() : 0;
        }
    }

    public MediaMetadata() {
        this(0);
    }

    public MediaMetadata(int i) {
        this(new ArrayList(), new Bundle(), i);
    }

    @Constructor
    MediaMetadata(@Param(id = 2) List<WebImage> list, @Param(id = 3) Bundle bundle, @Param(id = 4) int i) {
        this.zzz = list;
        this.zzdr = bundle;
        this.zzds = i;
    }

    private static void zza(String str, int i) throws IllegalArgumentException {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("null and empty keys are not allowed");
        }
        int zzg = zzdq.zzg(str);
        if (zzg != i && zzg != 0) {
            String str2 = zzdp[i];
            StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 21) + String.valueOf(str2).length());
            stringBuilder.append("Value for ");
            stringBuilder.append(str);
            stringBuilder.append(" must be a ");
            stringBuilder.append(str2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private final void zza(org.json.JSONObject r7, java.lang.String... r8) {
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
        r6 = this;
        r0 = r8.length;	 Catch:{ JSONException -> 0x0080 }
        r1 = 0;	 Catch:{ JSONException -> 0x0080 }
    L_0x0002:
        if (r1 >= r0) goto L_0x004a;	 Catch:{ JSONException -> 0x0080 }
    L_0x0004:
        r2 = r8[r1];	 Catch:{ JSONException -> 0x0080 }
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x0080 }
        r3 = r3.containsKey(r2);	 Catch:{ JSONException -> 0x0080 }
        if (r3 == 0) goto L_0x0047;	 Catch:{ JSONException -> 0x0080 }
    L_0x000e:
        r3 = zzdq;	 Catch:{ JSONException -> 0x0080 }
        r3 = r3.zzg(r2);	 Catch:{ JSONException -> 0x0080 }
        switch(r3) {
            case 1: goto L_0x0038;
            case 2: goto L_0x0028;
            case 3: goto L_0x0018;
            case 4: goto L_0x0038;
            default: goto L_0x0017;
        };	 Catch:{ JSONException -> 0x0080 }
    L_0x0017:
        goto L_0x0047;	 Catch:{ JSONException -> 0x0080 }
    L_0x0018:
        r3 = zzdq;	 Catch:{ JSONException -> 0x0080 }
        r3 = r3.zze(r2);	 Catch:{ JSONException -> 0x0080 }
        r4 = r6.zzdr;	 Catch:{ JSONException -> 0x0080 }
        r4 = r4.getDouble(r2);	 Catch:{ JSONException -> 0x0080 }
        r7.put(r3, r4);	 Catch:{ JSONException -> 0x0080 }
        goto L_0x0047;	 Catch:{ JSONException -> 0x0080 }
    L_0x0028:
        r3 = zzdq;	 Catch:{ JSONException -> 0x0080 }
        r3 = r3.zze(r2);	 Catch:{ JSONException -> 0x0080 }
        r4 = r6.zzdr;	 Catch:{ JSONException -> 0x0080 }
        r2 = r4.getInt(r2);	 Catch:{ JSONException -> 0x0080 }
        r7.put(r3, r2);	 Catch:{ JSONException -> 0x0080 }
        goto L_0x0047;	 Catch:{ JSONException -> 0x0080 }
    L_0x0038:
        r3 = zzdq;	 Catch:{ JSONException -> 0x0080 }
        r3 = r3.zze(r2);	 Catch:{ JSONException -> 0x0080 }
        r4 = r6.zzdr;	 Catch:{ JSONException -> 0x0080 }
        r2 = r4.getString(r2);	 Catch:{ JSONException -> 0x0080 }
        r7.put(r3, r2);	 Catch:{ JSONException -> 0x0080 }
    L_0x0047:
        r1 = r1 + 1;	 Catch:{ JSONException -> 0x0080 }
        goto L_0x0002;	 Catch:{ JSONException -> 0x0080 }
    L_0x004a:
        r8 = r6.zzdr;	 Catch:{ JSONException -> 0x0080 }
        r8 = r8.keySet();	 Catch:{ JSONException -> 0x0080 }
        r8 = r8.iterator();	 Catch:{ JSONException -> 0x0080 }
    L_0x0054:
        r0 = r8.hasNext();	 Catch:{ JSONException -> 0x0080 }
        if (r0 == 0) goto L_0x0080;	 Catch:{ JSONException -> 0x0080 }
    L_0x005a:
        r0 = r8.next();	 Catch:{ JSONException -> 0x0080 }
        r0 = (java.lang.String) r0;	 Catch:{ JSONException -> 0x0080 }
        r1 = "com.google.";	 Catch:{ JSONException -> 0x0080 }
        r1 = r0.startsWith(r1);	 Catch:{ JSONException -> 0x0080 }
        if (r1 != 0) goto L_0x0054;	 Catch:{ JSONException -> 0x0080 }
    L_0x0068:
        r1 = r6.zzdr;	 Catch:{ JSONException -> 0x0080 }
        r1 = r1.get(r0);	 Catch:{ JSONException -> 0x0080 }
        r2 = r1 instanceof java.lang.String;	 Catch:{ JSONException -> 0x0080 }
        if (r2 == 0) goto L_0x0076;	 Catch:{ JSONException -> 0x0080 }
    L_0x0072:
        r7.put(r0, r1);	 Catch:{ JSONException -> 0x0080 }
        goto L_0x0054;	 Catch:{ JSONException -> 0x0080 }
    L_0x0076:
        r2 = r1 instanceof java.lang.Integer;	 Catch:{ JSONException -> 0x0080 }
        if (r2 == 0) goto L_0x007b;	 Catch:{ JSONException -> 0x0080 }
    L_0x007a:
        goto L_0x0072;	 Catch:{ JSONException -> 0x0080 }
    L_0x007b:
        r2 = r1 instanceof java.lang.Double;	 Catch:{ JSONException -> 0x0080 }
        if (r2 == 0) goto L_0x0054;
    L_0x007f:
        goto L_0x0072;
    L_0x0080:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaMetadata.zza(org.json.JSONObject, java.lang.String[]):void");
    }

    private final boolean zza(Bundle bundle, Bundle bundle2) {
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            Object obj2 = bundle2.get(str);
            if ((obj instanceof Bundle) && (obj2 instanceof Bundle) && !zza((Bundle) obj, (Bundle) obj2)) {
                return false;
            }
            if (obj == null) {
                if (obj2 != null || !bundle2.containsKey(str)) {
                    return false;
                }
            } else if (!obj.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzb(org.json.JSONObject r7, java.lang.String... r8) {
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
        r6 = this;
        r0 = new java.util.HashSet;
        r8 = java.util.Arrays.asList(r8);
        r0.<init>(r8);
        r8 = r7.keys();	 Catch:{ JSONException -> 0x00ae }
    L_0x000d:
        r1 = r8.hasNext();	 Catch:{ JSONException -> 0x00ae }
        if (r1 == 0) goto L_0x00ae;	 Catch:{ JSONException -> 0x00ae }
    L_0x0013:
        r1 = r8.next();	 Catch:{ JSONException -> 0x00ae }
        r1 = (java.lang.String) r1;	 Catch:{ JSONException -> 0x00ae }
        r2 = "metadataType";	 Catch:{ JSONException -> 0x00ae }
        r2 = r2.equals(r1);	 Catch:{ JSONException -> 0x00ae }
        if (r2 != 0) goto L_0x000d;	 Catch:{ JSONException -> 0x00ae }
    L_0x0021:
        r2 = zzdq;	 Catch:{ JSONException -> 0x00ae }
        r2 = r2.zzf(r1);	 Catch:{ JSONException -> 0x00ae }
        if (r2 == 0) goto L_0x007c;	 Catch:{ JSONException -> 0x00ae }
    L_0x0029:
        r3 = r0.contains(r2);	 Catch:{ JSONException -> 0x00ae }
        if (r3 == 0) goto L_0x000d;
    L_0x002f:
        r1 = r7.get(r1);	 Catch:{ JSONException -> 0x000d }
        if (r1 != 0) goto L_0x0036;	 Catch:{ JSONException -> 0x000d }
    L_0x0035:
        goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0036:
        r3 = zzdq;	 Catch:{ JSONException -> 0x000d }
        r3 = r3.zzg(r2);	 Catch:{ JSONException -> 0x000d }
        switch(r3) {
            case 1: goto L_0x0075;
            case 2: goto L_0x0065;
            case 3: goto L_0x0055;
            case 4: goto L_0x0040;
            default: goto L_0x003f;
        };	 Catch:{ JSONException -> 0x000d }
    L_0x003f:
        goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0040:
        r3 = r1 instanceof java.lang.String;	 Catch:{ JSONException -> 0x000d }
        if (r3 == 0) goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0044:
        r3 = r1;	 Catch:{ JSONException -> 0x000d }
        r3 = (java.lang.String) r3;	 Catch:{ JSONException -> 0x000d }
        r3 = com.google.android.gms.internal.cast.zzdp.zzu(r3);	 Catch:{ JSONException -> 0x000d }
        if (r3 == 0) goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x004d:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x000d }
    L_0x004f:
        r1 = (java.lang.String) r1;	 Catch:{ JSONException -> 0x000d }
        r3.putString(r2, r1);	 Catch:{ JSONException -> 0x000d }
        goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0055:
        r3 = r1 instanceof java.lang.Double;	 Catch:{ JSONException -> 0x000d }
        if (r3 == 0) goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0059:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x000d }
        r1 = (java.lang.Double) r1;	 Catch:{ JSONException -> 0x000d }
        r4 = r1.doubleValue();	 Catch:{ JSONException -> 0x000d }
        r3.putDouble(r2, r4);	 Catch:{ JSONException -> 0x000d }
        goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0065:
        r3 = r1 instanceof java.lang.Integer;	 Catch:{ JSONException -> 0x000d }
        if (r3 == 0) goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0069:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x000d }
        r1 = (java.lang.Integer) r1;	 Catch:{ JSONException -> 0x000d }
        r1 = r1.intValue();	 Catch:{ JSONException -> 0x000d }
        r3.putInt(r2, r1);	 Catch:{ JSONException -> 0x000d }
        goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0075:
        r3 = r1 instanceof java.lang.String;	 Catch:{ JSONException -> 0x000d }
        if (r3 == 0) goto L_0x000d;	 Catch:{ JSONException -> 0x000d }
    L_0x0079:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x000d }
        goto L_0x004f;
    L_0x007c:
        r2 = r7.get(r1);	 Catch:{ JSONException -> 0x00ae }
        r3 = r2 instanceof java.lang.String;	 Catch:{ JSONException -> 0x00ae }
        if (r3 == 0) goto L_0x008c;	 Catch:{ JSONException -> 0x00ae }
    L_0x0084:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x00ae }
        r2 = (java.lang.String) r2;	 Catch:{ JSONException -> 0x00ae }
        r3.putString(r1, r2);	 Catch:{ JSONException -> 0x00ae }
        goto L_0x000d;	 Catch:{ JSONException -> 0x00ae }
    L_0x008c:
        r3 = r2 instanceof java.lang.Integer;	 Catch:{ JSONException -> 0x00ae }
        if (r3 == 0) goto L_0x009d;	 Catch:{ JSONException -> 0x00ae }
    L_0x0090:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x00ae }
        r2 = (java.lang.Integer) r2;	 Catch:{ JSONException -> 0x00ae }
        r2 = r2.intValue();	 Catch:{ JSONException -> 0x00ae }
        r3.putInt(r1, r2);	 Catch:{ JSONException -> 0x00ae }
        goto L_0x000d;	 Catch:{ JSONException -> 0x00ae }
    L_0x009d:
        r3 = r2 instanceof java.lang.Double;	 Catch:{ JSONException -> 0x00ae }
        if (r3 == 0) goto L_0x000d;	 Catch:{ JSONException -> 0x00ae }
    L_0x00a1:
        r3 = r6.zzdr;	 Catch:{ JSONException -> 0x00ae }
        r2 = (java.lang.Double) r2;	 Catch:{ JSONException -> 0x00ae }
        r4 = r2.doubleValue();	 Catch:{ JSONException -> 0x00ae }
        r3.putDouble(r1, r4);	 Catch:{ JSONException -> 0x00ae }
        goto L_0x000d;
    L_0x00ae:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaMetadata.zzb(org.json.JSONObject, java.lang.String[]):void");
    }

    public void addImage(WebImage webImage) {
        this.zzz.add(webImage);
    }

    public void clear() {
        this.zzdr.clear();
        this.zzz.clear();
    }

    public void clearImages() {
        this.zzz.clear();
    }

    public boolean containsKey(String str) {
        return this.zzdr.containsKey(str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaMetadata)) {
            return false;
        }
        MediaMetadata mediaMetadata = (MediaMetadata) obj;
        return zza(this.zzdr, mediaMetadata.zzdr) && this.zzz.equals(mediaMetadata.zzz);
    }

    public Calendar getDate(String str) {
        zza(str, 4);
        str = this.zzdr.getString(str);
        return str != null ? zzdp.zzu(str) : null;
    }

    public String getDateAsString(String str) {
        zza(str, 4);
        return this.zzdr.getString(str);
    }

    public double getDouble(String str) {
        zza(str, 3);
        return this.zzdr.getDouble(str);
    }

    public List<WebImage> getImages() {
        return this.zzz;
    }

    public int getInt(String str) {
        zza(str, 2);
        return this.zzdr.getInt(str);
    }

    public int getMediaType() {
        return this.zzds;
    }

    public String getString(String str) {
        zza(str, 1);
        return this.zzdr.getString(str);
    }

    public boolean hasImages() {
        return (this.zzz == null || this.zzz.isEmpty()) ? false : true;
    }

    public int hashCode() {
        int i = 17;
        for (String str : this.zzdr.keySet()) {
            i = (i * 31) + this.zzdr.get(str).hashCode();
        }
        return (i * 31) + this.zzz.hashCode();
    }

    public Set<String> keySet() {
        return this.zzdr.keySet();
    }

    public void putDate(String str, Calendar calendar) {
        zza(str, 4);
        this.zzdr.putString(str, zzdp.zza(calendar));
    }

    public void putDouble(String str, double d) {
        zza(str, 3);
        this.zzdr.putDouble(str, d);
    }

    public void putInt(String str, int i) {
        zza(str, 2);
        this.zzdr.putInt(str, i);
    }

    public void putString(String str, String str2) {
        zza(str, 1);
        this.zzdr.putString(str, str2);
    }

    public final org.json.JSONObject toJson() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x0064 in {2, 5, 7, 8, 9, 10, 11, 12, 13, 14} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
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
        r10 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "metadataType";	 Catch:{ JSONException -> 0x000c }
        r2 = r10.zzds;	 Catch:{ JSONException -> 0x000c }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x000c }
    L_0x000c:
        r1 = r10.zzz;
        com.google.android.gms.internal.cast.zzdp.zza(r0, r1);
        r1 = r10.zzds;
        switch(r1) {
            case 0: goto L_0x0057;
            case 1: goto L_0x0052;
            case 2: goto L_0x0043;
            case 3: goto L_0x002e;
            case 4: goto L_0x001d;
            default: goto L_0x0016;
        };
    L_0x0016:
        r1 = 0;
        r1 = new java.lang.String[r1];
    L_0x0019:
        r10.zza(r0, r1);
        return r0;
    L_0x001d:
        r2 = "com.google.android.gms.cast.metadata.TITLE";
        r3 = "com.google.android.gms.cast.metadata.ARTIST";
        r4 = "com.google.android.gms.cast.metadata.LOCATION_NAME";
        r5 = "com.google.android.gms.cast.metadata.LOCATION_LATITUDE";
        r6 = "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE";
        r7 = "com.google.android.gms.cast.metadata.WIDTH";
        r8 = "com.google.android.gms.cast.metadata.HEIGHT";
        r9 = "com.google.android.gms.cast.metadata.CREATION_DATE";
        goto L_0x003e;
    L_0x002e:
        r2 = "com.google.android.gms.cast.metadata.TITLE";
        r3 = "com.google.android.gms.cast.metadata.ARTIST";
        r4 = "com.google.android.gms.cast.metadata.ALBUM_TITLE";
        r5 = "com.google.android.gms.cast.metadata.ALBUM_ARTIST";
        r6 = "com.google.android.gms.cast.metadata.COMPOSER";
        r7 = "com.google.android.gms.cast.metadata.TRACK_NUMBER";
        r8 = "com.google.android.gms.cast.metadata.DISC_NUMBER";
        r9 = "com.google.android.gms.cast.metadata.RELEASE_DATE";
    L_0x003e:
        r1 = new java.lang.String[]{r2, r3, r4, r5, r6, r7, r8, r9};
        goto L_0x0019;
    L_0x0043:
        r1 = "com.google.android.gms.cast.metadata.TITLE";
        r2 = "com.google.android.gms.cast.metadata.SERIES_TITLE";
        r3 = "com.google.android.gms.cast.metadata.SEASON_NUMBER";
        r4 = "com.google.android.gms.cast.metadata.EPISODE_NUMBER";
        r5 = "com.google.android.gms.cast.metadata.BROADCAST_DATE";
        r1 = new java.lang.String[]{r1, r2, r3, r4, r5};
        goto L_0x0019;
    L_0x0052:
        r1 = "com.google.android.gms.cast.metadata.TITLE";
        r2 = "com.google.android.gms.cast.metadata.STUDIO";
        goto L_0x005b;
    L_0x0057:
        r1 = "com.google.android.gms.cast.metadata.TITLE";
        r2 = "com.google.android.gms.cast.metadata.ARTIST";
    L_0x005b:
        r3 = "com.google.android.gms.cast.metadata.SUBTITLE";
        r4 = "com.google.android.gms.cast.metadata.RELEASE_DATE";
        r1 = new java.lang.String[]{r1, r2, r3, r4};
        goto L_0x0019;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaMetadata.toJson():org.json.JSONObject");
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeTypedList(parcel, 2, getImages(), false);
        SafeParcelWriter.writeBundle(parcel, 3, this.zzdr, false);
        SafeParcelWriter.writeInt(parcel, 4, getMediaType());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public final void zze(org.json.JSONObject r11) {
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
        r10 = this;
        r10.clear();
        r0 = 0;
        r10.zzds = r0;
        r1 = "metadataType";	 Catch:{ JSONException -> 0x000e }
        r1 = r11.getInt(r1);	 Catch:{ JSONException -> 0x000e }
        r10.zzds = r1;	 Catch:{ JSONException -> 0x000e }
    L_0x000e:
        r1 = r10.zzz;
        com.google.android.gms.internal.cast.zzdp.zza(r1, r11);
        r1 = r10.zzds;
        switch(r1) {
            case 0: goto L_0x0064;
            case 1: goto L_0x0057;
            case 2: goto L_0x0048;
            case 3: goto L_0x0033;
            case 4: goto L_0x001e;
            default: goto L_0x0018;
        };
    L_0x0018:
        r0 = new java.lang.String[r0];
    L_0x001a:
        r10.zzb(r11, r0);
        return;
    L_0x001e:
        r2 = "com.google.android.gms.cast.metadata.TITLE";
        r3 = "com.google.android.gms.cast.metadata.ARTIST";
        r4 = "com.google.android.gms.cast.metadata.LOCATION_NAME";
        r5 = "com.google.android.gms.cast.metadata.LOCATION_LATITUDE";
        r6 = "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE";
        r7 = "com.google.android.gms.cast.metadata.WIDTH";
        r8 = "com.google.android.gms.cast.metadata.HEIGHT";
        r9 = "com.google.android.gms.cast.metadata.CREATION_DATE";
        r0 = new java.lang.String[]{r2, r3, r4, r5, r6, r7, r8, r9};
        goto L_0x001a;
    L_0x0033:
        r1 = "com.google.android.gms.cast.metadata.TITLE";
        r2 = "com.google.android.gms.cast.metadata.ALBUM_TITLE";
        r3 = "com.google.android.gms.cast.metadata.ARTIST";
        r4 = "com.google.android.gms.cast.metadata.ALBUM_ARTIST";
        r5 = "com.google.android.gms.cast.metadata.COMPOSER";
        r6 = "com.google.android.gms.cast.metadata.TRACK_NUMBER";
        r7 = "com.google.android.gms.cast.metadata.DISC_NUMBER";
        r8 = "com.google.android.gms.cast.metadata.RELEASE_DATE";
        r0 = new java.lang.String[]{r1, r2, r3, r4, r5, r6, r7, r8};
        goto L_0x001a;
    L_0x0048:
        r0 = "com.google.android.gms.cast.metadata.TITLE";
        r1 = "com.google.android.gms.cast.metadata.SERIES_TITLE";
        r2 = "com.google.android.gms.cast.metadata.SEASON_NUMBER";
        r3 = "com.google.android.gms.cast.metadata.EPISODE_NUMBER";
        r4 = "com.google.android.gms.cast.metadata.BROADCAST_DATE";
        r0 = new java.lang.String[]{r0, r1, r2, r3, r4};
        goto L_0x001a;
    L_0x0057:
        r0 = "com.google.android.gms.cast.metadata.TITLE";
        r1 = "com.google.android.gms.cast.metadata.STUDIO";
    L_0x005b:
        r2 = "com.google.android.gms.cast.metadata.SUBTITLE";
        r3 = "com.google.android.gms.cast.metadata.RELEASE_DATE";
        r0 = new java.lang.String[]{r0, r1, r2, r3};
        goto L_0x001a;
    L_0x0064:
        r0 = "com.google.android.gms.cast.metadata.TITLE";
        r1 = "com.google.android.gms.cast.metadata.ARTIST";
        goto L_0x005b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaMetadata.zze(org.json.JSONObject):void");
    }
}
