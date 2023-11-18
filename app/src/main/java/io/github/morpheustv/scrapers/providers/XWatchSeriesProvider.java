package io.github.morpheustv.scrapers.providers;

import android.net.Uri;
import android.util.Base64;
import android.webkit.CookieManager;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.lingala.zip4j.util.InternalZipConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class XWatchSeriesProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public XWatchSeriesProvider(Scraper scraper) {
        super(scraper, "SWATCHSERIES.TO", true);
        this.domains = new String[]{"swatchseries.to"};
        this.base_link = "https://www1.swatchseries.to";
        this.resourceWhitelist = new String[]{"(.*/search/.*)"};
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
                    wvgethtml(this.base_link);
                    providerSearchResult = CookieManager.getInstance().getCookie(this.base_link);
                    for (String str2 : list) {
                        String optString;
                        URL url = new URL(this.base_link);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("/show/search-shows-json/");
                        stringBuilder.append(str2);
                        int i3 = 0;
                        JSONArray jSONArray = new JSONArray(Jsoup.connect(new URL(url, stringBuilder.toString()).toString()).validateTLSCertificates(false).ignoreContentType(true).header(HttpHeaders.COOKIE, providerSearchResult != null ? providerSearchResult : "").header("referer", this.base_link).header("x-requested-with", "XMLHttpRequest").header("accept", "application/json, text/javascript, */*; q=0.01").userAgent(UserAgent).method(Method.GET).timeout(10000).execute().body());
                        while (i3 < jSONArray.length()) {
                            JSONObject jSONObject = jSONArray.getJSONObject(i3);
                            String replace = jSONObject.optString("label").replace("(US)", "");
                            optString = jSONObject.optString("seo_url");
                            replace = BaseProvider.cleantitle(replace);
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(str2);
                            stringBuilder2.append(str);
                            if (replace.equals(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(this.base_link);
                                stringBuilder3.append("/serie/");
                                stringBuilder3.append(optString);
                                optString = stringBuilder3.toString();
                                continue;
                                break;
                            }
                            i3++;
                        }
                        optString = null;
                        continue;
                        if (optString != null) {
                            providerSearchResult = new ProviderSearchResult();
                            providerSearchResult.setTitle(str2);
                            providerSearchResult.setYear(str);
                            list = new StringBuilder();
                            list.append(optString.replace("/serie/", "/episode/"));
                            list.append("_s");
                            list.append(String.valueOf(i));
                            list.append("_e");
                            list.append(String.valueOf(i2));
                            list.append(".html");
                            providerSearchResult.setPageUrl(list.toString());
                            providerSearchResult.setEpisode(i2);
                            providerSearchResult.setSeason(i);
                            return providerSearchResult;
                        }
                    }
                }
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            Iterator it = Jsoup.connect(providerSearchResult.getPageUrl()).validateTLSCertificates(false).ignoreContentType(true).userAgent(UserAgent).timeout(10000).get().select("div#linktable").first().select("tr").iterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                if (isAlive()) {
                    String attr;
                    String str;
                    if (element.hasClass("download_link_speedvid.net")) {
                        try {
                            attr = ((Element) element.select("a").get(0)).attr("href");
                            str = new String(Base64.decode(Uri.parse(attr).getQueryParameter(InternalZipConstants.READ_MODE), 0), "UTF-8");
                            if (str.contains("speedvid")) {
                                processLink(str, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (element.hasClass("download_link_openload.co")) {
                        try {
                            attr = ((Element) element.select("a").get(0)).attr("href");
                            str = new String(Base64.decode(Uri.parse(attr).getQueryParameter(InternalZipConstants.READ_MODE), 0), "UTF-8");
                            if (str.contains("openload")) {
                                processLink(str, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (element.hasClass("download_link_vidoza.net")) {
                        try {
                            attr = ((Element) element.select("a").get(0)).attr("href");
                            str = new String(Base64.decode(Uri.parse(attr).getQueryParameter(InternalZipConstants.READ_MODE), 0), "UTF-8");
                            if (str.contains("vidoza")) {
                                processLink(str, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            }
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (element.hasClass("download_link_vidup.me")) {
                        try {
                            attr = ((Element) element.select("a").get(0)).attr("href");
                            str = new String(Base64.decode(Uri.parse(attr).getQueryParameter(InternalZipConstants.READ_MODE), 0), "UTF-8");
                            if (str.contains("vidup")) {
                                processLink(str, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            }
                        } catch (Exception e222) {
                            e222.printStackTrace();
                        }
                    }
                    if (element.hasClass("download_link_vidlox.tv")) {
                        try {
                            attr = ((Element) element.select("a").get(0)).attr("href");
                            str = new String(Base64.decode(Uri.parse(attr).getQueryParameter(InternalZipConstants.READ_MODE), 0), "UTF-8");
                            if (str.contains("vidlox")) {
                                processLink(str, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            }
                        } catch (Exception e2222) {
                            e2222.printStackTrace();
                        }
                    }
                    if (element.hasClass("download_link_streamango.com")) {
                        try {
                            str = ((Element) element.select("a").get(0)).attr("href");
                            String str2 = new String(Base64.decode(Uri.parse(str).getQueryParameter(InternalZipConstants.READ_MODE), 0), "UTF-8");
                            if (str2.contains("streamango")) {
                                processLink(str2, str, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            } else {
                                continue;
                            }
                        } catch (Exception e3) {
                            try {
                                e3.printStackTrace();
                            } catch (ProviderSearchResult providerSearchResult2) {
                                providerSearchResult2.printStackTrace();
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }
}
