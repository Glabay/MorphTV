package io.github.morpheustv.scrapers.providers;

import com.google.android.gms.common.internal.ImagesContract;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.Utils;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class YtsAgProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public YtsAgProvider(Scraper scraper) {
        super(scraper, "YTS.AM", false);
        this.domains = new String[]{"yts.am"};
        this.base_link = "https://yts.am";
        this.resourceWhitelist = new String[0];
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            ProviderSearchResult providerSearchResult = new ProviderSearchResult();
            providerSearchResult.setTitle((String) list.get(0));
            providerSearchResult.setYear(str);
            providerSearchResult.setPageUrl("");
            providerSearchResult.setImdb(str2);
            return providerSearchResult;
        } catch (List<String> list2) {
            list2.printStackTrace();
            return null;
        }
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        YtsAgProvider ytsAgProvider = this;
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ytsAgProvider.base_link);
        stringBuilder.append("/api/v2/list_movies.json?query_term=");
        stringBuilder.append(providerSearchResult.getImdb());
        JSONArray jSONArray = new JSONObject(Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).ignoreContentType(true).userAgent(UserAgent).timeout(10000).execute().body()).getJSONObject("data").getJSONArray("movies");
        if (jSONArray.length() > 0) {
            int i = 0;
            while (i < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject.optString("imdb_code").equals(providerSearchResult.getImdb())) {
                    String replace = jSONObject.optString("slug").replace("-", ".");
                    JSONArray jSONArray2 = jSONObject.getJSONArray("torrents");
                    if (jSONArray.length() > 0) {
                        int i2 = 0;
                        while (i2 < jSONArray2.length()) {
                            jSONObject = jSONArray2.getJSONObject(i2);
                            String optString = jSONObject.optString(ImagesContract.URL);
                            long optLong = jSONObject.optLong("size_bytes");
                            long optLong2 = jSONObject.optLong("seeds");
                            long optLong3 = jSONObject.optLong("peers");
                            int i3 = i2;
                            String optString2 = jSONObject.optString("quality");
                            String labelQuality = getLabelQuality(optString2);
                            JSONArray jSONArray3 = jSONArray2;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(replace);
                            String str = replace;
                            stringBuilder2.append(".");
                            stringBuilder2.append(optString2);
                            stringBuilder2.append(".[YTS-AM]");
                            replace = stringBuilder2.toString().toUpperCase();
                            r9 = new Object[3];
                            JSONArray jSONArray4 = jSONArray;
                            r9[0] = Utils.formatSize(ytsAgProvider.mContext, optLong);
                            r9[1] = Long.valueOf(optLong2);
                            r9[2] = Long.valueOf(optLong3);
                            String str2 = str;
                            JSONArray jSONArray5 = jSONArray3;
                            String str3 = str2;
                            long j = optLong;
                            int i4 = i;
                            addTorrentSource(optString, labelQuality, copyOnWriteArrayList, providerSearchResult.getTitle(), replace, String.format(" (%s - S:%d - L:%d)", r9), j, optLong2, optLong3);
                            i2 = i3 + 1;
                            replace = str3;
                            jSONArray2 = jSONArray5;
                            jSONArray = jSONArray4;
                            i = i4;
                        }
                    }
                }
                i++;
                jSONArray = jSONArray;
            }
        }
    }
}
