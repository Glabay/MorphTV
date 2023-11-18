package com.android.morpheustv.helpers;

import android.net.Uri;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.google.common.net.HttpHeaders;
import com.uwetrottmann.trakt5.TraktV2;
import io.github.morpheustv.scrapers.model.BaseProvider;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class Tmdb {
    private static String api_key = "e630fd9a85cfd7b8dd07cb41aabb6236";
    private static boolean waiting;

    public static class ImageResult {
        public String backdropUrl = "";
        public boolean isLoading = false;
        public String posterUrl = "";

        public ImageResult(String str, String str2) {
            this.posterUrl = str;
            this.backdropUrl = str2;
            this.isLoading = false;
        }
    }

    public static ImageResult getMovieImages(Integer num, String str) {
        if (num == null) {
            try {
                return new ImageResult("", "");
            } catch (Integer num2) {
                num2.printStackTrace();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MOVIE_POSTER_");
            stringBuilder.append(String.valueOf(num2));
            String loadCache = Utils.loadCache(stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("MOVIE_BACKDROP_");
            stringBuilder2.append(String.valueOf(num2));
            String loadCache2 = Utils.loadCache(stringBuilder2.toString());
            if (loadCache != null) {
                if (loadCache2 != null) {
                    return new ImageResult(loadCache, loadCache2);
                }
            }
            PrintStream printStream = System.out;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Loading posters for movie ");
            stringBuilder2.append(String.valueOf(num2));
            stringBuilder2.append("...");
            printStream.println(stringBuilder2.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.themoviedb.org/3/movie/");
            stringBuilder.append(String.valueOf(num2));
            stringBuilder.append("/images?");
            str = request(Uri.parse(stringBuilder.toString()).buildUpon().appendQueryParameter("api_key", api_key).appendQueryParameter("language", str).build().toString(), 1);
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str.body());
                str = jSONObject.getJSONArray("posters");
                JSONArray jSONArray = jSONObject.getJSONArray("backdrops");
                loadCache2 = "";
                if (str != null && str.length() > 0) {
                    str = str.getJSONObject(0).getString("file_path");
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("http://image.tmdb.org/t/p/w500");
                    stringBuilder3.append(str);
                    loadCache2 = stringBuilder3.toString();
                }
                str = "";
                if (jSONArray != null && jSONArray.length() > 0) {
                    str = jSONArray.getJSONObject(0).getString("file_path");
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("http://image.tmdb.org/t/p/w780");
                    stringBuilder4.append(str);
                    str = stringBuilder4.toString();
                }
                if (!loadCache2.isEmpty()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("MOVIE_POSTER_");
                    stringBuilder.append(String.valueOf(num2));
                    Utils.saveCache(stringBuilder.toString(), loadCache2);
                }
                if (!str.isEmpty()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("MOVIE_BACKDROP_");
                    stringBuilder.append(String.valueOf(num2));
                    Utils.saveCache(stringBuilder.toString(), str);
                }
                return new ImageResult(loadCache2, str);
            }
            return new ImageResult("", "");
        }
    }

    public static ImageResult getShowImages(Integer num, String str) {
        if (num == null) {
            try {
                return new ImageResult("", "");
            } catch (Integer num2) {
                num2.printStackTrace();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SHOW_POSTER_");
            stringBuilder.append(String.valueOf(num2));
            String loadCache = Utils.loadCache(stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("SHOW_BACKDROP_");
            stringBuilder2.append(String.valueOf(num2));
            String loadCache2 = Utils.loadCache(stringBuilder2.toString());
            if (loadCache != null) {
                if (loadCache2 != null) {
                    return new ImageResult(loadCache, loadCache2);
                }
            }
            PrintStream printStream = System.out;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Loading posters for tv show ");
            stringBuilder2.append(String.valueOf(num2));
            stringBuilder2.append("...");
            printStream.println(stringBuilder2.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.themoviedb.org/3/tv/");
            stringBuilder.append(String.valueOf(num2));
            stringBuilder.append("/images?");
            str = request(Uri.parse(stringBuilder.toString()).buildUpon().appendQueryParameter("api_key", api_key).appendQueryParameter("language", str).build().toString(), 1);
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str.body());
                str = jSONObject.getJSONArray("posters");
                JSONArray jSONArray = jSONObject.getJSONArray("backdrops");
                loadCache2 = "";
                if (str != null && str.length() > 0) {
                    str = str.getJSONObject(0).getString("file_path");
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("http://image.tmdb.org/t/p/w500");
                    stringBuilder3.append(str);
                    loadCache2 = stringBuilder3.toString();
                }
                str = "";
                if (jSONArray != null && jSONArray.length() > 0) {
                    str = jSONArray.getJSONObject(0).getString("file_path");
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("http://image.tmdb.org/t/p/w780");
                    stringBuilder4.append(str);
                    str = stringBuilder4.toString();
                }
                if (!loadCache2.isEmpty()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SHOW_POSTER_");
                    stringBuilder.append(String.valueOf(num2));
                    Utils.saveCache(stringBuilder.toString(), loadCache2);
                }
                if (!str.isEmpty()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SHOW_BACKDROP_");
                    stringBuilder.append(String.valueOf(num2));
                    Utils.saveCache(stringBuilder.toString(), str);
                }
                return new ImageResult(loadCache2, str);
            }
            return new ImageResult("", "");
        }
    }

    public static ImageResult getSeasonImages(Integer num, int i, String str) {
        if (num == null) {
            try {
                return new ImageResult("", "");
            } catch (Integer num2) {
                num2.printStackTrace();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SEASON_POSTER_");
            stringBuilder.append(String.valueOf(num2));
            stringBuilder.append("_");
            stringBuilder.append(String.valueOf(i));
            String loadCache = Utils.loadCache(stringBuilder.toString());
            if (loadCache != null) {
                return new ImageResult(loadCache, "");
            }
            PrintStream printStream = System.out;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Loading posters for season ");
            stringBuilder2.append(String.valueOf(num2));
            stringBuilder2.append("...");
            printStream.println(stringBuilder2.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.themoviedb.org/3/tv/");
            stringBuilder.append(String.valueOf(num2));
            stringBuilder.append("/season/");
            stringBuilder.append(String.valueOf(i));
            stringBuilder.append("/images?");
            str = request(Uri.parse(stringBuilder.toString()).buildUpon().appendQueryParameter("api_key", api_key).appendQueryParameter("language", str).build().toString(), 1);
            if (str != null) {
                str = new JSONObject(str.body()).getJSONArray("posters");
                loadCache = "";
                if (str != null && str.length() > 0) {
                    str = str.getJSONObject(0).getString("file_path");
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("http://image.tmdb.org/t/p/w500");
                    stringBuilder2.append(str);
                    loadCache = stringBuilder2.toString();
                }
                if (loadCache.isEmpty() == null) {
                    str = new StringBuilder();
                    str.append("SEASON_POSTER_");
                    str.append(String.valueOf(num2));
                    str.append("_");
                    str.append(String.valueOf(i));
                    Utils.saveCache(str.toString(), loadCache);
                }
                return new ImageResult(loadCache, "");
            }
            return new ImageResult("", "");
        }
    }

    public static ImageResult getEpisodeImages(Integer num, int i, int i2, String str) {
        if (num == null) {
            try {
                return new ImageResult("", "");
            } catch (Integer num2) {
                num2.printStackTrace();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EPISODE_POSTER_");
            stringBuilder.append(String.valueOf(num2));
            stringBuilder.append("_");
            stringBuilder.append(String.valueOf(i));
            stringBuilder.append("_");
            stringBuilder.append(String.valueOf(i2));
            String loadCache = Utils.loadCache(stringBuilder.toString());
            if (loadCache != null) {
                return new ImageResult(loadCache, "");
            }
            PrintStream printStream = System.out;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Loading posters for episode ");
            stringBuilder2.append(String.valueOf(num2));
            stringBuilder2.append("...");
            printStream.println(stringBuilder2.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.themoviedb.org/3/tv/");
            stringBuilder.append(String.valueOf(num2));
            stringBuilder.append("/season/");
            stringBuilder.append(String.valueOf(i));
            stringBuilder.append("/episode/");
            stringBuilder.append(String.valueOf(i2));
            stringBuilder.append("/images?");
            str = request(Uri.parse(stringBuilder.toString()).buildUpon().appendQueryParameter("api_key", api_key).appendQueryParameter("language", str).build().toString(), 1);
            if (str != null) {
                str = new JSONObject(str.body()).getJSONArray("stills");
                loadCache = "";
                if (str != null && str.length() > 0) {
                    str = str.getJSONObject(0).getString("file_path");
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("http://image.tmdb.org/t/p/w300");
                    stringBuilder2.append(str);
                    loadCache = stringBuilder2.toString();
                }
                if (loadCache.isEmpty() == null) {
                    str = new StringBuilder();
                    str.append("EPISODE_POSTER_");
                    str.append(String.valueOf(num2));
                    str.append("_");
                    str.append(String.valueOf(i));
                    str.append("_");
                    str.append(String.valueOf(i2));
                    Utils.saveCache(str.toString(), loadCache);
                }
                return new ImageResult(loadCache, "");
            }
            return new ImageResult("", "");
        }
    }

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
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.helpers.Tmdb.getAlternativeTitles(java.lang.String, java.lang.String, java.lang.String, boolean):java.util.LinkedHashSet<java.lang.String>");
    }
}
