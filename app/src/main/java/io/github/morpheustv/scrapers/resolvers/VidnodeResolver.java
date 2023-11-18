package io.github.morpheustv.scrapers.resolvers;

import android.support.graphics.drawable.PathInterpolatorCompat;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class VidnodeResolver extends BaseResolver {
    public VidnodeResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, false);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        VidnodeResolver vidnodeResolver = this;
        String str4 = str;
        CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
        if (str4 != null) {
            try {
                if (!str.isEmpty()) {
                    StringBuilder stringBuilder;
                    String attr;
                    String attr2;
                    if (!str4.startsWith("http")) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("https:");
                        stringBuilder.append(str4);
                        str4 = stringBuilder.toString();
                    }
                    String str5 = str4;
                    Document parse = Jsoup.parse(Jsoup.connect(str5).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest").referrer(str5).method(Method.GET).execute().body());
                    try {
                        Iterator it = parse.select("source").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            attr = element.attr("label");
                            attr2 = element.attr("src");
                            if (!attr2.contains("vidnode.net")) {
                                processLink(attr2, str5, attr, copyOnWriteArrayList2, str3);
                                if (hasMaxSources(copyOnWriteArrayList2)) {
                                    return;
                                }
                            } else if (attr2.contains(".mp4") || attr2.contains(".m3u8")) {
                                Source source = new Source(str3, getCDNSource(attr2), getLabelQuality(attr), vidnodeResolver.PROVIDER_NAME, attr2);
                                source.setReferer(str5);
                                addSource(copyOnWriteArrayList2, source, false, true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Matcher matcher = Pattern.compile("sources\\s*:\\s*(\\[.*\\])").matcher(parse.html());
                        while (matcher.find() && matcher.groupCount() > 0) {
                            str4 = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(matcher.group(1)));
                            if (str4 != null) {
                                if (str4.startsWith("\"")) {
                                    str4 = str4.replaceAll("^\"|\"$", "");
                                }
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("{\"sources\":");
                                stringBuilder.append(str4);
                                stringBuilder.append("}");
                                JSONArray jSONArray = new JSONObject(stringBuilder.toString()).getJSONArray("sources");
                                for (int i = 0; i < jSONArray.length(); i++) {
                                    try {
                                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                                        attr2 = jSONObject.optString("file").replace("\"", "");
                                        if (!attr2.startsWith("http")) {
                                            StringBuilder stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append("http:");
                                            stringBuilder2.append(attr2);
                                            attr2 = stringBuilder2.toString();
                                        }
                                        attr = jSONObject.optString("label").replace("\"", "");
                                        if (attr2.isEmpty()) {
                                            continue;
                                        } else if (!attr2.contains("vidnode.net")) {
                                            processLink(attr2, str5, attr, copyOnWriteArrayList2, str3);
                                            if (hasMaxSources(copyOnWriteArrayList2)) {
                                                return;
                                            }
                                        } else if (attr2.contains(".mp4") || attr2.contains(".m3u8")) {
                                            Source source2 = new Source(str3, getCDNSource(attr2), getLabelQuality(attr), vidnodeResolver.PROVIDER_NAME, attr2);
                                            source2.setReferer(str5);
                                            addSource(copyOnWriteArrayList2, source2, false, true);
                                        }
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                                continue;
                            }
                        }
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Exception e222) {
                e222.printStackTrace();
            }
        }
    }
}
