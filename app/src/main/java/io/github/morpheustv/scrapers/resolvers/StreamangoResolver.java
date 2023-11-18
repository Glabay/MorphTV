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
import org.jsoup.nodes.Element;

public class StreamangoResolver extends BaseResolver {
    public StreamangoResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        StreamangoResolver streamangoResolver = this;
        try {
            String attr;
            Matcher matcher;
            String wvgethtml = wvgethtml(str);
            try {
                Element first = Jsoup.parse(wvgethtml).select("meta[name=description]").first();
                if (first != null) {
                    attr = first.attr("content");
                    matcher = Pattern.compile("\\.push\\((.*video\\/mp4.*)\\);").matcher(wvgethtml.replace("\"//streamango", "\"http://streamango"));
                    if (matcher.find() && matcher.groupCount() > 0) {
                        wvgethtml = matcher.group(1);
                        if (wvgethtml.contains("src:d(")) {
                            Matcher matcher2 = Pattern.compile("src:d\\((.*?)\\)").matcher(wvgethtml);
                            while (matcher2.find() && matcher2.groupCount() > 0) {
                                CharSequence group = matcher2.group();
                                String group2 = matcher2.group(1);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("(function() { return (d(");
                                stringBuilder.append(group2);
                                stringBuilder.append(")); })();");
                                group2 = wveval(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("src:");
                                stringBuilder.append(group2);
                                wvgethtml = wvgethtml.replace(group, stringBuilder.toString()).replace("\"//streamango", "\"http://streamango");
                            }
                        }
                        wvgethtml = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(wvgethtml));
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("[");
                        stringBuilder2.append(wvgethtml);
                        stringBuilder2.append("]");
                        JSONArray jSONArray = new JSONArray(stringBuilder2.toString());
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject = jSONArray.getJSONObject(i);
                            String optString = jSONObject.optString("src");
                            if (optString.contains("streamango.com/v/d")) {
                                String labelQuality = getLabelQuality(String.valueOf(jSONObject.optInt("height")));
                                String redirectOnce = redirectOnce(optString, str);
                                if (!(redirectOnce == null || redirectOnce.isEmpty())) {
                                    Source source = r4;
                                    Source source2 = new Source(str3, "STREAMANGO", labelQuality, streamangoResolver.PROVIDER_NAME, redirectOnce, attr, "");
                                    addSource(copyOnWriteArrayList, source, false, true);
                                }
                            } else {
                                String str4 = str;
                            }
                            CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
                        }
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            attr = str3;
            matcher = Pattern.compile("\\.push\\((.*video\\/mp4.*)\\);").matcher(wvgethtml.replace("\"//streamango", "\"http://streamango"));
            if (matcher.find()) {
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public Executor getExecutor() {
        return TaskManager.STREAMANGO_EXECUTOR;
    }
}
