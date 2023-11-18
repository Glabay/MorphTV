package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new C12971();
    private final List<DateFormat> dateFormats = new ArrayList();

    /* renamed from: com.google.gson.internal.bind.DateTypeAdapter$1 */
    static class C12971 implements TypeAdapterFactory {
        C12971() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
        }
    }

    public DateTypeAdapter() {
        this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
        }
    }

    public Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() != JsonToken.NULL) {
            return deserializeToDate(jsonReader.nextString());
        }
        jsonReader.nextNull();
        return null;
    }

    private synchronized java.util.Date deserializeToDate(java.lang.String r3) {
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
        r2 = this;
        monitor-enter(r2);
        r0 = r2.dateFormats;	 Catch:{ all -> 0x002c }
        r0 = r0.iterator();	 Catch:{ all -> 0x002c }
    L_0x0007:
        r1 = r0.hasNext();	 Catch:{ all -> 0x002c }
        if (r1 == 0) goto L_0x0019;	 Catch:{ all -> 0x002c }
    L_0x000d:
        r1 = r0.next();	 Catch:{ all -> 0x002c }
        r1 = (java.text.DateFormat) r1;	 Catch:{ all -> 0x002c }
        r1 = r1.parse(r3);	 Catch:{ ParseException -> 0x0007 }
        monitor-exit(r2);
        return r1;
    L_0x0019:
        r0 = new java.text.ParsePosition;	 Catch:{ ParseException -> 0x0025 }
        r1 = 0;	 Catch:{ ParseException -> 0x0025 }
        r0.<init>(r1);	 Catch:{ ParseException -> 0x0025 }
        r0 = com.google.gson.internal.bind.util.ISO8601Utils.parse(r3, r0);	 Catch:{ ParseException -> 0x0025 }
        monitor-exit(r2);
        return r0;
    L_0x0025:
        r0 = move-exception;
        r1 = new com.google.gson.JsonSyntaxException;	 Catch:{ all -> 0x002c }
        r1.<init>(r3, r0);	 Catch:{ all -> 0x002c }
        throw r1;	 Catch:{ all -> 0x002c }
    L_0x002c:
        r3 = move-exception;
        monitor-exit(r2);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.DateTypeAdapter.deserializeToDate(java.lang.String):java.util.Date");
    }

    public synchronized void write(JsonWriter jsonWriter, Date date) throws IOException {
        if (date == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(((DateFormat) this.dateFormats.get(0)).format(date));
        }
    }
}
