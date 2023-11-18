package io.github.morpheustv.scrapers.helper;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.google.common.net.HttpHeaders;
import com.uwetrottmann.trakt5.TraktV2;
import io.github.morpheustv.scrapers.model.BaseProvider;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class Tmdb {
    private static String api_key = "e630fd9a85cfd7b8dd07cb41aabb6236";
    private static boolean waiting;

    private static Response request(String str, int i) {
        try {
            Connection ignoreHttpErrors = Jsoup.connect(str).header(HttpHeaders.ACCEPT, TraktV2.CONTENT_TYPE_JSON).validateTLSCertificates(false).userAgent(BaseProvider.UserAgent).ignoreContentType(true).ignoreHttpErrors(true);
            while (waiting) {
                Thread.sleep(10);
            }
            Response execute = ignoreHttpErrors.execute();
            if (execute != null) {
                if (execute.statusCode() == 429 && i < 3) {
                    try {
                        System.out.println("TMDB Request Limit reached, waiting for retry...");
                        waiting = true;
                        int i2 = 10;
                        try {
                            i2 = Integer.parseInt(execute.header(HttpHeaders.RETRY_AFTER)) + 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Thread.sleep((long) (i2 * 1000));
                        waiting = false;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    execute = request(str, i + 1);
                }
                if (execute != null && execute.statusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    waiting = false;
                    return execute;
                } else if (!(execute == null || execute.statusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION)) {
                    waiting = false;
                    str = System.out;
                    i = new StringBuilder();
                    i.append("TMDB Request failed with code ");
                    i.append(String.valueOf(execute.statusCode()));
                    str.println(i.toString());
                }
            }
        } catch (String str2) {
            str2.printStackTrace();
            waiting = false;
        }
        return null;
    }

    public static java.util.LinkedHashSet<java.lang.String> getAlternativeTitles(java.lang.String r7, java.lang.String r8, java.lang.String r9, boolean r10) {
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
        r0 = new com.google.gson.Gson;
        r0.<init>();
        r0 = new java.util.LinkedHashSet;
        r0.<init>();
        r1 = new java.util.LinkedHashSet;
        r1.<init>();
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0104 }
        r2.<init>();	 Catch:{ Exception -> 0x0104 }
        r3 = "https://api.themoviedb.org/3/find/%s?api_key=";	 Catch:{ Exception -> 0x0104 }
        r2.append(r3);	 Catch:{ Exception -> 0x0104 }
        r3 = api_key;	 Catch:{ Exception -> 0x0104 }
        r2.append(r3);	 Catch:{ Exception -> 0x0104 }
        r3 = "&language=en-US&external_source=imdb_id";	 Catch:{ Exception -> 0x0104 }
        r2.append(r3);	 Catch:{ Exception -> 0x0104 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0104 }
        r3 = 1;	 Catch:{ Exception -> 0x0104 }
        r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0104 }
        r5 = 0;	 Catch:{ Exception -> 0x0104 }
        r4[r5] = r8;	 Catch:{ Exception -> 0x0104 }
        r8 = java.lang.String.format(r2, r4);	 Catch:{ Exception -> 0x0104 }
        r8 = request(r8, r3);	 Catch:{ Exception -> 0x0104 }
        r8 = r8.parse();	 Catch:{ Exception -> 0x0104 }
        r2 = "TMDB";	 Catch:{ Exception -> 0x0104 }
        r4 = r8.body();	 Catch:{ Exception -> 0x0104 }
        r4 = r4.text();	 Catch:{ Exception -> 0x0104 }
        android.util.Log.d(r2, r4);	 Catch:{ Exception -> 0x0104 }
        r2 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0104 }
        r8 = r8.body();	 Catch:{ Exception -> 0x0104 }
        r8 = r8.text();	 Catch:{ Exception -> 0x0104 }
        r2.<init>(r8);	 Catch:{ Exception -> 0x0104 }
        r8 = "movie_results";	 Catch:{ Exception -> 0x00e1 }
        r8 = r2.getJSONArray(r8);	 Catch:{ Exception -> 0x00e1 }
        r8 = r8.getJSONObject(r5);	 Catch:{ Exception -> 0x00e1 }
        r4 = "id";	 Catch:{ Exception -> 0x00e1 }
        r8 = r8.optInt(r4);	 Catch:{ Exception -> 0x00e1 }
        r4 = "movie_results";	 Catch:{ Exception -> 0x00e1 }
        r4 = r2.getJSONArray(r4);	 Catch:{ Exception -> 0x00e1 }
        r4 = r4.getJSONObject(r5);	 Catch:{ Exception -> 0x00e1 }
        r6 = "original_title";	 Catch:{ Exception -> 0x00e1 }
        r4 = r4.optString(r6);	 Catch:{ Exception -> 0x00e1 }
        r1.add(r4);	 Catch:{ Exception -> 0x00e1 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e1 }
        r4.<init>();	 Catch:{ Exception -> 0x00e1 }
        r6 = "https://api.themoviedb.org/3/movie/%d/alternative_titles?api_key=";	 Catch:{ Exception -> 0x00e1 }
        r4.append(r6);	 Catch:{ Exception -> 0x00e1 }
        r6 = api_key;	 Catch:{ Exception -> 0x00e1 }
        r4.append(r6);	 Catch:{ Exception -> 0x00e1 }
        r6 = "&country=US";	 Catch:{ Exception -> 0x00e1 }
        r4.append(r6);	 Catch:{ Exception -> 0x00e1 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e1 }
        r6 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x00e1 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x00e1 }
        r6[r5] = r8;	 Catch:{ Exception -> 0x00e1 }
        r8 = java.lang.String.format(r4, r6);	 Catch:{ Exception -> 0x00e1 }
        r8 = request(r8, r3);	 Catch:{ Exception -> 0x00e1 }
        r8 = r8.parse();	 Catch:{ Exception -> 0x00e1 }
        r3 = "TMDB";	 Catch:{ Exception -> 0x00e1 }
        r4 = r8.body();	 Catch:{ Exception -> 0x00e1 }
        r4 = r4.text();	 Catch:{ Exception -> 0x00e1 }
        android.util.Log.d(r3, r4);	 Catch:{ Exception -> 0x00e1 }
        r3 = new org.json.JSONObject;	 Catch:{ Exception -> 0x00e1 }
        r8 = r8.body();	 Catch:{ Exception -> 0x00e1 }
        r8 = r8.text();	 Catch:{ Exception -> 0x00e1 }
        r3.<init>(r8);	 Catch:{ Exception -> 0x00e1 }
        r8 = "titles";	 Catch:{ Exception -> 0x00e1 }
        r8 = r3.getJSONArray(r8);	 Catch:{ Exception -> 0x00e1 }
        r3 = 0;	 Catch:{ Exception -> 0x00e1 }
    L_0x00c3:
        r4 = r8.length();	 Catch:{ Exception -> 0x00e1 }
        if (r3 >= r4) goto L_0x0108;	 Catch:{ Exception -> 0x00e1 }
    L_0x00c9:
        r4 = r8.getJSONObject(r3);	 Catch:{ Exception -> 0x00e1 }
        r6 = "title";	 Catch:{ Exception -> 0x00e1 }
        r4 = r4.optString(r6);	 Catch:{ Exception -> 0x00e1 }
        if (r4 == 0) goto L_0x00de;	 Catch:{ Exception -> 0x00e1 }
    L_0x00d5:
        r6 = r4.isEmpty();	 Catch:{ Exception -> 0x00e1 }
        if (r6 != 0) goto L_0x00de;	 Catch:{ Exception -> 0x00e1 }
    L_0x00db:
        r1.add(r4);	 Catch:{ Exception -> 0x00e1 }
    L_0x00de:
        r3 = r3 + 1;
        goto L_0x00c3;
    L_0x00e1:
        r8 = "tv_results";	 Catch:{ Exception -> 0x0104 }
        r8 = r2.getJSONArray(r8);	 Catch:{ Exception -> 0x0104 }
        r8 = r8.getJSONObject(r5);	 Catch:{ Exception -> 0x0104 }
        r3 = "id";	 Catch:{ Exception -> 0x0104 }
        r8.optInt(r3);	 Catch:{ Exception -> 0x0104 }
        r8 = "tv_results";	 Catch:{ Exception -> 0x0104 }
        r8 = r2.getJSONArray(r8);	 Catch:{ Exception -> 0x0104 }
        r8 = r8.getJSONObject(r5);	 Catch:{ Exception -> 0x0104 }
        r2 = "original_name";	 Catch:{ Exception -> 0x0104 }
        r8 = r8.optString(r2);	 Catch:{ Exception -> 0x0104 }
        r1.add(r8);	 Catch:{ Exception -> 0x0104 }
        goto L_0x0108;
    L_0x0104:
        r8 = move-exception;
        r8.printStackTrace();
    L_0x0108:
        r1.add(r7);
        r7 = r1.iterator();
    L_0x010f:
        r8 = r7.hasNext();
        if (r8 == 0) goto L_0x0176;
    L_0x0115:
        r8 = r7.next();
        r8 = (java.lang.String) r8;
        r1 = "Marvel's";
        r2 = "";
        r1 = r8.replace(r1, r2);
        r2 = "Marvels";
        r3 = "";
        r1 = r1.replace(r2, r3);
        r2 = "Marvel";
        r3 = "";
        r1 = r1.replace(r2, r3);
        r1 = r1.trim();
        r0.add(r1);
        if (r10 == 0) goto L_0x0153;
    L_0x013c:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r8);
        r3 = " ";
        r2.append(r3);
        r2.append(r9);
        r2 = r2.toString();
        r0.add(r2);
    L_0x0153:
        r1 = r1.equals(r8);
        if (r1 != 0) goto L_0x010f;
    L_0x0159:
        r0.add(r8);
        if (r10 == 0) goto L_0x010f;
    L_0x015e:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r8);
        r8 = " ";
        r1.append(r8);
        r1.append(r9);
        r8 = r1.toString();
        r0.add(r8);
        goto L_0x010f;
    L_0x0176:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.helper.Tmdb.getAlternativeTitles(java.lang.String, java.lang.String, java.lang.String, boolean):java.util.LinkedHashSet<java.lang.String>");
    }
}
