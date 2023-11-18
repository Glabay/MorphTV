package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class VidloxResolver extends BaseResolver {
    public VidloxResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        String str4 = str2;
        try {
            String text;
            CharSequence wvgethtml = wvgethtml(str, str4, false);
            try {
                text = Jsoup.parse(wvgethtml).select("h1").first().text();
            } catch (Exception e) {
                e.printStackTrace();
                text = str3;
            }
            List arrayList = new ArrayList();
            List asList = Arrays.asList(new String[]{"480p", "720p", BaseProvider.FULLHD, "2160p"});
            Matcher matcher = Pattern.compile("sources\\s*:\\s*(\\[.*\\])").matcher(wvgethtml);
            if (matcher.find() && matcher.groupCount() > 0) {
                String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(matcher.group(1)));
                if (unescapeHtml4 != null) {
                    if (unescapeHtml4.startsWith("\"")) {
                        unescapeHtml4 = unescapeHtml4.replaceAll("^\"|\"$", "");
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("{\"sources\":");
                    stringBuilder.append(unescapeHtml4);
                    stringBuilder.append("}");
                    JSONArray jSONArray = new JSONObject(stringBuilder.toString()).getJSONArray("sources");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        Object optString = jSONArray.optString(i);
                        if (!optString.startsWith("http")) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("http:");
                            stringBuilder2.append(optString);
                            optString = stringBuilder2.toString();
                        }
                        arrayList.add(optString);
                    }
                }
            }
            int i2 = 0;
            while (i2 < arrayList.size()) {
                String str5;
                String str6 = (String) arrayList.get(i2);
                String str7 = (String) asList.get(i2);
                if (str6.isEmpty()) {
                    CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
                    str5 = text;
                } else {
                    String str8 = "VIDLOX";
                    if (str6.contains("m3u8")) {
                        str8 = "VIDLOX HLS";
                    }
                    Source source = r6;
                    str5 = text;
                    Source source2 = new Source(str3, str8, getLabelQuality(str7), r1.PROVIDER_NAME, str6, text, "");
                    source.setReferer(str4);
                    addSource(copyOnWriteArrayList, source, false, true);
                }
                i2++;
                text = str5;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
