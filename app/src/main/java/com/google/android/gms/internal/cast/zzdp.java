package com.google.android.gms.internal.cast;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class zzdp {
    private static final zzdg zzbd = new zzdg("MetadataUtils");
    private static final String[] zzxq = new String[]{"Z", "+hh", "+hhmm", "+hh:mm"};
    private static final String zzxr;

    static {
        String valueOf = String.valueOf("yyyyMMdd'T'HHmmss");
        String valueOf2 = String.valueOf(zzxq[0]);
        zzxr = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }

    public static String zza(Calendar calendar) {
        if (calendar == null) {
            zzbd.m25d("Calendar object cannot be null", new Object[0]);
            return null;
        }
        String str = zzxr;
        if (calendar.get(11) == 0 && calendar.get(12) == 0 && calendar.get(13) == 0) {
            str = "yyyyMMdd";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        simpleDateFormat.setTimeZone(calendar.getTimeZone());
        String format = simpleDateFormat.format(calendar.getTime());
        if (format.endsWith("+0000")) {
            format = format.replace("+0000", zzxq[0]);
        }
        return format;
    }

    public static void zza(java.util.List<com.google.android.gms.common.images.WebImage> r4, org.json.JSONObject r5) {
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
        r4.clear();	 Catch:{ JSONException -> 0x001f }
        r0 = "images";	 Catch:{ JSONException -> 0x001f }
        r5 = r5.getJSONArray(r0);	 Catch:{ JSONException -> 0x001f }
        r0 = r5.length();	 Catch:{ JSONException -> 0x001f }
        r1 = 0;	 Catch:{ JSONException -> 0x001f }
    L_0x000e:
        if (r1 >= r0) goto L_0x001f;	 Catch:{ JSONException -> 0x001f }
    L_0x0010:
        r2 = r5.getJSONObject(r1);	 Catch:{ JSONException -> 0x001f }
        r3 = new com.google.android.gms.common.images.WebImage;	 Catch:{ IllegalArgumentException -> 0x001c }
        r3.<init>(r2);	 Catch:{ IllegalArgumentException -> 0x001c }
        r4.add(r3);	 Catch:{ IllegalArgumentException -> 0x001c }
    L_0x001c:
        r1 = r1 + 1;
        goto L_0x000e;
    L_0x001f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdp.zza(java.util.List, org.json.JSONObject):void");
    }

    public static void zza(org.json.JSONObject r2, java.util.List<com.google.android.gms.common.images.WebImage> r3) {
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
        if (r3 == 0) goto L_0x002a;
    L_0x0002:
        r0 = r3.isEmpty();
        if (r0 != 0) goto L_0x002a;
    L_0x0008:
        r0 = new org.json.JSONArray;
        r0.<init>();
        r3 = r3.iterator();
    L_0x0011:
        r1 = r3.hasNext();
        if (r1 == 0) goto L_0x0025;
    L_0x0017:
        r1 = r3.next();
        r1 = (com.google.android.gms.common.images.WebImage) r1;
        r1 = r1.toJson();
        r0.put(r1);
        goto L_0x0011;
    L_0x0025:
        r3 = "images";	 Catch:{ JSONException -> 0x002a }
        r2.put(r3, r0);	 Catch:{ JSONException -> 0x002a }
    L_0x002a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdp.zza(org.json.JSONObject, java.util.List):void");
    }

    public static Calendar zzu(String str) {
        if (TextUtils.isEmpty(str)) {
            zzbd.m25d("Input string is empty or null", new Object[0]);
            return null;
        }
        String zzv = zzv(str);
        if (TextUtils.isEmpty(zzv)) {
            zzbd.m25d("Invalid date format", new Object[0]);
            return null;
        }
        str = zzw(str);
        String str2 = "yyyyMMdd";
        if (!TextUtils.isEmpty(str)) {
            zzv = String.valueOf(zzv);
            StringBuilder stringBuilder = new StringBuilder((String.valueOf(zzv).length() + 1) + String.valueOf(str).length());
            stringBuilder.append(zzv);
            stringBuilder.append("T");
            stringBuilder.append(str);
            zzv = stringBuilder.toString();
            str2 = str.length() == 6 ? "yyyyMMdd'T'HHmmss" : zzxr;
        }
        Calendar instance = GregorianCalendar.getInstance();
        try {
            instance.setTime(new SimpleDateFormat(str2).parse(zzv));
            return instance;
        } catch (ParseException e) {
            zzbd.m25d("Error parsing string: %s", e.getMessage());
            return null;
        }
    }

    private static String zzv(String str) {
        if (TextUtils.isEmpty(str)) {
            zzbd.m25d("Input string is empty or null", new Object[0]);
            return null;
        }
        try {
            return str.substring(0, 8);
        } catch (IndexOutOfBoundsException e) {
            zzbd.m27i("Error extracting the date: %s", e.getMessage());
            return null;
        }
    }

    private static String zzw(String str) {
        if (TextUtils.isEmpty(str)) {
            zzbd.m25d("string is empty or null", new Object[0]);
            return null;
        }
        int indexOf = str.indexOf(84);
        int i = indexOf + 1;
        if (indexOf != 8) {
            zzbd.m25d("T delimeter is not found", new Object[0]);
            return null;
        }
        indexOf = 1;
        try {
            str = str.substring(i);
            if (str.length() == 6) {
                return str;
            }
            char charAt = str.charAt(6);
            if (charAt == '+' || charAt == '-') {
                i = str.length();
                if (!(i == zzxq[1].length() + 6 || i == zzxq[2].length() + 6)) {
                    if (i != zzxq[3].length() + 6) {
                        indexOf = 0;
                    }
                }
                return indexOf != 0 ? str.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2") : null;
            } else if (charAt != 'Z' || str.length() != zzxq[0].length() + 6) {
                return null;
            } else {
                str = String.valueOf(str.substring(0, str.length() - 1));
                String valueOf = String.valueOf("+0000");
                return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
            }
        } catch (IndexOutOfBoundsException e) {
            zzbd.m25d("Error extracting the time substring: %s", e.getMessage());
            return null;
        }
    }
}
