package io.github.morpheustv.scrapers.providers;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AfdahProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public AfdahProvider(Scraper scraper) {
        super(scraper, "AFDAH.info", true);
        this.domains = new String[]{"afdah.info"};
        this.base_link = "https://afdah.info";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*cookie.*.js.*)", "(.*jquery.*.js.*)", "(.*jwplayer.*.js.*)", "(.*apps/head.*.js.*)", "(.*show-ads.*.js.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
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
                    String str2 = (String) list.get(0);
                    ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                    providerSearchResult2.setTitle(str2);
                    providerSearchResult2.setTitles(list);
                    providerSearchResult2.setYear(str);
                    providerSearchResult2.setPageUrl("");
                    providerSearchResult2.setSeason(i);
                    providerSearchResult2.setEpisode(i2);
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
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        } else {
            int i = 1;
            while (i <= 6) {
                try {
                    String str = "/embed/";
                    if (i > 1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("/embed");
                        stringBuilder.append(String.valueOf(i));
                        stringBuilder.append("/");
                        str = stringBuilder.toString();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this.base_link);
                    stringBuilder2.append(str);
                    stringBuilder2.append(providerSearchResult2.getImdb().replace(TtmlNode.TAG_TT, ""));
                    str = stringBuilder2.toString();
                    if (providerSearchResult2.getSeason() > 0 && providerSearchResult2.getEpisode() > 0) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str);
                        stringBuilder2.append(String.format("s%02de%02d", new Object[]{Integer.valueOf(providerSearchResult2.getSeason()), Integer.valueOf(providerSearchResult2.getEpisode())}));
                        str = stringBuilder2.toString();
                    }
                    Document parse = Jsoup.parse(wvgethtml(str));
                    Matcher matcher = Pattern.compile("sources\\s*:\\s*(\\[.*?\\])").matcher(parse.html().replace("\n", "").replace(StringUtils.CR, "").replace("\t", ""));
                    String attr;
                    if (!matcher.find() || matcher.groupCount() <= 0) {
                        attr = parse.select("iframe").first().attr("src");
                        if (!(attr == null || attr.isEmpty())) {
                            processLink(attr, str, null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                            if (hasMaxSources(copyOnWriteArrayList)) {
                                return;
                            }
                        }
                        i++;
                    } else {
                        String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(matcher.group(1)));
                        if (unescapeHtml4 == null) {
                            continue;
                        } else {
                            if (unescapeHtml4.startsWith("\"")) {
                                unescapeHtml4 = unescapeHtml4.replaceAll("^\"|\"$", "");
                            }
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("{\"sources\":");
                            stringBuilder3.append(unescapeHtml4);
                            stringBuilder3.append("}");
                            JSONArray jSONArray = new JSONObject(stringBuilder3.toString()).getJSONArray("sources");
                            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                                attr = jSONObject.optString("file").replace("\"", "");
                                if (!attr.startsWith("http")) {
                                    stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("http:");
                                    stringBuilder3.append(attr);
                                    attr = stringBuilder3.toString();
                                }
                                String replace = jSONObject.optString("label").replace("\"", "");
                                if (!attr.isEmpty()) {
                                    processLink(redirectOnce(attr, attr), str, replace, copyOnWriteArrayList, providerSearchResult2.getTitle());
                                    if (hasMaxSources(copyOnWriteArrayList)) {
                                        return;
                                    }
                                }
                            }
                            continue;
                        }
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
