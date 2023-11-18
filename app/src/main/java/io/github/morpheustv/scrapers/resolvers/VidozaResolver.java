package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.TaskManager;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class VidozaResolver extends BaseResolver {
    public VidozaResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        VidozaResolver vidozaResolver = this;
        String str4 = str2;
        CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
        try {
            Document parse = Jsoup.parse(str);
            Matcher matcher = Pattern.compile("sources\\s*:\\s*(\\[.*\\])").matcher(parse.html());
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
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        String replace = jSONObject.optString("file").replace("\"", "");
                        if (!replace.startsWith("http")) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("http:");
                            stringBuilder2.append(replace);
                            replace = stringBuilder2.toString();
                        }
                        String replace2 = jSONObject.optString("label").replace("\"", "");
                        if (!replace.isEmpty()) {
                            String str5 = str3;
                            Source source = r11;
                            Source source2 = new Source(str5, "VIDOZA", getLabelQuality(replace2), vidozaResolver.PROVIDER_NAME, replace, "");
                            source.setReferer(str4);
                            addSource(copyOnWriteArrayList2, source, false, true);
                        }
                    }
                }
            }
            Matcher matcher2 = Pattern.compile("sourcesCode\\s*:\\s*(\\[.*\\])").matcher(parse.html());
            if (matcher2.find() && matcher2.groupCount() > 0) {
                String unescapeHtml42 = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(matcher2.group(1)));
                if (unescapeHtml42 != null) {
                    if (unescapeHtml42.startsWith("\"")) {
                        unescapeHtml42 = unescapeHtml42.replaceAll("^\"|\"$", "");
                    }
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("{\"sources\":");
                    stringBuilder3.append(unescapeHtml42);
                    stringBuilder3.append("}");
                    JSONArray jSONArray2 = new JSONObject(stringBuilder3.toString()).getJSONArray("sources");
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                        String replace3 = jSONObject2.optString("src").replace("\"", "");
                        if (!replace3.startsWith("http")) {
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("http:");
                            stringBuilder4.append(replace3);
                            replace3 = stringBuilder4.toString();
                        }
                        String str6 = replace3;
                        String replace4 = jSONObject2.optString("label").replace("\"", "");
                        if (!str6.isEmpty()) {
                            Source source3 = new Source(str3, "VIDOZA", getLabelQuality(replace4), vidozaResolver.PROVIDER_NAME, str6, "");
                            source3.setReferer(str4);
                            addSource(copyOnWriteArrayList2, source3, false, true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Executor getExecutor() {
        return TaskManager.THEVIDEO_EXECUTOR;
    }
}
