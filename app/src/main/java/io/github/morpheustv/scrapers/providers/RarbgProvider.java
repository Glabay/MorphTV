package io.github.morpheustv.scrapers.providers;

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

public class RarbgProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public RarbgProvider(Scraper scraper) {
        super(scraper, "RARBG.TO", false);
        this.domains = new String[]{"torrentapi.org"};
        this.base_link = "https://torrentapi.org";
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
        Exception e;
        Exception exception;
        int i;
        RarbgProvider rarbgProvider = this;
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            boolean z = false;
            int i2 = 0;
            while (i2 < 3) {
                try {
                    Object obj;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(rarbgProvider.base_link);
                    stringBuilder.append("/pubapi_v2.php?get_token=get_token&app_id=Radarr");
                    String optString = new JSONObject(Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(z).ignoreContentType(true).userAgent(UserAgent).timeout(15000).execute().body()).optString("token");
                    Thread.sleep(1000);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(rarbgProvider.base_link);
                    stringBuilder2.append("/pubapi_v2.php?mode=search&app_id=Radarr&search_imdb=");
                    stringBuilder2.append(providerSearchResult.getImdb());
                    stringBuilder2.append("&format=json_extended&limit=50&sort=seeders&token=");
                    stringBuilder2.append(optString);
                    String stringBuilder3 = stringBuilder2.toString();
                    if (providerSearchResult.getSeason() > 0) {
                        try {
                            if (providerSearchResult.getEpisode() > 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(rarbgProvider.base_link);
                                stringBuilder2.append("/pubapi_v2.php?mode=search&app_id=Radarr&search_string=");
                                stringBuilder2.append(String.format("%s S%02dE%02d", new Object[]{providerSearchResult.getTitle(), Integer.valueOf(providerSearchResult.getSeason()), Integer.valueOf(providerSearchResult.getEpisode())}));
                                stringBuilder2.append("&format=json_extended&limit=50&sort=seeders&token=");
                                stringBuilder2.append(optString);
                                stringBuilder3 = stringBuilder2.toString();
                            }
                        } catch (Exception e22) {
                            exception = e22;
                            i = i2;
                            exception.printStackTrace();
                            i2 = i + 1;
                            z = false;
                        }
                    }
                    JSONArray jSONArray = new JSONObject(Jsoup.connect(stringBuilder3).validateTLSCertificates(z).ignoreContentType(true).userAgent(UserAgent).timeout(15000).execute().body()).getJSONArray("torrent_results");
                    if (jSONArray.length() > 0) {
                        int i3 = 0;
                        while (i3 < jSONArray.length()) {
                            JSONObject jSONObject = jSONArray.getJSONObject(i3);
                            String optString2 = jSONObject.optString("title");
                            String optString3 = jSONObject.optString("download");
                            long optLong = jSONObject.optLong("size");
                            long optLong2 = jSONObject.optLong("seeders");
                            long optLong3 = jSONObject.optLong("leechers");
                            stringBuilder3 = BaseProvider.getQualityFromUrl(optString2);
                            optString = " (%s - S:%d - L:%d)";
                            Object[] objArr = new Object[3];
                            objArr[0] = Utils.formatSize(rarbgProvider.mContext, optLong);
                            int i4 = i2;
                            long j = optLong2;
                            try {
                                objArr[1] = Long.valueOf(j);
                                objArr[2] = Long.valueOf(optLong3);
                                JSONArray jSONArray2 = jSONArray;
                                int i5 = i3;
                                i = i4;
                                try {
                                    addTorrentSource(optString3, stringBuilder3, copyOnWriteArrayList, providerSearchResult.getTitle(), optString2, String.format(optString, objArr), optLong, j, optLong3);
                                    i3 = i5 + 1;
                                    i2 = i;
                                    jSONArray = jSONArray2;
                                } catch (Exception e3) {
                                    e22 = e3;
                                }
                            } catch (Exception e4) {
                                e22 = e4;
                                i = i4;
                            }
                        }
                        i = i2;
                        obj = 1;
                    } else {
                        i = i2;
                        obj = null;
                    }
                    if (obj == null) {
                        i2 = i + 1;
                        z = false;
                    } else {
                        return;
                    }
                } catch (Exception e5) {
                    e22 = e5;
                    i = i2;
                    exception = e22;
                    exception.printStackTrace();
                    i2 = i + 1;
                    z = false;
                }
            }
        }
    }
}
