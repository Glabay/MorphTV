package io.github.morpheustv.scrapers.providers;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
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

public class EZTVProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public EZTVProvider(Scraper scraper) {
        super(scraper, "EZTV.AG", false);
        this.domains = new String[]{"eztv.re"};
        this.base_link = "https://eztv.re";
        this.resourceWhitelist = new String[0];
    }

    public ProviderSearchResult getTvShow(List<String> list, String str, String str2) {
        try {
            String str3 = (String) list.get(0);
            ProviderSearchResult providerSearchResult = new ProviderSearchResult();
            providerSearchResult.setTitle(str3);
            providerSearchResult.setTitles(list);
            providerSearchResult.setYear(str);
            providerSearchResult.setImdb(str2);
            providerSearchResult.setPageUrl("");
            return providerSearchResult;
        } catch (List<String> list2) {
            list2.printStackTrace();
            return null;
        }
    }

    public ProviderSearchResult getEpisode(ProviderSearchResult providerSearchResult, List<String> list, String str, int i, int i2) {
        if (providerSearchResult != null) {
            try {
                if (providerSearchResult.getPageUrl() != null) {
                    ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                    providerSearchResult2.setTitle((String) list.get(0));
                    providerSearchResult2.setTitles(list);
                    providerSearchResult2.setYear(str);
                    providerSearchResult2.setPageUrl("");
                    providerSearchResult2.setEpisode(i2);
                    providerSearchResult2.setSeason(i);
                    providerSearchResult2.setImdb(providerSearchResult.getImdb());
                    return providerSearchResult2;
                }
            } catch (ProviderSearchResult providerSearchResult3) {
                providerSearchResult3.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        EZTVProvider eZTVProvider = this;
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            boolean z = true;
            int i = 1;
            while (i < 10) {
                try {
                    Object obj;
                    int i2;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(eZTVProvider.base_link);
                    stringBuilder.append("/api/get-torrents?limit=100&page=");
                    stringBuilder.append(String.valueOf(i));
                    stringBuilder.append("&imdb_id=");
                    stringBuilder.append(providerSearchResult.getImdb().replace(TtmlNode.TAG_TT, ""));
                    JSONArray jSONArray = new JSONObject(Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).ignoreContentType(z).userAgent(UserAgent).timeout(10000).execute().body()).getJSONArray("torrents");
                    if (jSONArray.length() > 0) {
                        Object obj2 = null;
                        int i3 = 0;
                        while (i3 < jSONArray.length()) {
                            int i4;
                            JSONArray jSONArray2;
                            JSONObject jSONObject = jSONArray.getJSONObject(i3);
                            String optString = jSONObject.optString("filename");
                            String optString2 = jSONObject.optString("magnet_url");
                            long optLong = jSONObject.optLong("size_bytes");
                            int i5 = i;
                            long optLong2 = jSONObject.optLong("seeds");
                            JSONArray jSONArray3 = jSONArray;
                            long optLong3 = jSONObject.optLong("peers");
                            String qualityFromUrl = BaseProvider.getQualityFromUrl(optString);
                            r9 = new Object[3];
                            obj = obj2;
                            r9[0] = Utils.formatSize(eZTVProvider.mContext, optLong);
                            r9[1] = Long.valueOf(optLong2);
                            long j = optLong;
                            r9[2] = Long.valueOf(optLong3);
                            String format = String.format(" (%s - S:%d - L:%d)", r9);
                            if (optString.contains(String.format("S%02dE%02d", new Object[]{Integer.valueOf(providerSearchResult.getSeason()), Integer.valueOf(providerSearchResult.getEpisode())}))) {
                                i4 = i3;
                                jSONArray2 = jSONArray3;
                                i2 = i5;
                                addTorrentSource(optString2, qualityFromUrl, copyOnWriteArrayList, providerSearchResult.getTitle(), optString, format, j, optLong2, optLong3);
                                obj2 = 1;
                            } else {
                                i4 = i3;
                                i2 = i5;
                                jSONArray2 = jSONArray3;
                                obj2 = obj;
                            }
                            i3 = i4 + 1;
                            i = i2;
                            jSONArray = jSONArray2;
                        }
                        obj = obj2;
                        i2 = i;
                    } else {
                        i2 = i;
                        obj = null;
                    }
                    if (obj == null) {
                        i = i2 + 1;
                        z = true;
                    } else {
                        return;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
    }
}
