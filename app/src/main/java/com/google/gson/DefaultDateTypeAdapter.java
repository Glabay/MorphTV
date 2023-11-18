package com.google.gson;

import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

final class DefaultDateTypeAdapter extends TypeAdapter<Date> {
    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
    private final List<DateFormat> dateFormats;
    private final Class<? extends Date> dateType;

    DefaultDateTypeAdapter(Class<? extends Date> cls) {
        this.dateFormats = new ArrayList();
        this.dateType = verifyDateType(cls);
        this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2, Locale.US));
        if (Locale.getDefault().equals(Locale.US) == null) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
        }
        if (JavaVersion.isJava9OrLater() != null) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
        }
    }

    DefaultDateTypeAdapter(Class<? extends Date> cls, String str) {
        this.dateFormats = new ArrayList();
        this.dateType = verifyDateType(cls);
        this.dateFormats.add(new SimpleDateFormat(str, Locale.US));
        if (Locale.getDefault().equals(Locale.US) == null) {
            this.dateFormats.add(new SimpleDateFormat(str));
        }
    }

    DefaultDateTypeAdapter(Class<? extends Date> cls, int i) {
        this.dateFormats = new ArrayList();
        this.dateType = verifyDateType(cls);
        this.dateFormats.add(DateFormat.getDateInstance(i, Locale.US));
        if (Locale.getDefault().equals(Locale.US) == null) {
            this.dateFormats.add(DateFormat.getDateInstance(i));
        }
        if (JavaVersion.isJava9OrLater() != null) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateFormat(i));
        }
    }

    public DefaultDateTypeAdapter(int i, int i2) {
        this(Date.class, i, i2);
    }

    public DefaultDateTypeAdapter(Class<? extends Date> cls, int i, int i2) {
        this.dateFormats = new ArrayList();
        this.dateType = verifyDateType(cls);
        this.dateFormats.add(DateFormat.getDateTimeInstance(i, i2, Locale.US));
        if (Locale.getDefault().equals(Locale.US) == null) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(i, i2));
        }
        if (JavaVersion.isJava9OrLater() != null) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(i, i2));
        }
    }

    private static Class<? extends Date> verifyDateType(Class<? extends Date> cls) {
        if (cls == Date.class || cls == java.sql.Date.class || cls == Timestamp.class) {
            return cls;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Date type must be one of ");
        stringBuilder.append(Date.class);
        stringBuilder.append(", ");
        stringBuilder.append(Timestamp.class);
        stringBuilder.append(", or ");
        stringBuilder.append(java.sql.Date.class);
        stringBuilder.append(" but was ");
        stringBuilder.append(cls);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        if (date == null) {
            jsonWriter.nullValue();
            return;
        }
        synchronized (this.dateFormats) {
            jsonWriter.value(((DateFormat) this.dateFormats.get(0)).format(date));
        }
    }

    public Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader = deserializeToDate(jsonReader.nextString());
        if (this.dateType == Date.class) {
            return jsonReader;
        }
        if (this.dateType == Timestamp.class) {
            return new Timestamp(jsonReader.getTime());
        }
        if (this.dateType == java.sql.Date.class) {
            return new java.sql.Date(jsonReader.getTime());
        }
        throw new AssertionError();
    }

    private java.util.Date deserializeToDate(java.lang.String r4) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r3.dateFormats;
        monitor-enter(r0);
        r1 = r3.dateFormats;	 Catch:{ all -> 0x002e }
        r1 = r1.iterator();	 Catch:{ all -> 0x002e }
    L_0x0009:
        r2 = r1.hasNext();	 Catch:{ all -> 0x002e }
        if (r2 == 0) goto L_0x001b;	 Catch:{ all -> 0x002e }
    L_0x000f:
        r2 = r1.next();	 Catch:{ all -> 0x002e }
        r2 = (java.text.DateFormat) r2;	 Catch:{ all -> 0x002e }
        r2 = r2.parse(r4);	 Catch:{ ParseException -> 0x0009 }
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        return r2;
    L_0x001b:
        r1 = new java.text.ParsePosition;	 Catch:{ ParseException -> 0x0027 }
        r2 = 0;	 Catch:{ ParseException -> 0x0027 }
        r1.<init>(r2);	 Catch:{ ParseException -> 0x0027 }
        r1 = com.google.gson.internal.bind.util.ISO8601Utils.parse(r4, r1);	 Catch:{ ParseException -> 0x0027 }
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        return r1;	 Catch:{ all -> 0x002e }
    L_0x0027:
        r1 = move-exception;	 Catch:{ all -> 0x002e }
        r2 = new com.google.gson.JsonSyntaxException;	 Catch:{ all -> 0x002e }
        r2.<init>(r4, r1);	 Catch:{ all -> 0x002e }
        throw r2;	 Catch:{ all -> 0x002e }
    L_0x002e:
        r4 = move-exception;	 Catch:{ all -> 0x002e }
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.DefaultDateTypeAdapter.deserializeToDate(java.lang.String):java.util.Date");
    }

    public String toString() {
        DateFormat dateFormat = (DateFormat) this.dateFormats.get(0);
        if (dateFormat instanceof SimpleDateFormat) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DefaultDateTypeAdapter(");
            stringBuilder.append(((SimpleDateFormat) dateFormat).toPattern());
            stringBuilder.append(')');
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("DefaultDateTypeAdapter(");
        stringBuilder.append(dateFormat.getClass().getSimpleName());
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
